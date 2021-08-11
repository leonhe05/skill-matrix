import request from '@/utils/request'

// 查询技能列表
export function listCompetence(query) {
  return request({
    url: '/talent/competence/list',
    method: 'get',
    params: query
  })
}

// 查询技能列表
export function listCompetenceByPosition(query) {
  return request({
    url: '/talent/competence/listByPosition',
    method: 'get',
    params: query
  })
}

// 获取技能树
export function listCompetenceTree() {
  return request({
    url: '/talent/competence/listTree',
    method: 'get'
  })
}

// 新增技能
export function addCompetence(data) {
  return request({
    url: '/talent/competence',
    method: 'post',
    data: data
  })
}

// 修改技能
export function updateCompetence(data) {
  return request({
    url: '/talent/competence',
    method: 'put',
    data: data
  })
}

// 删除技能
export function delCompetence(competenceId) {
  return request({
    url: '/talent/competence/' + competenceId,
    method: 'delete'
  })
}

// 下载用户导入模板
export function importTemplate() {
  return request({
    url: '/talent/competence/importTemplate',
    method: 'get'
  })
}

// 导出用户
export function exportCompetence(query) {
  return request({
    url: '/talent/competence/export',
    method: 'get',
    params: query
  })
}
