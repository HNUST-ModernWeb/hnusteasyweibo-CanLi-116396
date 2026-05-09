import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Publish from '../views/Publish.vue'
import Profile from '../views/Profile.vue'
import Login from '../views/Login.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/publish', component: Publish },
  { path: '/profile', component: Profile },
  { path: '/login', component: Login },
  {path: '/register',component: () => import('../views/Register.vue')
},
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router