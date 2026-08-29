# 小智语音助手（Python）

基于 **FastAPI + faster-whisper + 通义千问 + edge-tts** 的语音对话助手后端，为前端"小智"页面提供语音识别、大模型流式对话、语音合成三个能力。

## 技术栈

| 能力 | 技术 | 说明 |
| --- | --- | --- |
| 语音识别 ASR | faster-whisper（base 模型） | 本地运行，无需外部服务；首次运行自动下载模型（约 74MB） |
| 大模型 LLM | 通义千问 qwen-plus（OpenAI 兼容协议） | API Key 在 `.env` 配置，与项目 Java AI 共用同一个 key |
| 语音合成 TTS | edge-tts（微软语音 zh-CN-XiaoxiaoNeural） | 免费、音质好，返回 mp3 |
| Web 服务 | FastAPI + uvicorn | SSE 流式输出、CORS 开放供前端跨域调用 |

## 启动

```bash
cd xiaozhi
pip install -r requirements.txt
# 首次复制 .env.example 为 .env 并填写 XIAOZHI_API_KEY
uvicorn app:app --host 0.0.0.0 --port 8000
```

服务运行在 `http://localhost:8000`，健康检查 `GET /api/health`。

## 接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | /api/health | 健康检查，返回 LLM 配置状态 |
| POST | /api/chat | 大模型流式对话，`{"message":"...","history":[]}`，SSE 返回 |
| POST | /api/asr | 语音识别，`multipart/form-data` 上传音频（webm/opus），返回 `{"text":"..."}` |
| GET | /api/tts | 语音合成，`?text=...`，返回 mp3 音频 |

## 目录

```
xiaozhi/
├── app.py             # FastAPI 主服务
├── requirements.txt   # 依赖清单
├── .env               # 本地配置（API Key，已被 gitignore）
└── .env.example       # 配置模板
```