<template>
  <div class="home">
    <!-- ===== 欢迎头 ===== -->
    <div class="hero">
      <div class="hero-glow"></div>
      <h2 class="hero-greeting">{{ greeting }}，{{ userName }}</h2>
      <p class="hero-sub">{{ today }}，愿您健康常在</p>
    </div>

    <!-- ===== 统计卡片 ===== -->
    <div class="stats-grid">
      <div v-for="s in stats" :key="s.label" class="stat-card" :class="s.theme">
        <div class="stat-icon">
          <el-icon :size="22"><component :is="s.icon" /></el-icon>
        </div>
        <div class="stat-body">
          <p class="stat-value">{{ s.value }}</p>
          <p class="stat-label">{{ s.label }}</p>
        </div>
      </div>
    </div>

    <!-- ===== 快捷入口 ===== -->
    <div class="quick-card">
      <h3 class="quick-title">快捷入口</h3>
      <div class="quick-grid">
        <router-link v-for="q in quickLinks" :key="q.title" :to="q.path" class="quick-item">
          <div class="quick-item-icon" :class="q.theme">
            <el-icon :size="20"><component :is="q.icon" /></el-icon>
          </div>
          <div class="quick-item-text">
            <p class="quick-item-title">{{ q.title }}</p>
            <p class="quick-item-desc">{{ q.desc }}</p>
          </div>
        </router-link>
      </div>
    </div>

    <p class="home-footer">智康体检预约系统 · 让健康管理更简单</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { User, Calendar, FirstAidKit, ChatDotRound, Box } from '@element-plus/icons-vue'
import useUserStore from '@/store/modules/user'
import { getStatistics } from '@/api/member/statistics'
import { listSetmeal } from '@/api/reservation/setmeal'
import { listCheckitem } from '@/api/reservation/checkitem'
import { listAppointment } from '@/api/reservation/appointment'

const userStore = useUserStore()
const userName = computed(() => userStore.nickName || userStore.name || '管理员')

const stats = ref([
  { label: '会员总数', value: 0, icon: User, theme: 't1' },
  { label: '体检记录', value: 0, icon: FirstAidKit, theme: 't2' },
  { label: '可选套餐', value: 0, icon: Box, theme: 't3' },
  { label: '预约订单', value: 0, icon: Calendar, theme: 't4' }
])

const quickLinks = [
  { title: '预约记录', desc: '管理会员预约订单', path: '/reservation/appointment', icon: Calendar, theme: 'q1' },
  { title: '会员管理', desc: '维护会员档案信息', path: '/member/member', icon: User, theme: 'q2' },
  { title: '套餐管理', desc: '配置体检套餐项目', path: '/reservation/setmeal', icon: Box, theme: 'q3' },
  { title: 'AI 助手', desc: '智能对话与语音助手', path: '/ai/chat', icon: ChatDotRound, theme: 'q4' }
]

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 6) return '夜深了'
  if (h < 12) return '早上好'
  if (h < 14) return '中午好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const today = computed(() => {
  const d = new Date()
  const week = ['日', '一', '二', '三', '四', '五', '六']
  return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日 星期${week[d.getDay()]}`
})

async function loadStats() {
  try {
    const res = await getStatistics()
    if (res.code === 200 && res.data) {
      stats.value[0].value = res.data.totalMembers || 0
      stats.value[1].value = res.data.totalRecords || 0
    }
  } catch { /* 静默 */ }
  try {
    const s = await listSetmeal({ pageNum: 1, pageSize: 1 })
    if (s.code === 200) stats.value[2].value = s.total || 0
  } catch { /* 静默 */ }
  try {
    const a = await listAppointment({ pageNum: 1, pageSize: 1 })
    if (a.code === 200) stats.value[3].value = a.total || 0
  } catch { /* 静默 */ }
}

onMounted(loadStats)
</script>

<style scoped>
.home {
  padding: 18px 22px 24px;
  max-width: 1100px;
  margin: 0 auto;
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Hiragino Sans GB',
               'Microsoft YaHei', sans-serif;
}

/* ===== 欢迎头 ===== */
.hero {
  position: relative;
  overflow: hidden;
  padding: 34px 36px;
  border-radius: 20px;
  background: linear-gradient(120deg, #52b6a4 0%, #6fc8b8 55%, #8ad6c8 100%);
  color: #fff;
  box-shadow: 0 10px 30px rgba(82, 182, 164, 0.25);
  margin-bottom: 20px;
}
.hero-glow {
  position: absolute;
  top: -60px;
  right: -40px;
  width: 240px;
  height: 240px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.25) 0%, transparent 70%);
  pointer-events: none;
}
.hero-greeting {
  margin: 0 0 6px;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 0.5px;
}
.hero-sub {
  margin: 0;
  font-size: 13px;
  opacity: 0.9;
  letter-spacing: 1px;
}

/* ===== 统计卡片 ===== */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}
.stat-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 20px 22px;
  background: #fff;
  border: 1px solid #edf3f0;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition: transform 0.2s, box-shadow 0.2s;
}
.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(82, 182, 164, 0.12);
}
.stat-icon {
  width: 46px;
  height: 46px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: #fff;
}
.t1 .stat-icon { background: linear-gradient(135deg, #52b6a4, #7ed3c4); }
.t2 .stat-icon { background: linear-gradient(135deg, #6fb8d6, #9ad0e6); }
.t3 .stat-icon { background: linear-gradient(135deg, #a3c46f, #c2da96); }
.t4 .stat-icon { background: linear-gradient(135deg, #d69a6f, #e6be9a); }
.stat-value {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #2c4a42;
  line-height: 1.2;
}
.stat-label {
  margin: 2px 0 0;
  font-size: 12px;
  color: #8aa89f;
}

/* ===== 快捷入口 ===== */
.quick-card {
  background: #fff;
  border: 1px solid #edf3f0;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 20px 24px;
}
.quick-title {
  margin: 0 0 16px;
  font-size: 15px;
  font-weight: 600;
  color: #2c4a42;
}
.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}
.quick-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 18px;
  border-radius: 14px;
  background: #f7fbf9;
  border: 1px solid #eef5f2;
  text-decoration: none;
  transition: all 0.2s;
}
.quick-item:hover {
  background: #eef8f5;
  border-color: #cde8e2;
  transform: translateY(-2px);
}
.quick-item-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.q1 .quick-item-icon { background: linear-gradient(135deg, #52b6a4, #7ed3c4); }
.q2 .quick-item-icon { background: linear-gradient(135deg, #6fb8d6, #9ad0e6); }
.q3 .quick-item-icon { background: linear-gradient(135deg, #a3c46f, #c2da96); }
.q4 .quick-item-icon { background: linear-gradient(135deg, #d69a6f, #e6be9a); }
.quick-item-title {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #2c4a42;
}
.quick-item-desc {
  margin: 3px 0 0;
  font-size: 12px;
  color: #8aa89f;
}

.home-footer {
  margin: 26px 0 0;
  text-align: center;
  font-size: 12px;
  color: #a9c4bc;
  letter-spacing: 1px;
}

@media (max-width: 992px) {
  .stats-grid, .quick-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>