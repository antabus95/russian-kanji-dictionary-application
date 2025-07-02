import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)
  const user = ref(null)
  const isAuthenticated = computed(() => !!token.value)

  if (token.value) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`
    setUserFromToken(token.value)
  }

  function setUserFromToken(jwt) {
    try {
      const payload = JSON.parse(atob(jwt.split('.')[1]))
      user.value = {
        username: payload.sub,
        role: payload.roles
      }
    } catch (e) {
      console.error('Ошибка при разборе JWT:', e)
      user.value = null
    }
  }

  async function login(username, password) {
    try {
      const res = await axios.post('/api/v1/auth/login', { username, password })
      token.value = res.data.token
      localStorage.setItem('token', token.value)
      axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`
      setUserFromToken(token.value)
    } catch (e) {
      logout()
      throw e
    }
  }

  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    delete axios.defaults.headers.common['Authorization']
  }

  async function register(username, password) {
    try {
      const res = await axios.post('/api/v1/auth/register', { username, password })
      token.value = res.data.token
      localStorage.setItem('token', token.value)
      axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`
      setUserFromToken(token.value)
    } catch (e) {
      throw e
    }
  }

  return {
    token,
    user,
    isAuthenticated,
    login,
    register,
    logout
  }
})
