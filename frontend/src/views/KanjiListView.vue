<template>
  <div class="kanji-page-wrapper">
    <KanjiFilters @filter-changed="onFilterChanged" />
    
    <KanjiGrid
      v-if="!selectedKanjiId"
      :kanjiList="kanjiList"
      @kanji-clicked="onKanjiClicked"
      @create-clicked="goToKanjiCreate"
      @kanji-middle-clicked="onKanjiMiddleClicked"
    />

    <KanjiDetail
      v-else-if="selectedMode === 'view'"
      :kanjiId="selectedKanjiId"
      @back="selectedKanjiId = null"
      @edit="selectedMode = 'edit'"
      @navigate="selectedKanjiId = $event"
    />

    <KanjiEdit
      v-else-if="selectedMode === 'edit'"
      :kanjiId="selectedKanjiId"
      @back="selectedMode = 'view'"
      @delete="handleKanjiDeleted"
    />

    <Pagination
      v-if="!selectedKanjiId"
      :current-page="page"
      :total-pages="totalPages"
      @page-changed="onPageChanged"
    />
  </div>
</template>

<script setup>
import { ref, watchEffect, onMounted, defineAsyncComponent } from 'vue'
import axios from 'axios'
import qs from 'qs'
import { useRoute, useRouter } from 'vue-router'

import KanjiGrid from '@/components/KanjiGrid.vue'
import KanjiFilters from '@/components/KanjiFilters.vue'
import Pagination from '@/components/Pagination.vue'

const KanjiDetail = defineAsyncComponent(() => import('@/components/KanjiDetail.vue'))
const KanjiEdit = defineAsyncComponent(() => import('@/components/KanjiEdit.vue'))

const kanjiList = ref([])
const page = ref(0)
const size = 44
const totalPages = ref(0)
const filters = ref({})
const selectedKanjiId = ref(null)
const selectedMode = ref('view') 

const route = useRoute()
const router = useRouter()


const fetchKanji = async () => {
  if (selectedKanjiId.value) return
  const params = { page: page.value, size, ...filters.value }
  const response = await axios.get('/api/v1/kanji', {
    params,
    paramsSerializer: (p) => qs.stringify(p, { arrayFormat: 'repeat' })
  })
  kanjiList.value = response.data.content
  totalPages.value = response.data.totalPages
}

watchEffect(fetchKanji)

onMounted(() => {
  const initialId = route.query.kanjiId
  const initialMode = route.query.mode || 'view'
  if (initialId) {
    selectedKanjiId.value = initialId
    selectedMode.value = initialMode
  }
})

watchEffect(() => {
  const query = { ...route.query }
  if (selectedKanjiId.value) {
    query.kanjiId = selectedKanjiId.value
    query.mode = selectedMode.value
  } else {
    delete query.kanjiId
    delete query.mode
  }
  router.replace({ query })
})


const onFilterChanged = (newFilters) => { filters.value = newFilters; page.value = 0 }
const onPageChanged = (newPage) => { page.value = newPage }
const onKanjiClicked = (id) => { selectedKanjiId.value = id }

const onKanjiMiddleClicked = (id) => {
  const url = new URL(window.location.href)
  url.searchParams.set('kanjiId', id)
  window.open(url.toString(), '_blank')
}

const goToKanjiCreate = () => {
  router.push('/kanji/create')  
}

const handleKanjiDeleted = () => {
  selectedKanjiId.value = null
  selectedMode.value = 'view'
  fetchKanji()
}

</script>

<style scoped>

.kanji-page-wrapper {
  max-width: 1200px; 
  margin: 0 auto; 
  padding: 20px; 
  box-sizing: border-box;
}

</style>