<template>
  <div class="login-container" v-if="!auth.isAuthenticated">
    <form class="login-form" @submit.prevent="handleLogin" novalidate>
      <h2>Вход в систему</h2>

      <div class="form-group">
        <label for="username">Логин</label>
        <input
          id="username"
          v-model="username"
          type="text"
          required
          autocomplete="username"
          :class="{ 'input-error': errorMessage }"
        />
      </div>

      <div class="form-group">
        <label for="password">Пароль</label>
        <input
          id="password"
          v-model="password"
          type="password"
          required
          autocomplete="current-password"
          :class="{ 'input-error': errorMessage }"
        />
      </div>

      <button type="submit">Войти</button>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const errorMessage = ref('')
const successMessage = ref('')

const auth = useAuthStore()
const router = useRouter()

const handleLogin = async () => {
  errorMessage.value = ''
  successMessage.value = ''
  try {
    await auth.login(username.value, password.value)
    successMessage.value = 'Успешный вход!'
    router.push('/')
  } catch (e) {
    if (e.response?.status === 401) {
      errorMessage.value = 'Неверный логин или пароль'
    } else {
      errorMessage.value = e.response?.data?.message || e.message || 'Ошибка входа'
    }
  }
}


</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  padding: 2rem;
}

.login-form {
  background: #2a2a2a;
  padding: 2rem 2.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.4);
  width: 100%;
  max-width: 400px;
  color: #f0f0f0;
}

.login-form h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #ffffff;
}

.form-group {
  margin-bottom: 1.2rem;
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 0.4rem;
  font-size: 0.95rem;
  color: #cccccc;
}

input {
  padding: 0.6rem;
  border: 1px solid #555;
  border-radius: 4px;
  background-color: #1e1e1e;
  color: #f0f0f0;
  font-size: 1rem;
  width: 100%;
  transition: border-color 0.3s ease;
}

input:focus {
  border-color: #888;
  outline: none;
}

.input-error {
  border-color: #ff4d4f;
}

button[type="submit"] {
  width: 100%;
  padding: 0.6rem;
  background-color: #4caf50;
  border: none;
  color: white;
  font-size: 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s ease;
}

button[type="submit"]:hover {
  background-color: #45a049;
}

.error-message {
  margin-top: 1rem;
  color: #ff4d4f;
  font-weight: 600;
  text-align: center;
}

.success-message {
  color: #4caf50;
  margin-top: 1rem;
  text-align: center;
}

</style>
