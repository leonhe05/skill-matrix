import request from '@/utils/request'

// 查询技能列表
export function listPosition(query) {
  return request({
    url: '/talent/position/list',
    method: 'get',
    params: query
  })
}

// 获取技能树
export function listPositionTree() {
  return request({
    url: '/talent/position/listTree',
    method: 'get'
  })
}



// 新增技能
export function addPosition(data) {
  return request({
    url: '/talent/position',
    method: 'post',
    data: data
  })
}

// 修改技能
export function updatePosition(data) {
  return request({
    url: '/talent/position',
    method: 'put',
    data: data
  })
}

// 删除技能
export function delPosition(positionId) {
  return request({
    url: '/talent/position/' + positionId,
    method: 'delete'
  })
}

// 为岗位添加员工
export function assignOwner(data) {
  return request({
    url: '/talent/position/assign/insertPositionEmployee',
    method: 'put',
    data: data
  })
}

// 为岗位删除员工
export function deletePositionEmployee(data) {
  return request({
    url: '/talent/position/assign/deletePositionEmployee',
    method: 'post',
    data: data
  })
}

// 获取该岗位的所有成员
export function listPositionEmployee(query) {
  return request({
    url: '/talent/position/assign/listPositionEmployee',
    method: 'get',
    params: query
  })
}

// 更新主管
export function updatePositionEmployee(data) {
  return request({
    url: '/talent/position/assign/updatePositionEmployee',
    method: 'post',
    data: data
  })
}


