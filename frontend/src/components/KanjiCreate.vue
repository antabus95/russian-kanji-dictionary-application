<template>
  <div>
    <h2>Создание нового иероглифа</h2>
    <KanjiEditForm
      :initialKanji="blankKanji"
      @save="createKanji"
      @back="goBack"
    />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import KanjiEditForm from '@/components/KanjiEditForm.vue'
import axios from 'axios'

const router = useRouter()

const blankKanji = {
  spelling: '',
  meanings: '',
  etymology: '',
  strokeCount: 1,
  category: 'OTHER',
  radicalForm: { id: null, spelling: '' },
  jlptLvl: 0,
  kankenLvl: 0,
  jisCode: '',
  unicode: '',
  readings: [],
  baseMeanings: [],
  traditionalForms: [],
  simplifiedForms: [],
  alternativeForms: []
}

const createKanji = async (dto) => {
  try {
    await axios.post('/api/v1/kanji', dto)
    router.push('/')
  } catch (e) {
    alert('Ошибка при создании: ' + (e.response?.data?.message || e.message))
  }
}

const goBack = () => {
  router.back()
}
</script>

