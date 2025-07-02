<template>
  <div class="filters">
    <input
      type="text"
      v-model="filter.meaning"
      placeholder="Значение"
    />

    <HandwritingInput v-model="filter.spelling" />

    <input
      type="text"
      v-model="filter.reading"
      placeholder="Чтение"
    />

    <select v-model="filter.jlptLvl">
      <option value="">Уровень JLPT</option>
      <option v-for="lvl in [5,4,3,2,1,0]" :key="lvl" :value="lvl">N{{ lvl }}</option>
    </select>

    <select v-model="filter.kankenLvl">
      <option value="">Уровень 漢字検定</option>
      <option
        v-for="lvl in ['10','9','8','7','6','5','4','3','2.5','2','1.5','1','0']"
        :key="lvl"
        :value="lvl"
      >
        {{ lvl }}級
      </option>
    </select>

    <input
      type="number"
      v-model.number="filter.strokeCount"
      placeholder="Число черт"
      min="1"
    />

    <fieldset class="category-filter">
      <legend>Категория</legend>
      <label v-for="cat in categories" :key="cat">
        <input
          type="checkbox"
          :value="cat"
          v-model="filter.categories"
        />
        {{ cat }}
      </label>
    </fieldset>

    <button @click="applyFilters">Поиск</button>
    <button @click="resetFilters">Сброс</button>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import HandwritingInput from './HandwritingInput.vue'

const emit = defineEmits(['filter-changed'])

const categories = ['JOYO', 'JINMEIYO', 'OTHER']

const filter = reactive({
  jlptLvl: '',
  kankenLvl: '',
  spelling: '',
  reading: '',
  meaning: '',
  strokeCount: '',
  categories: []
})

const applyFilters = () => {
  const cleaned = Object.fromEntries(
    Object.entries(filter).filter(
      ([_, v]) => (typeof v === 'string' && v !== '') || (typeof v === 'number' && !isNaN(v)) || (Array.isArray(v) && v.length > 0)
    )
  )
  emit('filter-changed', cleaned)
}

const resetFilters = () => {
  Object.keys(filter).forEach((key) => {
    filter[key] = Array.isArray(filter[key]) ? [] : ''
  })
  emit('filter-changed', {})
}
</script>

<style scoped>
.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 24px;
  background-color: #2a2a2a;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #444;
}

.filters input,
.filters select {
  background-color: #1e1e1e;
  color: #eee;
  border: 1px solid #555;
  border-radius: 6px;
  padding: 8px 12px;
  font-size: 14px;
  transition: border-color 0.3s, background-color 0.3s;
}

.filters input:focus,
.filters select:focus {
  border-color: #4fc3f7;
  outline: none;
  background-color: #2c2c2c;
}

.category-filter {
  border: 1px solid #555;
  border-radius: 6px;
  padding: 10px 12px;
  color: #ccc;
}

.category-filter legend {
  font-weight: bold;
  color: #aaa;
  padding: 0 6px;
}

.category-filter label {
  margin-right: 10px;
  font-size: 14px;
}

.filters button {
  padding: 4px 10px;
  font-size: 14px;
  line-height: 1;
  border: 1px solid #444;
  background-color: #222;
  color: #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}

.filters button:hover {
  background-color: #333;
}


.filters button:last-of-type {
  background-color: #666;
  color: #fff;
}

.filters button:last-of-type:hover {
  background-color: #888;
}

</style>
