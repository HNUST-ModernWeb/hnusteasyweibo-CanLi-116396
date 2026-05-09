import request from './request'

// 获取所有帖子
export function getPostList() {
  return request({
    url: '/post/list',
    method: 'get'
  })
}

// 发布帖子
export function addPost(data) {
  return request({
    url: '/post/add',
    method: 'post',
    data
  })
}

// 删除帖子
export function deletePost(id) {
  return request({
    url: `/post/delete/${id}`,
    method: 'delete'
  })
}
export function like(postId, isLike) {
  return request({
    url: '/post/like/' + postId,
    method: 'post',
    params: { isLike }
  })
}