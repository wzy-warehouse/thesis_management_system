import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/UserLogin.vue'),
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('@/views/home/HomePage.vue'),
    },
  ],
})

export default router
