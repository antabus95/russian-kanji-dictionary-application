<template>
  <div v-if="radical">
    <div v-if="errorMessage" class="error-popup">{{ errorMessage }}</div>
    <RadicalEditForm
      v-if="radical"
      :key="radical.id"
      :initialRadical="radical"
      @save="updateRadical"
      @back="goBack"
      @delete="deleteRadical"
    />
  </div>
</template>

<script setup>
import { ref, watch, reactive } from 'vue'
import axios from 'axios'
import RadicalEditForm from '@/components/RadicalEditForm.vue'

const errorMessage = ref('')

const props = defineProps({
  radicalId: {
    type: [String, Number],
    required: true
  }
})

const emit = defineEmits(['back', 'delete'])

const radical = ref(null)

const loadRadical = async () => {
  try {
    const { data } = await axios.get(`/api/v1/radicals/${props.radicalId}`)
    data.radicalForms ||= []
    radical.value = data
  } catch (e) {
    alert('Ошибка загрузки ключа: ' + (e.response?.data?.message || e.message))
  }
}

const updateRadical = async (dto) => {
  try {
    await axios.put(`/api/v1/radicals/${dto.id}`, dto)
    const { data } = await axios.get(`/api/v1/radicals/${radical.value.id}`)
    radical.value = data
    alert('Сохранено!')
  } catch (e) {
    if (e.response?.status === 409) {
      errorMessage.value = "Ключ с таким номером уже существует"
    } else {
      errorMessage.value = 'Ошибка при сохранении: ' + (e.response?.data?.message || e.message)
    }
    setTimeout(() => errorMessage.value = '', 5000)
  }
}

const goBack = () => emit('back')

const deleteRadical = async (id) => {
  try {
    await axios.delete(`/api/v1/radicals/${id}`)
    alert('Ключ удалён.')
    emit('delete')
    emit('back')
  } catch (e) {
    alert('Ошибка при удалении: ' + (e.response?.data?.message || e.message))
  }
}

watch(() => props.radicalId, loadRadical, { immediate: true })

</script>

<style scoped>

.error-popup {
  background-color: #f44336;
  color: #fff;
  padding: 10px 15px;
  border-radius: 4px;
  margin-bottom: 1rem;
  text-align: center;
}

</style>