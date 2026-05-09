<template>
  <div class="container">
    <h2>发布动态</h2>
    <div class="card">
      <div class="emoji">
        <span @click="addEmoji('😊')">😊</span>
        <span @click="addEmoji('😂')">😂</span>
        <span @click="addEmoji('👍')">👍</span>
        <span @click="addEmoji('❤️')">❤️</span>
        <span @click="addEmoji('🎉')">🎉</span>
        <span @click="addEmoji('🔥')">🔥</span>
        <span @click="addEmoji('😭')">😭</span>
        <span @click="addEmoji('✨')">✨</span>
      </div>

      <div class="tags">
        <button @click="addTag('#学习')">#学习</button>
        <button @click="addTag('#生活')">#生活</button>
        <button @click="addTag('#技术')">#技术</button>
        <button @click="addTag('#心情')">#心情</button>
        <button @click="addTag('#日常')">#日常</button>
      </div>

      <input v-model="title" placeholder="标题" />
      <p class="err" v-show="titleErr">标题不能为空</p>

      <textarea v-model="content" rows="6" placeholder="分享你的想法..."></textarea>
      <p class="err" v-show="contentErr">内容不能为空</p>

      <!-- 图片上传 -->
      <div class="upload-box">
        <div class="img-list">
          <div class="img-item" v-for="(item, idx) in imgList" :key="idx">
            <img :src="'http://localhost:8080' + item + '?t=' + new Date().getTime()" />
            <span class="del" @click="delImg(idx)">×</span>
          </div>

          <div class="add-img" @click="openFile">
            + 选择图片
            <input ref="fileInput" type="file" accept="image/*" multiple hidden @change="handleUpload">
          </div>
        </div>
      </div>

      <button class="submit" @click="send" :disabled="!user.isLogin" :class="{disabled:!user.isLogin}">
        {{ user.isLogin ? "发布动态" : "请先登录" }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../store/user'
import { useNoticeStore } from '../store/notice'
import { useRouter } from 'vue-router'
import { toast } from 'vue-sonner'
// ✅ 正确导入你的后端请求工具
import request from '@/api/request'
import { addPost } from '@/api/post'

const user = useUserStore()
const notice = useNoticeStore()
const router = useRouter()

const title = ref('')
const content = ref('')
const titleErr = ref(false)
const contentErr = ref(false)
const imgList = ref([])
const fileInput = ref(null)

// 打开文件选择
const openFile = () => {
  fileInput.value.click()
}

const addEmoji = (e) => {
  content.value += e
}
const addTag = (tag) => {
  content.value += ' ' + tag + ' '
}

// ======================
// ✅ 最终正确：图片上传
// ======================
const handleUpload = async (e) => {
  const files = e.target.files
  if (!files.length) return

  for (let file of files) {
    let fd = new FormData()
    fd.append('file', file)
    try {
      // ✅ 用 request 访问 8080 后端
      let res = await request.post('/post/upload', fd)
      if (res.code === 200) {
        imgList.value.push(res.data)
        toast.success('上传成功')
      } else {
        toast.error('上传失败')
      }
    } catch (e) {
      toast.error('上传异常')
      console.error(e)
    }
  }
}

// 删除图片
const delImg = (idx) => {
  imgList.value.splice(idx, 1)
}

// 发布
const send = async () => {
  if (!user.isLogin) {
    toast.error('请先登录')
    router.push('/login')
    return
  }
  titleErr.value = !title.value.trim()
  contentErr.value = !content.value.trim()
  if (!title.value || !content.value) {
    toast.warning('请填写完整')
    return
  }

  try {
    await addPost({
      title: title.value,
      content: content.value,
      userId: user.userInfo.id,
      imgUrls: imgList.value.join(',')
    })
    toast.success('发布成功！')
    router.push('/')
  } catch (e) {
    toast.error('发布失败')
  }
}
</script>

<style scoped>
.container {
  max-width: 700px;
  margin: 30px auto;
  padding: 0 20px;
}
.card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 1px 5px rgba(0,0,0,0.1);
}
.emoji {
  display: flex;
  gap: 10px;
  font-size: 22px;
  margin: 10px 0;
}
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin: 10px 0;
}
.tags button {
  background: #e2e8f0;
  border: none;
  padding: 4px 10px;
  border-radius: 20px;
}
input, textarea {
  width: 100%;
  padding: 10px;
  margin: 8px 0;
  border: 1px solid #ddd;
  border-radius: 6px;
}
.err {
  color: red;
  font-size: 12px;
}
.submit {
  width: 100%;
  padding: 12px;
  background: #4263eb;
  color: white;
  border: none;
  border-radius: 6px;
  margin-top: 10px;
}
.submit.disabled {
  background: #ccc;
}

.upload-box { margin: 12px 0; }
.img-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.img-item {
  width: 80px;
  height: 80px;
  position: relative;
  border: 1px solid #eee;
  border-radius: 6px;
}
.img-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.del {
  position: absolute;
  top: 2px;
  right: 2px;
  background: #00000066;
  color: white;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  text-align: center;
  line-height: 18px;
  font-size: 14px;
  cursor: pointer;
}

.add-img {
  width: 80px;
  height: 80px;
  border: 1px dashed #ccc;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer !important;
  color: #999;
}
</style>