import request from '@/utils/request'

export function getStatistics(params) {
    return request({
        url: '/member/statistics',
        method: 'get',
        params: params
    })
}