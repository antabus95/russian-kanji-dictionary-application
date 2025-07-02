<template>
  <div class="kanji-grid">
    <div
      v-for="kanji in kanjiItems"
      :key="kanji.id"
      class="kanji-tile"
      @click="handleClick(kanji.id)"
      @auxclick="handleAuxClick($event, kanji.id)"
      style="user-select: none; cursor: pointer;"
    >
      <div class="kanji-character">{{ kanji.spelling }}</div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue'
import { computed } from 'vue'

const kanjiItems = computed(() => {
  return [...props.kanjiList]
})

const emit = defineEmits(['kanji-clicked', 'kanji-middle-clicked'])

const props = defineProps({
  kanjiList: {
    type: Array,
    required: true
  }
})

const handleClick = (id) => { emit('kanji-clicked', id)}

const handleAuxClick = (event, id) => {
  if (event.button === 1) {
        event.preventDefault()
        emit('kanji-middle-clicked', id)
    }    
}

</script>

<style scoped>

.kanji-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 12px;
}
.kanji-tile {
  background: #3f3f3f;
  padding: 10px;
  text-align: center;
  border-radius: 4px;
}
.kanji-character {
  font-size: 32px;
}
.kanji-meaning {
  font-size: 14px;
  color: #555;
}

</style>
