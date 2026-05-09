<template>
  <div class="post-card">
    <h3 class="post-title">{{ title }}</h3>
    <p class="post-content">{{ content }}</p>
  </div>
</template>

<script setup>
// 定义组件接收的props（父组件传值）
const props = defineProps({
  title: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  }
})
</script>

<style scoped>
.post-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 1rem;
  box-shadow: 0 3px 15px rgba(0, 0, 0, 0.05);
  transition: 0.3s;
  border: 1px solid #eee;
}

.post-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
}

.post-title {
  margin-bottom: 0.5rem;
  color: #222;
  font-size: 1.2rem;
}

.post-content {
  color: #555;
  line-height: 1.6;
}
</style>