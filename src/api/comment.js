import request from './request'

// 发布评论
export function addComment(data) {
  return request({
    url: '/comment/add',
    method: 'post',
    data
  })
}

// 获取某个帖子的评论
export function getComments(postId) {
  return request({
    url: '/comment/listByPostId',
    method: 'get',
    params: { postId }
  })
}

// 删除评论
export function delComment(id) {
  return request({
    url: '/comment/delete',
    method: 'get',
    params: { id }
  })
}