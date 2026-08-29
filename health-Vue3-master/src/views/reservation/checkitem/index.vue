<template>
  <div class="app-container">
<!--    搜索框开始-->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item  prop="searchValue">
        <el-input
            v-model="queryParams.searchValue"
            placeholder="项目编码/项目名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="项目编码" prop="code">-->
<!--        <el-input-->
<!--          v-model="queryParams.code"-->
<!--          placeholder="请输入项目编码"-->
<!--          clearable-->
<!--          @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="项目名称" prop="name">-->
<!--        <el-input-->
<!--          v-model="queryParams.name"-->
<!--          placeholder="请输入项目名称"-->
<!--          clearable-->
<!--          @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="类型" prop="type" >
        <el-select v-model="queryParams.type" placeholder="请选择" clearable style="width: 150px" >
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button  icon="Search" @click="handleQuery">搜索</el-button>
<!--                <el-button icon="Refresh" @click="resetQuery">重置</el-button>-->
        <el-button
            type="primary"
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['reservation:checkitem:add']"
        >新增</el-button>
<!--        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="Refresh" @click="resetQuery">重置</el-button>-->
      </el-form-item>
    </el-form>
<!--    搜索框结束-->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['reservation:checkitem:add']"
        >新增</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="Edit"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['reservation:checkitem:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="Delete"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['reservation:checkitem:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="Download"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['reservation:checkitem:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="checkitemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" align="center" width="50"/>
      <el-table-column label="检查项ID" align="center" prop="id" />
      <el-table-column label="项目编码" align="center" prop="code" />
      <el-table-column label="项目名称" align="center" prop="name" />
<!--      <el-table-column label="适用性别" align="center" prop="sex" />-->
      <el-table-column label="适用性别" align="center" prop="sex">
        <template #default="scope">
          <dict-tag :options="health_sex" :value="scope.row.sex"/>
<!--          <el-tag type="success" v-if="scope.row.sex == 0">不限</el-tag>-->
<!--          <el-tag type="danger" v-else-if="scope.row.sex == 1">男</el-tag>-->
<!--          <el-tag type="info" v-else="scope.row.sex == 2">女</el-tag>-->
        </template>
      </el-table-column>
      <el-table-column label="适用年龄" align="center" prop="age" />
      <el-table-column label="价格" align="center" prop="price" />
<!--      <el-table-column label="检查类型" align="center" prop="type" />-->
      <el-table-column label="检查类型" align="center" prop="type" >
        <template #default="scope">
          <dict-tag :options="health_type" :value="scope.row.type"/>
<!--          <el-tag :type="scope.row.type == 1 ? 'success' : 'danger'">-->
<!--            {{ scope.row.type == 1 ? '检查' : '检验' }}-->
<!--          </el-tag>-->
        </template>
      </el-table-column>
      <el-table-column label="注意事项" align="center" prop="attention" />
      <el-table-column label="项目说明" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
<!--          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>-->
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['reservation:checkitem:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['reservation:checkitem:remove']">删除</el-button>
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

    <!-- 添加或修改检查项管理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="checkitemRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入项目编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择适用性别">
                <el-option
                    v-for="dict in health_sex"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用年龄" prop="age">
              <el-input v-model="form.age" placeholder="请输入适用年龄" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input v-model="form.price" placeholder="请输入价格" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio
                    v-for="dict in health_tybe"
                    :key="dict.value"
                    :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注意事项" prop="attention">
              <el-input v-model="form.attention" placeholder="请输入注意事项" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目说明" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入项目说明" />
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

<script setup name="Checkitem">
import { listCheckitem, getCheckitem, delCheckitem, addCheckitem, updateCheckitem } from "@/api/reservation/checkitem"

const { proxy } = getCurrentInstance()

//引用数据字典，定义的变量名称必须和字典类型保持一致
const { health_sex,health_type } = proxy.useDict('health_sex','health_type')

const checkitemList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")


const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: null,
    name: null,
    type: null,
    searchValue: null,
  },
  rules: {
  }
})
// const data = reactive({
//   form: {},
//   queryParams: {
//     pageNum: 1,
//     pageSize: 10,
//     code: undefined,
//     name: undefined,
//   },
//   rules: {
//   }
// })

const options = ref([
  {
    value: 1,
    label: "检查"
  },
  {
    value: 2,
    label: "检验"
  }
])

const { queryParams, form, rules } = toRefs(data)

/** 查询检查项管理列表 */
function getList() {
  loading.value = true
  listCheckitem(queryParams.value).then(response => {
    checkitemList.value = response.rows
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
    code: null,
    name: null,
    sex: null,
    age: null,
    price: null,
    type: null,
    attention: null,
    remark: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null
  }
  proxy.resetForm("checkitemRef")
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
  title.value = "添加检查项管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getCheckitem(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改检查项管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["checkitemRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateCheckitem(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCheckitem(form.value).then(() => {
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
  proxy.$modal.confirm('是否确认删除检查项管理编号为"' + _ids + '"的数据项？').then(function() {
    return delCheckitem(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('reservation/checkitem/export', {
    ...queryParams.value
  }, `checkitem_${new Date().getTime()}.xlsx`)
}

getList()
</script>
