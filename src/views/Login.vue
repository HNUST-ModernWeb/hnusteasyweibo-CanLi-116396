<template>
  <div class="login-page">
    <div class="login-box">
      <h2>登录</h2>

      <el-form label-width="80px" @submit.prevent="handleLogin">
        <el-form-item label="账号">
          <el-input v-model="username" placeholder="请输入账号" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="password" type="password" placeholder="请输入密码" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit" block>登录</el-button>
          <el-button link @click="$router.push('/register')">去注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { login } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()

const username = ref('')
const password = ref('')

// 登录方法
const handleLogin = async () => {
  try {
    const res = await login({
      username: username.value,
      password: password.value
    })

    if (res && res.code === 200) {
      // ✅ 正确调用 store 方法
      userStore.login(res.data)
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res?.msg || '登录失败')
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('服务器连接失败，请检查后端是否启动')
  }
}
</script>

<style>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}
.login-box {
  width: 400px;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 0 10px #ccc;
}
</style>