<template>
  <div :class="{ dark: theme.darkMode }">
    <NavBar />
    <Toaster position="top-right" />
    <router-view />
  </div>
</template>

<script setup>
import NavBar from './components/NavBar.vue'
import { useThemeStore } from './store/theme'
const theme = useThemeStore()
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
:root {
  --bg: #f5f7fa;
  --card: #fff;
  --text: #333;
}
.dark {
  --bg: #18181b;
  --card: #27272a;
  --text: #e5e7eb;
}
body {
  background: var(--bg);
  color: var(--text);
  font-family: Arial, sans-serif;
  min-height: 100vh;
}
</style>