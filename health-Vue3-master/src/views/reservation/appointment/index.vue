<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会员" prop="memberId">
        <el-select
          v-model="queryParams.memberId"
          placeholder="搜索会员姓名"
          clearable
          filterable
          remote
          :remote-method="searchMember"
          :loading="memberLoading"
          style="width: 180px"
          @keyup.enter="handleQuery"
        >
          <el-option v-for="item in memberOptions" :key="item.id" :label="item.name + '（' + item.phone + '）'" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="套餐" prop="setmealId">
        <el-select
          v-model="queryParams.setmealId"
          placeholder="请选择体检套餐"
          clearable
          filterable
          style="width: 180px"
        >
          <el-option v-for="item in setmealOptions" :key="item.id" :label="item.name + '（¥' + item.price + '）'" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="预约日期" prop="appointmentDate">
        <el-date-picker clearable
          v-model="queryParams.appointmentDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择预约日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预约时段" prop="appointmentTime">
        <el-select v-model="queryParams.appointmentTime" placeholder="预约时段" clearable style="width: 140px">
          <el-option v-for="dict in appointmentTimeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="预约状态" clearable style="width: 140px">
          <el-option v-for="dict in appointment_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="来源" prop="source">
        <el-select v-model="queryParams.source" placeholder="预约来源" clearable style="width: 140px">
          <el-option v-for="dict in sourceOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['reservation:appointment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['reservation:appointment:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['reservation:appointment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['reservation:appointment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appointmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预约ID" align="center" prop="id" width="70" />
      <el-table-column label="会员" align="center" width="150">
        <template #default="scope">
          <span>{{ scope.row.memberName || scope.row.memberId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="体检套餐" align="center" width="160">
        <template #default="scope">
          <span>{{ scope.row.setmealName || scope.row.setmealId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约日期" align="center" prop="appointmentDate" width="110">
        <template #default="scope">
          <span>{{ parseTime(scope.row.appointmentDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约时段" align="center" prop="appointmentTime" width="90" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template #default="scope">
          <dict-tag :options="appointment_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="预约来源" align="center" prop="source" width="90" />
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="110">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['reservation:appointment:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['reservation:appointment:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改预约记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="appointmentRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="会员" prop="memberId">
              <el-select
                v-model="form.memberId"
                placeholder="搜索并选择会员"
                clearable
                filterable
                remote
                :remote-method="searchMember"
                :loading="memberLoading"
                style="width: 100%"
              >
                <el-option v-for="item in memberOptions" :key="item.id" :label="item.name + '（' + item.phone + '）'" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="体检套餐" prop="setmealId">
              <el-select
                v-model="form.setmealId"
                placeholder="请选择体检套餐"
                clearable
                filterable
                style="width: 100%"
              >
                <el-option v-for="item in setmealOptions" :key="item.id" :label="item.name + '（¥' + item.price + '）'" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预约日期" prop="appointmentDate">
              <el-date-picker clearable
                v-model="form.appointmentDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择已放号的预约日期"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预约时段" prop="appointmentTime">
              <el-select v-model="form.appointmentTime" placeholder="请选择预约时段" clearable style="width: 100%">
                <el-option v-for="dict in appointmentTimeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" clearable style="width: 100%">
                <el-option v-for="dict in appointment_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预约来源" prop="source">
              <el-select v-model="form.source" placeholder="请选择预约来源" clearable style="width: 100%">
                <el-option v-for="dict in sourceOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" type="textarea" :rows="2" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Appointment">
import { listAppointment, getAppointment, delAppointment, addAppointment, updateAppointment } from "@/api/reservation/appointment"
import { listMember } from "@/api/member/member"
import { listSetmeal } from "@/api/reservation/setmeal"

const { proxy } = getCurrentInstance()
const { appointment_status } = proxy.useDict('appointment_status')

const appointmentList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

// 会员远程搜索
const memberOptions = ref([])
const memberLoading = ref(false)
function searchMember(query) {
  memberLoading.value = true
  listMember({ pageNum: 1, pageSize: 20, name: query }).then(response => {
    memberOptions.value = response.rows
    memberLoading.value = false
  })
}

// 套餐下拉
const setmealOptions = ref([])
function getSetmealOptions() {
  listSetmeal({ pageNum: 1, pageSize: 100 }).then(response => {
    setmealOptions.value = response.rows
  })
}

// 时段 / 来源 固定选项
const appointmentTimeOptions = [
  { value: '上午', label: '上午' },
  { value: '下午', label: '下午' },
  { value: '全天', label: '全天' }
]
const sourceOptions = [
  { value: '线上', label: '线上' },
  { value: '前台', label: '前台' },
  { value: '电话', label: '电话' }
]

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    memberId: undefined,
    setmealId: undefined,
    appointmentDate: undefined,
    appointmentTime: undefined,
    status: undefined,
    source: undefined,
  },
  rules: {
    memberId: [
      { required: true, message: "请选择会员", trigger: "change" }
    ],
    setmealId: [
      { required: true, message: "请选择体检套餐", trigger: "change" }
    ],
    appointmentDate: [
      { required: true, message: "请选择预约日期", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询预约记录列表 */
function getList() {
  loading.value = true
  listAppointment(queryParams.value).then(response => {
    appointmentList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: null,
    memberId: null,
    setmealId: null,
    appointmentDate: null,
    appointmentTime: null,
    status: null,
    source: null,
    remark: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null
  }
  proxy.resetForm("appointmentRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加预约记录"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getAppointment(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改预约记录"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["appointmentRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateAppointment(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addAppointment(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除预约记录编号为"' + _ids + '"的数据项？').then(function() {
    return delAppointment(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('reservation/appointment/export', {
    ...queryParams.value
  }, `appointment_${new Date().getTime()}.xlsx`)
}

// 初始化：加载会员与套餐下拉 + 列表
searchMember('')
getSetmealOptions()
getList()
</script>