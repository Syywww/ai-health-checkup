<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="性别" align="center" prop="gender" :formatter="genderFormat" />
      <el-table-column label="出生日期" align="center" prop="birthday" width="120" />
      <el-table-column label="手机号" align="center" prop="phone" />
      <el-table-column label="身份证" align="center" prop="idCard" />
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="memberFormRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择性别">
                <el-option label="男" value="0" />
                <el-option label="女" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker v-model="form.birthday" type="date" placeholder="请选择日期" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="住址" prop="address">
              <el-input v-model="form.address" placeholder="请输入住址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listMember, getMember, addMember, updateMember, delMember } from '@/api/member/member'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(true)
const multiple = ref(true)
const total = ref(0)
const memberList = ref([])
const open = ref(false)
const title = ref('')
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  phone: ''
})
const form = ref({
  id: null,
  name: '',
  gender: '0',
  birthday: '',
  phone: '',
  idCard: '',
  address: '',
  status: '0',
  remark: ''
})
const rules = {
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  phone: [{ required: true, message: '手机号不能为空', trigger: 'blur' }]
}

/** 查询会员列表 */
function getList() {
  loading.value = true
  listMember(queryParams).then(res => {
    memberList.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.name = ''
  queryParams.phone = ''
  handleQuery()
}

/** 性别字典翻译 */
function genderFormat(row) {
  return row.gender === '0' ? '男' : '女'
}

/** 状态字典翻译 */
function statusFormat(row) {
  return row.status === '0' ? '正常' : '停用'
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  resetForm()
  open.value = true
  title.value = '新增会员'
}

/** 修改按钮操作 */
function handleUpdate(row) {
  resetForm()
  const id = row.id || row.id
  getMember(id).then(res => {
    form.value = res.data
    open.value = true
    title.value = '修改会员'
  })
}

/** 重置表单 */
function resetForm() {
  form.value = {
    id: null,
    name: '',
    gender: '0',
    birthday: '',
    phone: '',
    idCard: '',
    address: '',
    status: '0',
    remark: ''
  }
}

/** 提交表单 */
function submitForm() {
  // 验证表单
  // 简化，直接提交
  if (form.value.id) {
    updateMember(form.value).then(() => {
      ElMessage.success('修改成功')
      open.value = false
      getList()
    })
  } else {
    addMember(form.value).then(() => {
      ElMessage.success('新增成功')
      open.value = false
      getList()
    })
  }
}

/** 取消按钮 */
function cancel() {
  open.value = false
}

/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id || row.id
  ElMessageBox.confirm('是否确认删除该会员？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    delMember(ids).then(() => {
      ElMessage.success('删除成功')
      getList()
    })
  }).catch(() => {})
}

onMounted(() => {
  getList()
})
</script>