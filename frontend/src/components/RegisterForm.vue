<template>
  <form class="auth-form" @submit.prevent="handleRegister">
    <h2>Регистрация</h2>

    <div class="form-group">
      <label for="username">Логин</label>
      <input id="username" v-model="username" type="text" required />
    </div>

    <div class="form-group">
      <label for="password">Пароль</label>
      <input id="password" v-model="password" type="password" required />
    </div>

    <button type="submit">Зарегистрироваться</button>

    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </form>
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

const handleRegister = async () => {
  errorMessage.value = ''
  successMessage.value = ''

  try {
    if (auth.isAuthenticated) {
      auth.logout()
    }

    await auth.register(username.value, password.value)
    successMessage.value = 'Регистрация успешна! Вы вошли в систему.'
    setTimeout(() => router.push('/'), 1500)
  } catch (e) {
      console.error('Ошибка регистрации:', e)
      errorMessage.value =
      e.response?.data?.message ||
      (e.response?.status === 409
        ? 'Пользователь с таким логином уже существует'
        : null) ||
      e.message ||
      'Неизвестная ошибка'
  }

}
</script>

<style scoped>

.auth-form {
  max-width: 400px;
  margin: 40px auto;
  padding: 2rem;
  background-color: #2b2b2b;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.4);
  color: #f0f0f0;
}

.auth-form h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  color: #ffffff;
}

.form-group {
  margin-bottom: 1.2rem;
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 0.5rem;
  font-weight: bold;
}

input {
  padding: 0.6rem;
  border: 1px solid #555;
  border-radius: 4px;
  background-color: #1e1e1e;
  color: #f0f0f0;
  font-size: 1rem;
  width: 100%;
}

button {
  width: 100%;
  padding: 0.7rem;
  background-color: #444;
  color: #f0f0f0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.2s;
  margin-top: 10px;
}

button:hover {
  background-color: #666;
}

.success-message {
  margin-top: 1rem;
  color: #4caf50;
  text-align: center;
}

.error-message {
  margin-top: 1rem;
  color: #f44336;
  text-align: center;
}

</style>

