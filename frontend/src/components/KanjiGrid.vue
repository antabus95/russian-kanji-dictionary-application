<template>
  <div class="kanji-grid">
    <div
      v-for="kanji in kanjiItemsWithCreate"
      :key="kanji.id"
      class="kanji-tile"
      @click="handleClick(kanji.id)"
      @auxclick="handleAuxClick($event, kanji.id)"
      style="user-select: none; cursor: pointer;"
    >
      <div class="kanji-character">{{ kanji.id === '__create__' ? '➕' :　kanji.spelling }}</div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue'
import { computed } from 'vue'

const kanjiItemsWithCreate = computed(() => {
  return [
    { id: '__create__' },  
    ...props.kanjiList
  ]
})

const emit = defineEmits(['kanji-clicked', 'kanji-middle-clicked', 'create-clicked'])

const props = defineProps({
  kanjiList: {
    type: Array,
    required: true
  }
})

const handleClick = (id) => {
  if (id === '__create__') {
    emit('create-clicked')
  } else {
    emit('kanji-clicked', id)
  }
}

const handleAuxClick = (event, id) => {
  if (event.button === 1) {
    event.preventDefault()
    if (id === '__create__') {
      window.open('/kanji/create', '_blank')
    } else {
      emit('kanji-middle-clicked', id)
    }    
  }
}

</script>

<style scoped>
.kanji-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 12px;
  padding: 8px;
}

.kanji-tile {
  background: #2c2c2c;
  padding: 16px;
  text-align: center;
  border-radius: 6px;
  border: 1px solid #444;
  transition: background 0.2s, transform 0.2s;
  color: #f0f0f0;
}

.kanji-tile:hover {
  background: #3a3a3a;
  transform: translateY(-2px);
}

.kanji-character {
  font-size: 32px;
  color: #ffffff;
  user-select: none;
}

</style>
