<template>
  <div v-if="radical" class="radical-detail-container">
    <div class="radical-info">
      <p class="radical-number">Ключ №{{ radical.number }}</p>
      <h2 class="radical-name">{{ radical.name }}</h2>
      <div class="radical-main">{{ radical.spelling }}</div>
      <p><strong>Число черт:</strong> {{ radical.strokeCount }}</p>

      <div class="radical-forms">
        <h3>Формы:</h3>
        <ul>
          <li v-for="form in orderedForms" :key="form.spelling">
            {{ form.formName }}: {{ form.spelling }}
          </li>
        </ul>
      </div>

      <div class="radical-buttons">
        <button @click="$emit('back')">← Назад</button>
        <button v-if="isAdmin" @click="$emit('edit')">✏️ Редактировать</button>
      </div>
    </div>

    <div class="kanji-section">
      <h3>Иероглифы с этим ключом:</h3>
      <div v-if="kanjiList.length > 0" class="kanji-grid">
        <div
          v-for="kanji in kanjiList"
          :key="kanji.id"
          class="kanji-tile"
          @click="goToKanji(kanji.id)"
        >
          <div class="kanji-char">{{ kanji.spelling }}</div>
          <div class="kanji-info">Черт: {{ kanji.strokeCount }}</div>
        </div>
      </div>
      <div v-else class="kanji-grid-empty">
        <p class="empty-message">Нет иероглифов с этим ключом.</p>
      </div>

      <div v-if="kanjiPage.totalPages > 1" class="pagination">
        <button :disabled="currentPage === 0" @click="goToPage(currentPage - 1)">← Назад</button>
        <span>Страница {{ currentPage + 1 }} из {{ kanjiPage.totalPages }}</span>
        <button :disabled="kanjiPage.last" @click="goToPage(currentPage + 1)">Вперёд →</button>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

const props = defineProps(['radicalId'])
const emit = defineEmits(['back', 'edit', 'navigate'])

const auth = useAuthStore()
const radical = ref(null)
const orderedForms = ref([])
const kanjiList = ref([])
const kanjiPage = ref({})
const currentPage = ref(0)
const pageSize = 18

const isAdmin = computed(() => {
  return auth.isAuthenticated && auth.user?.role === 'ROLE_ADMIN'
})

const loadRadical = async () => {
  try {
    const response = await axios.get(`/api/v1/radicals/${props.radicalId}`)
    radical.value = response.data
    orderedForms.value = [...(radical.value.radicalForms || [])]
    await loadKanji(0)
  } catch (error) {
    console.error("Ошибка при загрузке ключа:", error)
    radical.value = null
    orderedForms.value = []
    kanjiList.value = []
  }
}

const loadKanji = async (page = 0) => {
  try {
    const response = await axios.get(`/api/v1/radicals/${props.radicalId}/kanji`, {
      params: {
        page, size: pageSize, sort: 'strokeCount,asc'
      }
    })
    kanjiList.value = response.data.content || []
    kanjiPage.value = response.data
    currentPage.value = response.data.number
  } catch (error) {
    console.error("Ошибка при загрузке иероглифов:", error)
    kanjiList.value = []
  }
}

const goToKanji = (id) => {
  emit('navigate', { type: 'kanji', id })
}

const goToPage = (page) => {
  if (page >= 0 && page < kanjiPage.value.totalPages) {
    loadKanji(page)
  }
}

watch(() => props.radicalId, loadRadical, { immediate: true })
</script>

<style scoped>

.radical-detail-container {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  padding: 2rem;
  background-color: #1e1e1e;
  color: #f0f0f0;
  border-radius: 8px;
}

.radical-info {
  flex: 1;
  min-width: 260px;
  max-width: 350px;
  padding: 1rem;
  background-color: #2a2a2a;
  border-radius: 8px;
}

.radical-number {
  font-size: 1.1rem;
  color: #ccc;
}

.radical-name {
  margin: 0.5rem 0;
  font-size: 1.3rem;
  color: #ffffff;
}

.radical-main {
  font-size: 4rem;
  font-weight: bold;
  text-align: center;
  margin: 1rem 0;
  color: #ffffff;
}

.radical-forms h3 {
  margin-top: 1.2rem;
  font-size: 1.1rem;
  color: #f0f0f0;
}

.radical-forms ul {
  padding-left: 1rem;
  margin-top: 0.5rem;
}

.radical-forms li {
  margin-bottom: 0.4rem;
  font-size: 0.95rem;
  color: #dddddd;
}

.radical-buttons {
  margin-top: 1.5rem;
  display: flex;
  gap: 10px;
}

.radical-buttons button {
  padding: 0.5rem 1rem;
  background-color: #4caf50;
  border: none;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
}

.radical-buttons button:hover {
  background-color: #45a049;
}

.kanji-section {
  flex: 2;
  min-width: 300px;
  padding: 1rem;
}

.kanji-section h3 {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.kanji-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px; 
  margin-top: 10px;
  align-items: flex-start; 
}

.kanji-tile {
  border: 1px solid #ccc;
  padding: 8px;
  width: 80px;
  height: auto; 
  box-sizing: border-box;
  text-align: center;
  cursor: pointer;
  border-radius: 4px;
  background: #1e1e1e;
  color: #f0f0f0;
  transition: background 0.2s;
  display: flex;
  flex-direction: column;
  justify-content: space-between; 
}

.kanji-tile:hover {
  background: #3a3a3a;
}

.kanji-char {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.kanji-info {
  font-size: 12px;
  color: #999;
}

.pagination {
  margin-top: 15px;
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
}

.pagination button {
  padding: 5px 10px;
  border: 1px solid #aaa;
  background: #333;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: default;
}

.kanji-grid-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 240px;
  margin-top: 10px;
  background-color: #1e1e1e;
  border: 1px dashed #444;
  border-radius: 6px;
}

.empty-message {
  color: #888;
  font-style: italic;
  font-size: 1rem;
}

</style>