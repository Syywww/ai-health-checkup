import request from '@/utils/request'

export function listExamination(query) {
    return request({
        url: '/member/examination/list',
        method: 'get',
        params: query
    })
}

export function getExamination(id) {
    return request({
        url: '/member/examination/' + id,
        method: 'get'
    })
}

export function addExamination(data) {
    return request({
        url: '/member/examination',
        method: 'post',
        data: data
    })
}

export function updateExamination(data) {
    return request({
        url: '/member/examination',
        method: 'put',
        data: data
    })
}

export function delExamination(ids) {
    return request({
        url: '/member/examination/' + ids,
        method: 'delete'
    })
}