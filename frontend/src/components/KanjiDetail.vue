<template>
  <div v-if="kanji">
    <p>№{{kanji.id}}</p>
    <h2>Кандзи: {{ kanji.spelling }}</h2>
    <p><strong>Значения:</strong> {{ kanji.meanings }}</p>
    <p><strong>Этимология:</strong> {{ kanji.etymology }}</p>
    <p><strong>Число черт:</strong> {{ kanji.strokeCount }}</p>
    <p><strong>Ключ (радикал):</strong> {{ kanji.radicalForm?.spelling || '—' }}</p>
    <p><strong>Категория:</strong> {{ kanji.category }}</p>
    <p><strong>JLPT:</strong> {{ kanji.jlptLvl }}</p>
    <p><strong>Kanken:</strong> {{ kanji.kankenLvl }}</p>
    <p><strong>JIS Code:</strong> {{ kanji.jisCode }}</p>
    <p><strong>Unicode:</strong> {{ kanji.unicode }}</p>

    <div>
      <h3>Чтения:</h3>
      <ul>
        <li v-for="reading in orderedReadings" :key="reading.text">
          {{ reading.readingType }}: {{ reading.text }} <span v-if="reading.joyo">（常用）</span>
          <span v-if="reading.chineseReadingCategory">[{{ reading.chineseReadingCategory }}]</span>
        </li>
      </ul>
    </div>

    <div>
      <h3>Базовое значение:</h3>
      <ul>
        <li v-for="baseMeaning in baseMeanings" :key="baseMeaning.meaning">
          {{ baseMeaning.meaning }}
        </li>
      </ul>
    </div>

    <div>
      <p v-if="kanji.traditionalForms?.length">
        <strong>Традиционные формы:</strong>
        <span v-for="form in kanji.traditionalForms" :key="form.id">
        <a href="#" @click.prevent="goToKanji(form.id)">{{ form.spelling || form.id }}</a>
        </span>
      </p>
      <p v-if="kanji.simplifiedForms?.length">
        <strong>Упрощённые формы:</strong>
        <span v-for="form in kanji.simplifiedForms" :key="form.id">
          <a href="#" @click.prevent="goToKanji(form.id)">{{ form.spelling || form.id }}</a>
        </span>
      </p>
      <p v-if="kanji.alternativeForms?.length">
        <strong>Альтернативные формы:</strong>
        <span v-for="form in kanji.alternativeForms" :key="form.id">
          <a href="#" @click.prevent="goToKanji(form.id)">{{ form.spelling || form.id }}</a>
        </span>
      </p>
    </div>

    <button @click="$emit('back')">Назад</button>
    <button @click="$emit('edit')">✏️ Редактировать</button>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'

const props = defineProps(['kanjiId'])
const emit = defineEmits(['back', 'edit', 'navigate'])

const kanji = ref(null)

const orderedReadings = ref([])
const baseMeanings = ref([])

const loadKanji = async () => {
  try {
    const response = await axios.get(`/api/v1/kanji/${props.kanjiId}`)
    kanji.value = response.data
    orderedReadings.value = [...kanji.value.readings].sort((a, b) => {
      const order = { ON: 1, KUN: 2, NANORI: 3 }
      return (order[a.readingType] || 4) - (order[b.readingType] || 4)
    })
    baseMeanings.value = [...kanji.value.baseMeanings]
  } catch(error) {
    console.error('Не удалось загрузить иероглиф', error)
  }
  
}

const goToKanji = (id) => {
  emit('navigate', id)
}

watch(() => props.kanjiId, loadKanji, { immediate: true })
</script>
