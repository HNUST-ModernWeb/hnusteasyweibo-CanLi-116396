<template>
  <div class="container">
    <h2>动态广场</h2>
    <div class="grid">
      <div class="card" v-for="p in postList" :key="p.id">
        <div class="post-user" v-if="user.isLogin">
          <img 
            class="avatar" 
            :src="p.avatar || 'https://picsum.photos/200/200'" 
            alt="头像"
          >
          <span class="nickname">{{ p.name || '未知用户' }}</span>
        </div>

        <h3 class="post-title">{{ p.title }}</h3>
        <p class="content">{{ p.content }}</p>

        <img
          v-if="p.imgUrls"
          :src="'http://localhost:8080' + p.imgUrls"
          class="post-image"
        />

        <div class="btns">
          <button class="orange" @click="likePost(p)">
            点赞 {{ p.like || 0 }}
          </button>
          <button class="blue" @click="toggleComment(p.id)">评论</button>
        </div>

        <div v-if="showComment[p.id]" class="comment">
          <!-- 从数据库加载评论 -->
          <div class="item" v-for="c in commentList[p.id]" :key="c.id">
            <img class="comment-avatar" :src="c.avatar" alt="头像" />
            <div class="comment-info">
              <div class="comment-name">{{ c.name }}</div>
              <div class="comment-content">{{ c.content }}</div>
            </div>
            <button 
               v-if="Number(c.userId) === Number(user.userInfo.id)" 
              class="comment-del" 
              @click="deleteComment(c.id, p.id)"
            >
              删除
            </button>
          </div>

          <div class="input-wrap">
            <button class="emoji-btn" @click="showEmoji[p.id] = !showEmoji[p.id]">😊</button>
            <input v-model="cText" placeholder="写评论..." />
            <button @click="sendComment(p.id)">发送</button>
          </div>

          <div v-if="showEmoji[p.id]" class="emoji-group">
            <span v-for="em in emojis" :key="em" @click="selectEmoji(p.id, em)" class="emoji">{{ em }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { toast } from 'vue-sonner'
import { getPostList, like } from '@/api/post'
import { addComment, getComments, delComment } from '@/api/comment'

const user = useUserStore()

const postList = ref([])
const showComment = ref({})
const cText = ref('')
const commentList = ref({}) // 数据库评论

const emojis = ref(['😀', '😂', '😍', '👍', '❤️', '🎉', '🔥', '🥺', '😢', '😡'])
const showEmoji = ref({})

onMounted(() => {
  loadList()
})

const loadList = async () => {
  const res = await getPostList()
  postList.value = res.data
}

// 点赞
const likePost = async (post) => {
  try {
    if (!post.likeStatus) {
      await like(post.id, true)
      post.like = (post.like || 0) + 1
      post.likeStatus = true
      toast.success('点赞成功')
    } else {
      await like(post.id, false)
      post.like = (post.like || 1) - 1
      post.likeStatus = false
      toast.info('取消点赞')
    }
  } catch (e) {
    toast.error('操作失败')
  }
}

// 打开评论 → 从数据库加载
const toggleComment = async (id) => {
  showComment.value[id] = !showComment.value[id]
  if (showComment.value[id]) {
    const res = await getComments(id)
    commentList.value[id] = res.data
  }
}

// 发送评论 → 存数据库
const sendComment = async (postId) => {
  if (!cText.value.trim()) return
  await addComment({
    postId,
    userId: user.userInfo.id,
    name: user.userInfo.name,
    avatar: user.userInfo.avatar,
    content: cText.value
  })
  // 重新加载评论
  const res = await getComments(postId)
  commentList.value[postId] = res.data
  cText.value = ''
  showEmoji.value[postId] = false
  toast.success('评论成功')
}

// 删除评论 → 删数据库
const deleteComment = async (cid, postId) => {
  await delComment(cid)
  const res = await getComments(postId)
  commentList.value[postId] = res.data
  toast.success('删除成功')
}

const selectEmoji = (postId, em) => {
  cText.value += em
}
</script>

<style scoped>
/* 你的样式完全不变，保持原来一样美观 */
.container { max-width: 1000px; margin: 30px auto; padding: 0 20px; }
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}
.card { background: var(--card); padding: 20px; border-radius: 12px; }
.post-user {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  gap: 10px;
}
.avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  object-fit: cover;
}
.nickname {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}
.post-title {
  margin: 0 0 10px 0;
  font-size: 18px;
}
.content { line-height: 1.6; margin: 10px 0; }

.post-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px;
  margin: 10px 0;
}

.btns { display: flex; gap: 8px; flex-wrap: wrap; }
.orange { background: #f59e0b; color: white; border: none; padding: 6px 10px; border-radius: 6px; cursor: pointer; }
.blue { background: #3b82f6; color: white; border: none; padding: 6px 10px; border-radius: 6px; cursor: pointer; }

.comment { margin-top: 12px; padding-top: 12px; border-top: 1px solid #eee; }
.item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  padding: 6px 0;
  font-size: 14px;
}
.comment-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}
.comment-info {
  flex: 1;
  line-height: 1.4;
}
.comment-name {
  font-size: 12px;
  font-weight: bold;
  color: #666;
}
.comment-content {
  font-size: 14px;
  color: #333;
}
.comment-del {
  background: none;
  border: none;
  color: #999;
  font-size: 12px;
  cursor: pointer;
}
.comment-del:hover {
  color: #e53e3e;
}

.input-wrap { display: flex; gap: 8px; margin-top: 8px; }
.input-wrap input { flex: 1; padding: 6px; border: 1px solid #ddd; border-radius: 6px; }

.emoji-btn {
  background: #f5f5f5;
  border: none;
  border-radius: 6px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 16px;
}
.emoji-group {
  display: flex;
  gap: 6px;
  margin-top: 6px;
  flex-wrap: wrap;
}
.emoji {
  font-size: 16px;
  cursor: pointer;
}
.emoji:hover {
  transform: scale(1.1);
}
</style>