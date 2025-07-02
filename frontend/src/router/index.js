import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth' 
import LoginForm from '../components/LoginForm.vue'
import RegisterForm from '../components/RegisterForm.vue'
import KanjiCreate from '../components/KanjiCreate.vue'
import AdminUsers from '../views/AdminUsers.vue'
import KanjiListView from '../views/KanjiListView.vue'
import RadicalListView from '../views/RadicalListView.vue'
import RadicalCreate from '../components/RadicalCreate.vue'

const routes = [
  { path: '/', component: KanjiListView },
  { path: '/radicals', component: RadicalListView},
  { path: '/login', component: LoginForm },
  { path: '/register', component: RegisterForm },
  {
    path: '/users',
    component: AdminUsers,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/kanji/create',
    name: 'KanjiCreate',
    component: KanjiCreate
  },
  {
    path: '/radicals/create',
    name: 'RadicalCreate',
    component: RadicalCreate
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return next('/login')
  }

  if (to.meta.requiresAdmin && auth.user?.role !== 'ROLE_ADMIN') {
    return next('/')
  }

  next()
})

export default router

