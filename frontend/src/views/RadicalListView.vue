<template>
  <h2 v-if="!selectedRadicalId" class="page-title">Список иероглифических ключей:</h2>
  <div class="radical-grid-wrapper">

    <RadicalGrid
      v-if="!selectedRadicalId"
      :radicalList="radicalList"
      @radical-clicked="onRadicalClicked"
      @create-clicked="goToRadicalCreate"
      @radical-middle-clicked="onRadicalMiddleClicked"
    />

    <RadicalDetail
      v-else-if="selectedMode === 'view'"
      :radicalId="selectedRadicalId"
      @back="selectedRadicalId = null"
      @edit="selectedMode = 'edit'"
      @navigate="handleNavigate"
    />

    <RadicalEdit
      v-else-if="selectedMode === 'edit'"
      :radicalId="selectedRadicalId"
      @back="selectedMode = 'view'"
      @delete="handleRadicalDeleted"
    />

    <KanjiDetail
      v-else-if="selectedMode === 'kanji'"
      :kanjiId="selectedKanjiId"
      @back="selectedMode = 'view'"
      @navigate="openKanji"
      @edit="selectedMode = 'kanji-edit'"
    />

    <KanjiEdit
      v-else-if="selectedMode === 'kanji-edit'"
      :kanjiId="selectedKanjiId"
      @back="selectedMode = 'kanji'"
      @delete="handleKanjiDeleted"
    />

  </div>
</template>

<script setup>
import { ref, watchEffect, onMounted, defineAsyncComponent } from 'vue'
import axios from 'axios'
import qs from 'qs'
import { useRoute, useRouter } from 'vue-router'
import RadicalGrid from '@/components/RadicalGrid.vue'

const RadicalDetail = defineAsyncComponent(() => import('@/components/RadicalDetail.vue'))
const RadicalEdit = defineAsyncComponent(() => import('@/components/RadicalEdit.vue'))
const KanjiDetail = defineAsyncComponent(() => import('@/components/KanjiDetail.vue'))
const KanjiEdit = defineAsyncComponent(() => import('@/components/KanjiEdit.vue'))

const radicalList = ref([])
const page = ref(0)
const size = 215
const totalPages = ref(0)
const filters = ref({})
const selectedRadicalId = ref(null)
const selectedMode = ref('view') 
const selectedKanjiId = ref(null)

const route = useRoute()
const router = useRouter()

const fetchRadical = async () => {
  if (selectedRadicalId.value) return
  const params = { page: page.value, size, ...filters.value }
  const response = await axios.get('/api/v1/radicals', {
    params,
    paramsSerializer: (p) => qs.stringify(p, { arrayFormat: 'repeat' })
  })
  radicalList.value = response.data.content
  totalPages.value = response.data.totalPages
}

watchEffect(fetchRadical)

onMounted(() => {
  const initialId = route.query.radicalId
  const initialMode = route.query.mode || 'view'
  if (initialId) {
    selectedRadicalId.value = initialId
    selectedMode.value = initialMode
  }
})

watchEffect(() => {
  const query = { ...route.query }
  if (selectedRadicalId.value) {
    query.radicalId = selectedRadicalId.value
    query.mode = selectedMode.value
  } else {
    delete query.radicalId
    delete query.mode
  }
  router.replace({ query })
})

const onRadicalClicked = (id) => { selectedRadicalId.value = id }
const onRadicalMiddleClicked = (id) => {
  const url = new URL(window.location.href)
  url.searchParams.set('radicalId', id)
  window.open(url.toString(), '_blank')
}
const goToRadicalCreate = () => {
  router.push('/radicals/create')  
}

const handleRadicalDeleted = () => {
  selectedRadicalId.value = null
  selectedMode.value = 'view'
  fetchRadical()
}

const openKanji = (kanjiId) => {
  selectedKanjiId.value = kanjiId
  selectedMode.value = 'kanji'
}

const handleNavigate = (payload) => {
  if (typeof payload === 'object' && payload.type === 'kanji') {
    selectedKanjiId.value = payload.id
    selectedMode.value = 'kanji'
  } else {
    selectedRadicalId.value = payload.id || payload
    selectedMode.value = 'view'
  }
}

const handleKanjiDeleted = () => {
  selectedMode.value = 'view'
  selectedKanjiId.value = null
}

</script>

<style scoped>

.page-title {
  text-align: center;
  color: #f0f0f0;
  margin: 2rem 0 1rem;
  font-size: 1.8rem;
}

.radical-grid-wrapper {
  box-sizing: border-box;
  padding-left: 300px;
  padding-right: 300px;
  max-width: 1600px;
  margin: 0 auto;
  width: 100%;
}

</style>
