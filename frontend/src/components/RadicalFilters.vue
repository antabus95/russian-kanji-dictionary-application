<template>
  <div class="filters">
    <input
      type="number"
      v-model="filter.number"
      placeholder="Номер ключа"
      min="1"
    />
    <input
      type="text"
      v-model="filter.spelling"
      placeholder="Начертание ключа"
    />
    <input
      type="number"
      v-model.number="filter.strokeCount"
      placeholder="Число черт"
      min="1"
    />

    <button @click="applyFilters">🔍 </button>
    <button @click="resetFilters">Reset</button>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const emit = defineEmits(['filter-changed'])

const filter = reactive({
  spelling: '',
  number: '',
  strokeCount: ''
})

const applyFilters = () => {
  const cleaned = Object.fromEntries(
    Object.entries(filter).filter(
      ([_, v]) => (typeof v === 'string' && v !== '') || (typeof v === 'number' && !isNaN(v)) || (Array.isArray(v) && v.length > 0)
    )
  )
  emit('filter-changed', cleaned)
}

const resetFilters = () => {
  Object.keys(filter).forEach((key) => {
    filter[key] = Array.isArray(filter[key]) ? [] : ''
  })
  emit('filter-changed', {})
}
</script>

<style scoped>

.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}
.filters input,
.filters select {
  padding: 6px;
}

</style>
