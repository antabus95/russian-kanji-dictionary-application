<template>
  <div class="app-wrapper">
    <header class="app-header">
      <nav class="nav-container">
        <div class="nav-left">
          <!-- Здесь будет логотип -->
        </div>

        <div class="nav-center">
          <router-link to="/">Главная</router-link>
          <router-link to="/radicals">Радикалы</router-link>
          <router-link v-if="auth.isAuthenticated" to="/radicals">Мои списки</router-link>
          <router-link v-if="isAdmin" to="/users">Пользователи</router-link>
        </div>

        <div class="nav-right">
          <span v-if="auth.isAuthenticated">
            Добро пожаловать, {{ auth.user.username }}!
            <button @click="logoutAndRedirect">Выйти</button>
          </span>
          <span v-else>
            <router-link to="/login">Войти</router-link>
            <router-link to="/register">Регистрация</router-link>
          </span>
        </div>
      </nav>
    </header>

    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useAuthStore } from './stores/auth'
import { useRouter } from 'vue-router'
import { computed } from 'vue'

const auth = useAuthStore()
const router = useRouter()

function logoutAndRedirect() {
  auth.logout()
  router.push('/')
}

const isAdmin = computed(() => {
  return auth.isAuthenticated && auth.user?.role === 'ROLE_ADMIN'
})

</script>

<style scoped>

.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh; 
}

a {
  padding: 1rem;
}

.app-main {
  flex-grow: 1;
  margin-top: 20px;
  padding: 0 20px;
  box-sizing: border-box; 
}

.app-header button {
  margin-left: 1rem;
  padding: 0.3rem 0.8rem;
  cursor: pointer;
}

.app-header {
  background-color: #1e1e1e; 
  padding: 10px 20px;
  border-bottom: 1px solid #333; 
  color: #eee; 
}

.nav-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-left {
  flex: 1;
}

.nav-center {
  flex: 1;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.nav-center a {
  color: #bbb;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-center a:hover {
  color: #fff; 
}

.nav-right {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  color: #bbb;
}

.nav-right button {
  background-color: transparent;
  border: 1px solid #555;
  color: #bbb;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.nav-right button:hover {
  background-color: #555;
  color: #fff;
}

.nav-right a {
  color: #bbb;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-right a:hover {
  color: #fff;
}

.router-link-active {
  font-weight: 700;
  color: #4fc3f7; 
  text-decoration: underline;
}

</style>