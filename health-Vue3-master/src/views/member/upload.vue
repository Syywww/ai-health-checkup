<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>体检记录录入</span>
        </div>
      </template>
      <el-form :model="form" ref="uploadFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="会员" prop="memberId" required>
              <el-select v-model="form.memberId" placeholder="请选择会员" filterable>
                <el-option v-for="item in memberOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体检套餐" prop="setmealId">
              <el-select v-model="form.setmealId" placeholder="请选择套餐" filterable @change="handleSetmealChange">
                <el-option v-for="item in setmealOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体检日期" prop="examinationDate">
              <el-date-picker v-model="form.examinationDate" type="date" placeholder="请选择日期" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体检医生" prop="doctor">
              <el-input v-model="form.doctor" placeholder="请输入医生姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="体检报告" prop="attachment">
              <el-upload
                  action="/common/upload"
                  :on-success="handleUploadSuccess"
                  :on-remove="handleUploadRemove"
                  :limit="1"
                  :file-list="fileList"
              >
                <el-button type="primary">上传报告</el-button>
                <template #tip>
                  <div class="el-upload__tip">支持pdf/jpg/png格式，单个文件不超过10MB</div>
                </template>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="总结" prop="resultSummary">
              <el-input v-model="form.resultSummary" type="textarea" placeholder="请输入体检总结" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 检查明细列表 -->
    <el-card style="margin-top:20px">
      <template #header>
        <span>检查明细</span>
      </template>
      <el-button type="primary" plain @click="addDetail">添加检查项</el-button>
      <el-table :data="form.details" border style="margin-top:10px">
        <el-table-column label="检查项" prop="checkitemName" width="200">
          <template #default="{ row }">
            <el-select v-model="row.checkitemId" placeholder="请选择" filterable @change="selectCheckitem(row)">
              <el-option v-for="item in checkitemOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="结果值" prop="resultValue">
          <el-input v-model="row.resultValue" placeholder="请输入结果" />
        </el-table-column>
        <el-table-column label="单位" prop="unit">
          <el-input v-model="row.unit" placeholder="单位" />
        </el-table-column>
        <el-table-column label="参考范围" prop="referenceRange">
          <el-input v-model="row.referenceRange" placeholder="参考范围" />
        </el-table-column>
        <el-table-column label="状态" prop="status">
          <el-select v-model="row.status" placeholder="请选择">
            <el-option label="正常" value="0" />
            <el-option label="异常" value="1" />
          </el-select>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <el-button link type="danger" @click="removeDetail(row)">删除</el-button>
        </el-table-column>
      </el-table>
    </el-card>

    <div style="margin-top:20px; text-align:center">
      <el-button type="primary" @click="submitForm">保存体检记录</el-button>
      <el-button @click="resetForm">重置</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listMember } from '@/api/member/member'
import { listSetmeal } from '@/api/reservation/setmeal' // 假设已有套餐列表接口
import { listCheckitem } from '@/api/reservation/checkitem' // 检查项接口
import { addExamination } from '@/api/member/examination'
import { ElMessage } from 'element-plus'

const memberOptions = ref([])
const setmealOptions = ref([])
const checkitemOptions = ref([])
const fileList = ref([])
const form = reactive({
  memberId: '',
  setmealId: '',
  examinationDate: '',
  doctor: '',
  resultSummary: '',
  attachment: '',
  details: []
})

function loadMemberOptions() {
  listMember({ pageNum: 1, pageSize: 100 }).then(res => {
    memberOptions.value = res.rows
  })
}
function loadSetmealOptions() {
  listSetmeal({ pageNum: 1, pageSize: 100 }).then(res => {
    setmealOptions.value = res.rows
  })
}
function loadCheckitemOptions() {
  listCheckitem({ pageNum: 1, pageSize: 100 }).then(res => {
    checkitemOptions.value = res.rows
  })
}

function handleSetmealChange(val) {
  // 可选：根据套餐自动加载对应的检查项，但为简化，由用户手动添加
}

function selectCheckitem(row) {
  const selected = checkitemOptions.value.find(item => item.id === row.checkitemId)
  if (selected) {
    row.checkitemName = selected.name
    // 可自动填充单位、参考范围等
  }
}

function addDetail() {
  form.details.push({
    checkitemId: '',
    checkitemName: '',
    resultValue: '',
    unit: '',
    referenceRange: '',
    status: '0'
  })
}

function removeDetail(row) {
  const index = form.details.indexOf(row)
  if (index > -1) form.details.splice(index, 1)
}

function handleUploadSuccess(response, file) {
  form.attachment = response.data.fileName
  fileList.value = [{ name: file.name, url: response.data.filePath }]
}
function handleUploadRemove(file) {
  form.attachment = ''
  fileList.value = []
}

function submitForm() {
  const params = { ...form }
  addExamination(params).then(() => {
    ElMessage.success('保存成功')
    resetForm()
  })
}

function resetForm() {
  form.memberId = ''
  form.setmealId = ''
  form.examinationDate = ''
  form.doctor = ''
  form.resultSummary = ''
  form.attachment = ''
  form.details = []
  fileList.value = []
}

onMounted(() => {
  loadMemberOptions()
  loadSetmealOptions()
  loadCheckitemOptions()
})
</script>