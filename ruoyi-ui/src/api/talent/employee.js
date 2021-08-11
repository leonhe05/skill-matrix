
import request from '@/utils/request'



export function listEmployee(query) {
    return request({
      url: '/talent/employee/getEmployeeList',
      method: 'get',
      params: query
    })
}

// 修改用户登录信息
export function updateEmployee(data) {
  return request({
    url: '/talent/employee',
    method: 'put',
    data: data
  })
}

// 导出用户
export function exportUser(query) {
  return request({
    url: '/talent/employee/export',
    method: 'get',
    params: query
  })
}


// 下载用户导入模板
export function importTemplate() {
  return request({
    url: '/talent/employee/importTemplate',
    method: 'get'
  })
}
