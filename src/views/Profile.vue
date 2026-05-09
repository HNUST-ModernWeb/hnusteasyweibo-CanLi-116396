<template>
  <div class="container" v-if="user.isLogin">
    <div class="card">
      <h2>个人资料</h2>
      <img :src="user.userInfo.avatar || 'https://picsum.photos/100'" class="avatar" @click="file.click" />
      <input type="file" ref="file" style="display: none" @change="upAvatar" />

      <div v-if="!edit">
        <p>昵称：{{ user.userInfo.name || '未设置' }}</p>
        <p>简介：{{ user.userInfo.intro || '未设置' }}</p>
        <button class="btn" @click="edit = true">编辑资料</button>
      </div>
      <div v-else>
        <input v-model="nName" placeholder="昵称" />
        <textarea v-model="nIntro" placeholder="简介"></textarea>
        <button class="btn" @click="save">保存</button>
      </div>
    </div>

    <h2 style="margin:30px 0 16px">我的发布</h2>
    <div class="grid">
      <div class="card" v-for="p in myPosts" :key="p.id">
        <h3>{{ p.title }}</h3>
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
          <button class="red" @click="del(p.id)">删除</button>
        </div>

        <div v-if="showComment[p.id]" class="comment">
          <!-- 数据库评论 -->
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
  <div v-else class="tip">请先登录</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { useNoticeStore } from '@/store/notice'
import { toast } from 'vue-sonner'
import { getPostList, deletePost, like } from '@/api/post'
import { updateUser } from '@/api/user'
import { addComment, getComments, delComment } from '@/api/comment'

const user = useUserStore()
const notice = useNoticeStore()
const file = ref(null)

const edit = ref(false)
const nName = ref(user.userInfo.name || '')
const nIntro = ref(user.userInfo.intro || '')
const realPostList = ref([])
const showComment = ref({})
const cText = ref('')
const commentList = ref({}) // 数据库评论

const emojis = ref(['😀','😂','😍','👍','❤️','🎉','🔥','🥺','😢','😡'])
const showEmoji = ref({})

onMounted(() => {
  loadMyPosts()
  notice.clear()
})

const loadMyPosts = async () => {
  try {
    const res = await getPostList()
    realPostList.value = res.data
  } catch (e) {
    toast.error("加载失败")
  }
}

const myPosts = computed(() => {
  const uid = Number(user.userInfo.id)
  if (!realPostList.value) return []
  return realPostList.value.filter(p => p && Number(p.userId) === uid)
})

const likePost = async (post) => {
  if (!user.isLogin) return toast.error('请先登录')
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

// 打开评论 → 加载数据库
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

const save = async () => {
  try {
    const params = {
      id: user.userInfo.id,
      name: nName.value,
      intro: nIntro.value,
      avatar: user.userInfo.avatar || ''
    }

    await updateUser(params)

    user.userInfo.name = nName.value
    user.userInfo.intro = nIntro.value

    edit.value = false
    alert("✅ 全部保存成功！")
  } catch (e) {
    console.error(e)
    alert("保存失败")
  }
}

const upAvatar = async (e) => {
  const file = e.target.files[0]
  if (!file) return

  const reader = new FileReader()
  reader.onload = async (event) => {
    const base64Str = event.target.result

    await updateUser({
      id: user.userInfo.id,
      name: user.userInfo.name,
      intro: user.userInfo.intro,
      avatar: base64Str
    })

    user.userInfo.avatar = base64Str
    alert("✅ 头像保存成功！")
  }
  reader.readAsDataURL(file)
}

const del = async (id) => {
  if (!confirm('确定删除？')) return
  await deletePost(id)
  toast.success('删除成功')
  loadMyPosts()
}
</script>

<style scoped>
/* 样式完全不变，保持美观 */
.container { max-width: 1000px; margin: 30px auto; padding: 0 20px; }
.card { background: var(--card); padding: 20px; border-radius: 12px; margin-bottom: 16px; }
.avatar { width: 80px; height: 80px; border-radius: 50%; object-fit: cover; cursor: pointer; }
.btn { background: #4263eb; color: white; border: none; padding: 8px 12px; border-radius: 6px; cursor: pointer; }
.grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 16px; }
.content { line-height: 1.6; margin: 10px 0; }

.post-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 8px;
  margin: 10px 0;
}

.btns { display: flex; gap: 8px; flex-wrap: wrap; }
.orange { background: #f59e0b; color: white; border: none; padding: 6px 10px; border-radius: 6px; cursor: pointer; }
.blue { background: #3b82f6; color: white; border: none; padding: 6px 10px; border-radius: 6px; cursor: pointer; }
.red { background: #e53e3e; color: white; border: none; padding: 6px 10px; border-radius: 6px; cursor: pointer; }

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
.input-wrap button { background: #4263eb; color: white; border: none; padding: 6px 10px; border-radius: 6px; cursor: pointer; }

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

.tip { text-align: center; margin-top: 60px; font-size: 18px; }
input, textarea {
  width: 100%;
  padding: 8px;
  margin: 8px 0;
  border-radius: 6px;
  border: 1px solid #ccc;
  background: var(--card);
  color: var(--text);
}
</style>