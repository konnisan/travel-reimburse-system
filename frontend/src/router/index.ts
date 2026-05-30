import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/reimburse/list'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: {
        public: true
      }
    },
    {
      path: '/reimburse/list',
      name: 'reimburse-list',
      component: () => import('@/views/ReimburseListView.vue'),
      meta: {
        requiresAuth: true,
        permission: 'reimburse:list'
      }
    },
    {
      path: '/reimburse/detail/:id',
      name: 'reimburse-detail',
      component: () => import('@/views/ReimburseDetailView.vue'),
      meta: {
        requiresAuth: true,
        permission: 'reimburse:view'
      }
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/reimburse/list'
    }
  ]
})

router.beforeEach((to) => {
  const auth = useAuthStore()

  if (to.meta.public) {
    return auth.isAuthenticated ? '/reimburse/list' : true
  }

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }

  const permission = to.meta.permission
  if (typeof permission === 'string' && !auth.hasPermission(permission)) {
    return '/reimburse/list'
  }

  return true
})

export default router
