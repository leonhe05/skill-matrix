
import request from '@/utils/request'



export function listEmployee(query) {
    return request({
      url: '/talent/employee/getEmployeelist',
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





