import request from './request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

// 👇 只改了这里：update → updateUser
export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data
  })
}