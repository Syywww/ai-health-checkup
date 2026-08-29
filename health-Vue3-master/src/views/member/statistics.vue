<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-title">会员总数</div>
            <div class="stat-number">{{ totalMembers }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-title">体检总次数</div>
            <div class="stat-number">{{ totalRecords }}</div>
          </div>
        </el-card>
      </el-col>
      <!-- 可扩展更多卡片 -->
    </el-row>
    <el-card style="margin-top:20px">
      <template #header>
        <span>统计筛选</span>
      </template>
      <el-form :inline="true">
        <el-form-item label="开始日期">
          <el-date-picker v-model="query.startDate" type="date" placeholder="开始" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="query.endDate" type="date" placeholder="结束" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadStatistics">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStatistics } from '@/api/member/statistics'

const totalMembers = ref(0)
const totalRecords = ref(0)
const query = ref({
  startDate: '',
  endDate: ''
})

function loadStatistics() {
  getStatistics(query.value).then(res => {
    totalMembers.value = res.data.totalMembers
    totalRecords.value = res.data.totalRecords
  })
}

function resetQuery() {
  query.value.startDate = ''
  query.value.endDate = ''
  loadStatistics()
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.stat-item {
  text-align: center;
}
.stat-title {
  font-size: 14px;
  color: #666;
}
.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
  margin-top: 8px;
}
</style>