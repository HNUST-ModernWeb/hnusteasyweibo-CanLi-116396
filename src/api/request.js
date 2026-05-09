import axios from 'axios'

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(config => {
  console.log('请求发送:', config)
  return config
}, err => {
  console.error('请求发送失败:', err)
  return Promise.reject(err)
})

// 响应拦截器
request.interceptors.response.use(
  res => {
    console.log('响应成功:', res.data)
    return res.data
  },
  err => {
    console.error('响应异常:', err)
    return Promise.reject(err)
  }
)

export default request