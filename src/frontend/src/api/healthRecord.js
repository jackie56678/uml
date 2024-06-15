import request from '@/utils/request'

export function getHealthRecord() {
  return request({
    url: `/healthRecord/getHealthRecord`,
    method: 'get',
  })
}

export function getRecord(username) {
    return request({
      url: `/healthRecord/getRecord`,
      method: 'get',
      data: username
    })
}

export function updateHealthRecord(data) {
  return request({
    url: `/healthRecord/updateRecord`,
    method: 'post',
    data: data
  })
}
