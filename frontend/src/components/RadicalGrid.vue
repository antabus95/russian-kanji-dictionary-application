<template>
  <div class="radical-grid">
    <div
      v-for="radical in radicalItemsWithCreate"
      :key="radical.id"
      class="radical-tile"
      @click="handleClick(radical.id)"
      @auxclick="handleAuxClick($event, radical.id)"
      style="user-select: none; cursor: pointer;"
    >
      <div class="radical-character">{{ radical.id === '__create__' ? '➕' :　radical.spelling }}</div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue'
import { computed } from 'vue'

const radicalItemsWithCreate = computed(() => {
  return [
    { id: '__create__' },  
    ...props.radicalList
  ]
})

const emit = defineEmits(['radical-clicked', 'radical-middle-clicked', 'create-clicked'])

const props = defineProps({
  radicalList: {
    type: Array,
    required: true
  }
})

const handleClick = (id) => {
  if (id === '__create__') {
    emit('create-clicked')
  } else {
    emit('radical-clicked', id)
  }
}

const handleAuxClick = (event, id) => {
  if (event.button === 1) {
    event.preventDefault()
    if (id === '__create__') {
      window.open('/radicals/create', '_blank')
    } else {
      emit('radical-middle-clicked', id)
    }    
  }
}

</script>

<style scoped>

.radical-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 12px;
}

.radical-tile {
  background: #3f3f3f;
  padding: 10px;
  text-align: center;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}

.radical-tile:hover {
  background: #525252;
}

.radical-character {
  font-size: 28px;
  color: #f0f0f0;
}

</style>
