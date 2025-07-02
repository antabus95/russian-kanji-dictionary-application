<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const kanji = ref(null)

const joinForms = (arr) => {
  return (arr || []).map(f => f.spelling || f.id).join(', ')
}

const orderedReadings = computed(() => {
  if (!kanji.value?.readings) return []
  const order = { ON: 1, KUN: 2, NANORI: 3 }
  return [...kanji.value.readings].sort((a, b) =>
    (order[a.readingType] || 4) - (order[b.readingType] || 4)
  )
})

onMounted(async () => {
  const { id } = route.params
  const response = await axios.get(`/api/v1/kanji/${id}`)
  kanji.value = response.data
})
</script>

<template>
  <div v-if="kanji">
    <h2>{{ kanji.spelling }} — {{ kanji.meanings }}</h2>
    <p><strong>Строк:</strong> {{ kanji.strokeCount }}</p>
    <p><strong>Категория:</strong> {{ kanji.category }}</p>
    <p><strong>JLPT:</strong> {{ kanji.jlptLvl }}</p>
    <p><strong>Kanken:</strong> {{ kanji.kankenLvl }}</p>
    <p><strong>JIS Code:</strong> {{ kanji.jisCode }}</p>
    <p><strong>Unicode:</strong> {{ kanji.unicode }}</p>

    <div v-if="orderedReadings.length">
      <h3>Чтения:</h3>
      <ul>
        <li v-for="reading in orderedReadings" :key="reading.text">
          {{ reading.readingType }}: {{ reading.text }}
          <span v-if="reading.joyo">（常用）</span>
          <span v-if="reading.chineseReadingCategory">[{{ reading.chineseReadingCategory }}]</span>
        </li>
      </ul>
    </div>

    <div>
      <h3>Формы:</h3>
      <p><strong>Традиционные:</strong> {{ joinForms(kanji.traditionalForms) }}</p>
      <p><strong>Упрощённые:</strong> {{ joinForms(kanji.simplifiedForms) }}</p>
      <p><strong>Альтернативные:</strong> {{ joinForms(kanji.alternativeForms) }}</p>
    </div>

    <button @click="$router.back()">Назад</button>
  </div>
</template>


