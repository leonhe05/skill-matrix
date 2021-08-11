import request from '@/utils/request'


// 获取技能树
export function listMyPositionTree() {
  return request({
    url: '/talent/position/listMyTree',
    method: 'get'
  })
}

// 获取岗位技能评分
export function listRating(query) {
  return request({
    url: '/talent/rating/list',
    method: 'get',
    params: query
  })
}

// 获取岗位技能评分
export function checkOwner(query) {
  return request({
    url: '/talent/rating/checkOwner',
    method: 'get',
    params: query
  })
}

// 更改评分
export function editRating(data) {
  return request({
    url: '/talent/rating',
    method: 'put',
    data: data
  })
}

// 导出用户
export function exportRating(query) {
  return request({
    url: '/talent/rating/export',
    method: 'get',
    params: query
  })
}
