<template>
  <div v-if="kanji">
    <KanjiEditForm
      :initialKanji="kanji"
      @save="updateKanji"
      @back="goBack"
      @delete="deleteKanji"
    />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import KanjiEditForm from '@/components/KanjiEditForm.vue'

const router = useRouter()

const props = defineProps({
  kanjiId: {
    type: [String, Number],
    required: true
  }
})

const emit = defineEmits(['back', 'delete'])

const kanji = ref(null)

const loadKanji = async () => {
  try {
    const { data } = await axios.get(`/api/v1/kanji/${props.kanjiId}`)
    data.alternativeForms ||= []
    data.traditionalForms ||= []
    data.simplifiedForms ||= []
    data.readings ||= []
    data.baseMeanings ||= []
    data.radicalForm ||= { id: null, spelling: '' }

    data.readings.forEach(r => {
      if (r.chineseReadingCategory === undefined) {
        r.chineseReadingCategory = ''
      }
      if (r.joyo === undefined) {
        r.joyo = false
      }
    })

    kanji.value = data
  } catch (e) {
    alert('Ошибка загрузки иероглифа: ' + (e.response?.data?.message || e.message))
  }
}

const updateKanji = async (dto) => {
  try {
    await axios.put(`/api/v1/kanji/${dto.id}`, dto)
    alert('Сохранено!')
    router.push('/')
  } catch (e) {
    alert('Ошибка при сохранении: ' + (e.response?.data?.message || e.message))
  }
}

const goBack = () => emit('back')

const deleteKanji = async (id) => {
  try {
    await axios.delete(`/api/v1/kanji/${id}`)
    alert('Иероглиф удалён.')
    emit('delete')
    emit('back')
  } catch (e) {
    alert('Ошибка при удалении: ' + (e.response?.data?.message || e.message))
  }
}

watch(() => props.kanjiId, loadKanji, { immediate: true })
</script>
