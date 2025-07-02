<template>
  <div class="radical-create-container">
    <h2 class="form-title">Добавление нового ключа</h2>
    <div class="form-wrapper">
      <div v-if="errorMessage" class="error-popup">{{ errorMessage }}</div>
      <RadicalEditForm
        :initialRadical="blankRadical"
        @save="createRadical"
        @back="goBack"
      />
    </div>
  </div>
</template>


<script setup>
import { useRouter } from 'vue-router'
import RadicalEditForm from '@/components/RadicalEditForm.vue'
import axios from 'axios'
import {ref} from 'vue'

const errorMessage = ref('')
const router = useRouter()

const blankRadical = {
  number: 0,
  spelling: '',
  strokeCount: 1,
  radicalForms: [],
}

const createRadical = async (dto) => {
  errorMessage.value = ''
  try {
    await axios.post('/api/v1/radicals', dto)
    router.push('/radicals')
  } catch (e) {
    if (e.response?.status === 409) {
      errorMessage.value = "Ключ с таким номером уже существует"
    } else {
      errorMessage.value = 'Ошибка при создании: ' + (e.response?.data?.message || e.message)
    }
    setTimeout(() => errorMessage.value = '', 5000)
  }
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.radical-create-container {
  padding: 2rem;
  max-width: 800px;
  margin: 0 auto;
  background-color: #1e1e1e;
  color: #f0f0f0;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0,0,0,0.4);
}

.form-title {
  text-align: center;
  font-size: 1.8rem;
  color: #ffffff;
  margin-bottom: 2rem;
}

.form-wrapper {
  background-color: #2a2a2a;
  padding: 2rem;
  border-radius: 8px;
}

.error-popup {
  background-color: #f44336;
  color: #fff;
  padding: 10px 15px;
  border-radius: 4px;
  margin-bottom: 1rem;
  text-align: center;
}

</style>
