# -*- coding: utf-8 -*-
"""
小智语音助手 · Python 后端服务（FastAPI）

功能：
  GET  /api/health  健康检查（含 LLM 配置状态）
  POST /api/chat    大模型流式对话（SSE）
  POST /api/asr     语音识别（faster-whisper，接收前端录音 webm/opus）
  GET  /api/tts     语音合成（edge-tts，微软语音，返回 mp3）

运行：uvicorn app:app --host 0.0.0.0 --port 8000
依赖：pip install -r requirements.txt
"""
import io
import json
import os
import tempfile
import uuid

from dotenv import load_dotenv
from fastapi import FastAPI, File, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse, JSONResponse, StreamingResponse
from pydantic import BaseModel

load_dotenv()

# ---------------------------------------------------------------------------
# LLM 配置（默认通义千问 qwen-plus，OpenAI 兼容协议）
# ---------------------------------------------------------------------------
LLM_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1"
LLM_API_KEY = os.getenv("XIAOZHI_API_KEY", "").strip()
LLM_MODEL = os.getenv("XIAOZHI_MODEL", "qwen-plus").strip()

SYSTEM_PROMPT = (
    "你是语音助手'小智'，语气活泼友好。回答要口语化、简短，适合语音播报，"
    "不要使用 Markdown 符号、列表编号或换行。"
)

_llm = None


def get_llm():
    """延迟初始化 OpenAI 客户端，避免启动时强依赖 key。"""
    global _llm
    if _llm is None:
        from openai import OpenAI
        _llm = OpenAI(base_url=LLM_BASE_URL, api_key=LLM_API_KEY)
    return _llm


# ---------------------------------------------------------------------------
# 语音识别（faster-whisper，模型延迟加载）
# ---------------------------------------------------------------------------
_whisper = None


def get_whisper():
    global _whisper
    if _whisper is None:
        from faster_whisper import WhisperModel
        # base 模型约 74MB，首次运行自动从 HuggingFace 下载
        _whisper = WhisperModel("base", device="cpu", compute_type="int8")
    return _whisper


app = FastAPI(title="小智语音助手", version="1.0.0")

# 允许前端（Vite 80 端口）跨域直连
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)


# ---------------------------------------------------------------------------
# 健康检查
# ---------------------------------------------------------------------------
@app.get("/api/health")
def health():
    return {
        "status": "ok",
        "llm_configured": bool(LLM_API_KEY),
        "llm_model": LLM_MODEL,
        "llm_provider": "dashscope",
    }


# ---------------------------------------------------------------------------
# 大模型流式对话（SSE）
# ---------------------------------------------------------------------------
class ChatRequest(BaseModel):
    message: str
    history: list[dict] = []


def _sse_stream(req: ChatRequest):
    messages = [{"role": "system", "content": SYSTEM_PROMPT}]
    for h in req.history[-20:]:
        role = h.get("role")
        content = h.get("content")
        if role in ("user", "assistant") and content:
            messages.append({"role": role, "content": content})
    messages.append({"role": "user", "content": req.message})

    try:
        stream = get_llm().chat.completions.create(
            model=LLM_MODEL, messages=messages, stream=True, temperature=0.7
        )
        for chunk in stream:
            delta = chunk.choices[0].delta.content if chunk.choices else None
            if delta:
                yield f"data: {json.dumps({'content': delta}, ensure_ascii=False)}\n\n"
        yield "data: [DONE]\n\n"
    except Exception as e:  # noqa: BLE001
        yield f"data: {json.dumps({'error': str(e)}, ensure_ascii=False)}\n\n"


@app.post("/api/chat")
def chat(req: ChatRequest):
    return StreamingResponse(_sse_stream(req), media_type="text/event-stream")


# ---------------------------------------------------------------------------
# 语音识别（ASR）：前端录音 → 文字
# ---------------------------------------------------------------------------
@app.post("/api/asr")
async def asr(file: UploadFile = File(...)):
    data = await file.read()
    if not data:
        return JSONResponse({"error": "空音频"}, status_code=400)
    try:
        segments, _info = get_whisper().transcribe(
            io.BytesIO(data), language="zh", vad_filter=True
        )
        text = "".join(s.text for s in segments).strip()
        return {"text": text}
    except Exception as e:  # noqa: BLE001
        return JSONResponse({"error": f"识别失败: {e}"}, status_code=500)


# ---------------------------------------------------------------------------
# 语音合成（TTS）：文字 → mp3
# ---------------------------------------------------------------------------
@app.get("/api/tts")
async def tts(text: str):
    text = (text or "").strip()
    if not text:
        return JSONResponse({"error": "内容为空"}, status_code=400)
    try:
        import edge_tts
        out = os.path.join(tempfile.gettempdir(), f"xiaozhi_{uuid.uuid4().hex}.mp3")
        await edge_tts.Communicate(text[:2000], "zh-CN-XiaoxiaoNeural").save(out)
        return FileResponse(out, media_type="audio/mpeg", filename="xiaozhi.mp3")
    except Exception as e:  # noqa: BLE001
        return JSONResponse({"error": f"合成失败: {e}"}, status_code=500)