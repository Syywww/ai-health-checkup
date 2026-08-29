<template>
  <div class="xiaozhi-wrapper">
    <!-- ========== 头部 ========== -->
    <header class="xz-header">
      <div class="xz-avatar">智</div>
      <div class="xz-title-group">
        <h2 class="xz-title">小智语音助手</h2>
        <p class="xz-subtitle">
          <span class="status-dot" :class="{ on: llmReady }"></span>
          {{ llmReady ? '服务在线' : '服务未连接' }}
          <span v-if="llmReady" class="llm-model">· {{ llmModel }}</span>
        </p>
      </div>
      <div class="xz-header-actions">
        <el-switch
          v-model="voiceOn"
          active-text="语音播报"
          inline-prompt
          style="--el-switch-on-color: #409eff;"
        />
        <el-button
          text
          class="btn-clear"
          :disabled="isStreaming || messages.length === 0"
          @click="handleClear"
        >
          <el-icon><Delete /></el-icon>
          清空对话
        </el-button>
      </div>
    </header>

    <!-- ========== 对话区 ========== -->
    <main class="xz-main" ref="mainRef">
      <div v-if="messages.length === 0" class="xz-empty">
        <div class="xz-empty-avatar">智</div>
        <p class="xz-empty-title">你好，我是小智</p>
        <p class="xz-empty-sub">点击下方话筒说话，或直接打字提问</p>
        <div class="xz-empty-tips">
          <div class="tip-item" @click="inputText = '今天天气怎么样？'">
            <el-icon><Sunny /></el-icon>
            <span>问天气</span>
          </div>
          <div class="tip-item" @click="inputText = '讲个笑话吧'">
            <el-icon><PartlyCloudy /></el-icon>
            <span>讲笑话</span>
          </div>
          <div class="tip-item" @click="inputText = '你是谁'">
            <el-icon><MagicStick /></el-icon>
            <span>认识我</span>
          </div>
        </div>
      </div>

      <TransitionGroup v-else name="msg-slide" tag="div" class="xz-messages">
        <div
          v-for="(msg, index) in messages"
          :key="index"
          class="xz-row"
          :class="msg.role"
        >
          <div v-if="msg.role === 'assistant'" class="xz-av ai-av">智</div>

          <div
            class="xz-bubble"
            :class="[`${msg.role}-bubble`, { 'has-error': msg.error }]"
          >
            <!-- AI：loading 三点 -->
            <div v-if="msg.loading" class="loading-dots">
              <span></span><span></span><span></span>
            </div>
            <!-- AI：错误 -->
            <div v-else-if="msg.error" class="error-msg">
              <el-icon><Warning /></el-icon>
              {{ msg.error }}
            </div>
            <!-- 正常内容 -->
            <div
              v-else
              class="xz-text"
              :class="{ 'typing-cursor': msg.streaming }"
            >{{ msg.content || ' ' }}</div>
          </div>

          <div v-if="msg.role === 'user'" class="xz-av user-av">我</div>
        </div>
      </TransitionGroup>
    </main>

    <!-- ========== 输入区 ========== -->
    <footer class="xz-footer">
      <div class="xz-input-box">
        <!-- 录音按钮 -->
        <el-button
          class="btn-voice"
          :class="{ recording: recording }"
          :disabled="isStreaming"
          :title="recording ? '点击结束录音' : '按住说话 / 点击开始录音'"
          @click="recording ? stopRecording() : startRecording()"
        >
          <el-icon class="voice-icon">
            <Microphone v-if="!recording" />
            <Loading v-else class="is-loading" />
          </el-icon>
        </el-button>

        <!-- 录音中红点提示 -->
        <span v-if="recording" class="recording-tip">
          <i class="rec-dot"></i> 正在聆听…
        </span>

        <!-- 文字输入 -->
        <el-input
          v-model="inputText"
          type="textarea"
          :autosize="{ minRows: 1, maxRows: 4 }"
          placeholder="打字提问，Enter 发送"
          resize="none"
          :disabled="isStreaming"
          @keydown="handleKeyDown"
        />
        <el-button
          class="btn-send"
          :disabled="isStreaming || !inputText.trim()"
          @click="handleSend()"
        >
          <el-icon><Promotion /></el-icon>
        </el-button>
      </div>
      <p class="xz-hint">
        {{ recording ? '说出你的问题，松开发送' : '支持语音与文字，AI 回答可语音播报' }}
      </p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Delete, Warning, Microphone, Loading, Promotion,
  Sunny, PartlyCloudy, MagicStick
} from '@element-plus/icons-vue'

// ──────────────────────────────────────────
// 配置：Python 语音服务（FastAPI，见 xiaozhi/ 目录）
// ──────────────────────────────────────────
const XIAOZHI_BASE = 'http://localhost:8000'

// ──────────────────────────────────────────
// 状态
// ──────────────────────────────────────────
const messages     = ref([])
const inputText    = ref('')
const isStreaming  = ref(false)
const voiceOn      = ref(true)
const recording    = ref(false)
const llmReady     = ref(false)
const llmModel     = ref('')
const mainRef      = ref(null)

// 录音相关
let mediaRecorder   = null
let recorderStream  = null
let audioChunks     = []
let currentReader   = null
let audioPlayer     = null   // TTS 播放器

const history = () => messages.value
  .filter(m => !m.loading && !m.streaming && !m.error && m.content)
  .slice(-20)
  .map(m => ({ role: m.role, content: m.content }))

// ──────────────────────────────────────────
// 生命周期
// ──────────────────────────────────────────
onMounted(() => {
  checkHealth()
})
onBeforeUnmount(() => {
  stopRecording(true)
  abortStream()
  audioPlayer?.pause()
})

async function checkHealth() {
  try {
    const res = await fetch(`${XIAOZHI_BASE}/api/health`)
    const data = await res.json()
    llmReady.value = !!data.llm_configured
    llmModel.value = data.llm_model || ''
  } catch {
    llmReady.value = false
  }
}

// ──────────────────────────────────────────
// 文字发送（复用：语音识别后也会走到这里）
// ──────────────────────────────────────────
function handleKeyDown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    handleSend()
  }
}

async function handleSend(text) {
  const content = (text ?? inputText.value).trim()
  if (!content || isStreaming.value) return

  if (text === undefined) inputText.value = ''
  isStreaming.value = true
  scrollToBottom()

  // 用户气泡
  messages.value.push({ role: 'user', content, loading: false, streaming: false, error: null })
  // AI 占位
  const aiIndex = messages.value.length
  messages.value.push({ role: 'assistant', content: '', loading: true, streaming: false, error: null })
  scrollToBottom()

  try {
    const response = await fetch(`${XIAOZHI_BASE}/api/chat`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ message: content, history: history() })
    })
    if (!response.ok) throw new Error(`HTTP ${response.status}`)

    const reader = response.body.getReader()
    currentReader = reader
    const decoder = new TextDecoder('utf-8')
    let buffer = ''

    messages.value[aiIndex] = { ...messages.value[aiIndex], loading: false, streaming: true }

    while (true) {
      const { done, value } = await reader.read()
      if (done) break
      buffer += decoder.decode(value, { stream: true })
      const lines = buffer.split('\n')
      buffer = lines.pop()
      for (const line of lines) {
        const trimmed = line.trim()
        if (!trimmed.startsWith('data: ')) continue
        const payload = trimmed.slice(6)
        if (payload === '[DONE]') continue
        try {
          const obj = JSON.parse(payload)
          if (obj.error) throw new Error(obj.error)
          if (obj.content) {
            const cur = messages.value[aiIndex]
            messages.value[aiIndex] = { ...cur, content: cur.content + obj.content }
            scrollToBottom()
          }
        } catch (err) {
          if (err instanceof SyntaxError) continue
          throw err
        }
      }
    }

    messages.value[aiIndex] = { ...messages.value[aiIndex], streaming: false }
    // 语音播报
    const answer = messages.value[aiIndex].content
    if (voiceOn.value && answer) await speak(answer)

  } catch (e) {
    if (e.name === 'AbortError') return
    const errMsg = e.message || '服务异常，请重试'
    messages.value[aiIndex] = {
      role: 'assistant', content: '', loading: false, streaming: false, error: errMsg
    }
    ElMessage.error('AI 响应失败：' + errMsg)
  } finally {
    isStreaming.value = false
    currentReader = null
  }
}

function abortStream() {
  currentReader?.cancel?.()
  currentReader = null
  isStreaming.value = false
}

// ──────────────────────────────────────────
// 语音播报（TTS）
// ──────────────────────────────────────────
async function speak(text) {
  try {
    const url = `${XIAOZHI_BASE}/api/tts?text=${encodeURIComponent(text)}`
    audioPlayer?.pause()
    audioPlayer = new Audio(url)
    await audioPlayer.play()
  } catch {
    /* 浏览器自动播放限制：忽略，文字已展示 */
  }
}

// ──────────────────────────────────────────
// 录音 → ASR
// ──────────────────────────────────────────
async function startRecording() {
  if (recording.value) return
  try {
    recorderStream = await navigator.mediaDevices.getUserMedia({ audio: true })
  } catch {
    ElMessage.warning('无法访问麦克风，请检查浏览器权限')
    return
  }

  // 优先 webm/opus（Chrome/Edge 支持，whisper 可解码）
  const mime = MediaRecorder.isTypeSupported('audio/webm;codecs=opus')
    ? 'audio/webm;codecs=opus'
    : ''
  mediaRecorder = new MediaRecorder(recorderStream, mime ? { mimeType: mime } : undefined)
  audioChunks = []

  mediaRecorder.ondataavailable = (e) => {
    if (e.data.size > 0) audioChunks.push(e.data)
  }
  mediaRecorder.onstop = async () => {
    recorderStream?.getTracks().forEach((t) => t.stop())
    recorderStream = null
    const type = mediaRecorder?.mimeType || 'audio/webm'
    const blob = new Blob(audioChunks, { type })
    audioChunks = []
    mediaRecorder = null
    if (blob.size > 0) await recognize(blob)
  }

  mediaRecorder.start()
  recording.value = true
}

function stopRecording(force = false) {
  if (!mediaRecorder) return
  try {
    if (mediaRecorder.state !== 'inactive') mediaRecorder.stop()
  } catch { /* noop */ }
  if (force && recorderStream) {
    recorderStream.getTracks().forEach((t) => t.stop())
    recorderStream = null
  }
  recording.value = false
}

async function recognize(blob) {
  if (isStreaming.value) return
  ElMessage.info('识别中…')
  try {
    const fd = new FormData()
    fd.append('file', blob, 'record.webm')
    const res = await fetch(`${XIAOZHI_BASE}/api/asr`, { method: 'POST', body: fd })
    const data = await res.json()
    if (data.error) throw new Error(data.error)
    const text = (data.text || '').trim()
    if (!text) {
      ElMessage.warning('没有听清，请再说一次')
      return
    }
    await handleSend(text)
  } catch (e) {
    ElMessage.error('语音识别失败：' + (e.message || e))
  }
}

// ──────────────────────────────────────────
// 工具
// ──────────────────────────────────────────
function handleClear() {
  abortStream()
  audioPlayer?.pause()
  messages.value = []
  inputText.value = ''
}

function scrollToBottom() {
  nextTick(() => {
    const el = mainRef.value
    if (el) el.scrollTop = el.scrollHeight
  })
}
</script>

<style scoped>
.xiaozhi-wrapper {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 84px);
  background: #f5f7fa;
  overflow: hidden;
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Hiragino Sans GB',
               'Microsoft YaHei', sans-serif;
}

/* ===== 头部 ===== */
.xz-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 24px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
  flex-shrink: 0;
}
.xz-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #6c3be4 0%, #409eff 100%);
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.xz-title-group { flex: 1; min-width: 0; }
.xz-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a2e;
  margin: 0;
}
.xz-subtitle {
  margin: 2px 0 0;
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 6px;
}
.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #c0c4cc;
  display: inline-block;
}
.status-dot.on { background: #67c23a; box-shadow: 0 0 0 3px rgba(103, 194, 58, 0.15); }
.llm-model { color: #409eff; }
.xz-header-actions { display: flex; align-items: center; gap: 14px; }
.btn-clear { color: #909399; }

/* ===== 对话区 ===== */
.xz-main {
  flex: 1;
  overflow-y: auto;
  padding: 20px 24px 10px;
  min-height: 0;
}
.xz-messages { display: flex; flex-direction: column; gap: 2px; }

/* 空状态 */
.xz-empty {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
}
.xz-empty-avatar {
  width: 68px;
  height: 68px;
  border-radius: 50%;
  background: linear-gradient(135deg, #6c3be4 0%, #409eff 100%);
  color: #fff;
  font-size: 30px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 32px rgba(64, 158, 255, 0.25);
  margin-bottom: 6px;
}
.xz-empty-title { font-size: 22px; font-weight: 600; color: #1a1a2e; margin: 0; }
.xz-empty-sub { font-size: 13px; color: #909399; margin: 0; }
.xz-empty-tips { display: flex; gap: 10px; margin-top: 12px; }
.tip-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 10px;
  font-size: 13px;
  color: #606266;
  cursor: pointer;
  transition: all 0.2s;
}
.tip-item:hover { border-color: #409eff; color: #409eff; }

/* 消息行 */
.xz-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  max-width: 860px;
  width: 100%;
  margin: 0 auto;
  box-sizing: border-box;
}
.xz-row.user { justify-content: flex-end; }
.xz-av {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
}
.user-av { background: #409eff; color: #fff; font-size: 12px; }
.ai-av { background: linear-gradient(135deg, #6c3be4 0%, #409eff 100%); color: #fff; }

.xz-bubble {
  max-width: 70%;
  padding: 11px 16px;
  border-radius: 14px;
  font-size: 14px;
  line-height: 1.75;
  word-break: break-word;
}
.user-bubble { background: #409eff; color: #fff; border-bottom-right-radius: 4px; }
.assistant-bubble {
  background: #fff;
  color: #303133;
  border-bottom-left-radius: 4px;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.06);
}
.has-error { background: #fff5f5; border: 1px solid #fde2e2; }
.xz-text { white-space: pre-wrap; }

.typing-cursor::after {
  content: '▋';
  font-size: 12px;
  animation: blink 0.8s step-start infinite;
  margin-left: 2px;
  color: #409eff;
}
@keyframes blink { 50% { opacity: 0; } }

.loading-dots { display: flex; align-items: center; gap: 5px; padding: 2px 0; }
.loading-dots span {
  display: inline-block;
  width: 7px;
  height: 7px;
  background: #c0c4cc;
  border-radius: 50%;
  animation: dotBounce 1.2s infinite ease-in-out;
}
.loading-dots span:nth-child(2) { animation-delay: 0.18s; }
.loading-dots span:nth-child(3) { animation-delay: 0.36s; }
@keyframes dotBounce {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-7px); }
}

.error-msg { display: flex; align-items: center; gap: 6px; color: #f56c6c; font-size: 13px; }

/* 消息过渡 */
.msg-slide-enter-active { transition: all 0.22s ease; }
.msg-slide-enter-from { opacity: 0; transform: translateY(10px); }

/* ===== 输入区 ===== */
.xz-footer { padding: 10px 24px 14px; background: #f5f7fa; flex-shrink: 0; }
.xz-input-box {
  max-width: 860px;
  margin: 0 auto;
  background: #fff;
  border: 1.5px solid #dcdfe6;
  border-radius: 14px;
  padding: 8px 10px 8px 12px;
  display: flex;
  align-items: flex-end;
  gap: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  box-sizing: border-box;
}
.xz-input-box :deep(.el-textarea__inner) {
  border: none !important;
  box-shadow: none !important;
  background: transparent !important;
  padding: 5px 0 !important;
  font-size: 14px !important;
  line-height: 1.65 !important;
  color: #303133 !important;
  font-family: inherit !important;
  resize: none !important;
}

/* 录音按钮 */
.btn-voice {
  width: 38px !important;
  height: 38px !important;
  min-width: 38px !important;
  padding: 0 !important;
  border-radius: 50% !important;
  background: #f0f2f5 !important;
  border: 1px solid #dcdfe6 !important;
  color: #606266 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  flex-shrink: 0 !important;
  margin-bottom: 1px;
  transition: all 0.2s !important;
}
.btn-voice:hover:not(:disabled) { border-color: #409eff !important; color: #409eff !important; }
.btn-voice.recording {
  background: #f56c6c !important;
  border-color: #f56c6c !important;
  color: #fff !important;
  animation: pulse 1.2s infinite;
}
@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(245, 108, 108, 0.4); }
  70% { box-shadow: 0 0 0 10px rgba(245, 108, 108, 0); }
  100% { box-shadow: 0 0 0 0 rgba(245, 108, 108, 0); }
}

.recording-tip {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #f56c6c;
  white-space: nowrap;
}
.rec-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #f56c6c;
  animation: blink 1s step-start infinite;
}

.btn-send {
  width: 36px !important;
  height: 36px !important;
  min-width: 36px !important;
  padding: 0 !important;
  border-radius: 50% !important;
  background: #409eff !important;
  border-color: #409eff !important;
  color: #fff !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  flex-shrink: 0 !important;
  margin-bottom: 1px;
}
.btn-send:disabled { background: #c0c4cc !important; border-color: #c0c4cc !important; }

.xz-hint {
  max-width: 860px;
  margin: 5px auto 0;
  font-size: 11px;
  color: #c0c4cc;
  text-align: center;
}

/* 滚动条 */
.xz-main::-webkit-scrollbar { width: 4px; }
.xz-main::-webkit-scrollbar-thumb { background: #e4e7ed; border-radius: 4px; }
</style>