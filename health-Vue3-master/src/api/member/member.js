import request from '@/utils/request'

export function listMember(query) {
    return request({
        url: '/member/member/list',
        method: 'get',
        params: query
    })
}

export function getMember(id) {
    return request({
        url: '/member/member/' + id,
        method: 'get'
    })
}

export function addMember(data) {
    return request({
        url: '/member/member',
        method: 'post',
        data: data
    })
}

export function updateMember(data) {
    return request({
        url: '/member/member',
        method: 'put',
        data: data
    })
}

export function delMember(ids) {
    return request({
        url: '/member/member/' + ids,
        method: 'delete'
    })
}