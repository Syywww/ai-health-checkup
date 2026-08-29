import request from '@/utils/request'

// 查询预约设置列表
export function listSetting(query) {
  return request({
    url: '/reservation/setting/list',
    method: 'get',
    params: query
  })
}

// 查询预约设置详细
export function getSetting(id) {
  return request({
    url: '/reservation/setting/' + id,
    method: 'get'
  })
}

// 新增预约设置
export function addSetting(data) {
  return request({
    url: '/reservation/setting',
    method: 'post',
    data: data
  })
}

// 修改预约设置
export function updateSetting(data) {
  return request({
    url: '/reservation/setting',
    method: 'put',
    data: data
  })
}

// 删除预约设置
export function delSetting(id) {
  return request({
    url: '/reservation/setting/' + id,
    method: 'delete'
  })
}

// ===== 以下为预约设置日历接口（对齐 PDF 03） =====

// 根据年月查询当月预约设置（日历展示）
export function getOrderSettingByMonth(month) {
  return request({
    url: '/reservation/setting/getOrderSettingByMonth',
    method: 'get',
    params: { month }
  })
}

// 新增某天预约设置（日历）
export function addOrderSetting(data) {
  return request({
    url: '/reservation/setting/add',
    method: 'post',
    data
  })
}

// 修改某天可预约人数（日历）
export function editNumberByOrderDate(data) {
  return request({
    url: '/reservation/setting/editNumberByOrderDate',
    method: 'put',
    data
  })
}

// 下载预约设置导入模板
export function downloadTemplate() {
  return request({
    url: '/reservation/setting/downloadTemplate',
    method: 'get',
    responseType: 'blob'
  })
}

// 上传预约设置 Excel 导入
export function uploadOrderSettingExcel(file) {
  const formData = new FormData()
  formData.append('excelFile', file)
  return request({
    url: '/reservation/setting/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
