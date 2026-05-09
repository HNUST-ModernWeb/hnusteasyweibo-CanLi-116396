<template>
  <div class="login-box">
    <h2>用户注册</h2>
    <input v-model="username" placeholder="请输入用户名" />
    <input v-model="password" placeholder="请输入密码" type="password" />
    <button @click="handleRegister">立即注册</button>
    <p class="tip" @click="$router.push('/login')">已有账号？去登录</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/user'

const router = useRouter()
const username = ref('')
const password = ref('')

// 注册逻辑
const handleRegister = async () => {
  if (!username.value || !password.value) {
    alert('请输入用户名和密码')
    return
  }

  try {
    let res = await register({
      username: username.value,
      password: password.value
    })

    if (res.code === 200) {
      alert('注册成功！请登录')
      router.push('/login')
    } else {
      alert(res.msg)
    }
  } catch (e) {
    alert('注册失败，请检查后端是否启动')
  }
}
</script>

<style scoped>
.login-box {
  max-width: 360px;
  margin: 100px auto;
  padding: 30px;
  background: white;
  border-radius: 12px;
  text-align: center;
}
input {
  width: 100%;
  padding: 10px;
  margin: 15px 0;
  border-radius: 6px;
  border: 1px solid #ddd;
}
button {
  width: 100%;
  padding: 10px;
  background: #22c55e;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.tip {
  margin-top: 15px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
}
</style>