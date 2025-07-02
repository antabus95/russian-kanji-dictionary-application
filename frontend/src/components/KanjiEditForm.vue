<template>
  <div v-if="kanji">
    <p v-if="kanji.id">№{{ kanji.id }}</p>
    <p><strong>Кандзи: </strong> <input v-model="kanji.spelling" /></p>
    <p><strong>Значения: </strong><textarea v-model="kanji.meanings" /></p>
    <p><strong>Этимология: </strong><textarea v-model="kanji.etymology" /></p>
    <p><strong>Число черт: </strong><input type="number" v-model="kanji.strokeCount" /></p>

    <p><strong>Ключ: </strong><input v-model="kanji.radicalForm.spelling" readonly />
      <button type="button" @click="openRadicalPicker">Выбрать</button></p>

    <div v-if="showRadicalPicker" class="radical-picker-overlay">
      <div class="radical-picker">
        <div class="header">
          <strong>Выберите ключ</strong>
          <button @click="showRadicalPicker = false">✖</button>
        </div>
        <div class="tiles">
          <button
            v-for="radical in radicalForms"
            :key="radical.id"
            class="tile"
            @click="selectRadical(radical)"
          >
            {{ radical.spelling }}
          </button>
        </div>
      </div>
    </div>

    <p><strong>Категория: </strong>
      <select v-model="kanji.category">
        <option v-for="opt in categoryOptions" :key="opt.value" :value="opt.value">
          {{ opt.label }}
        </option>
      </select>
    </p>

    <p><strong>Уровень JLPT: </strong>
      <select v-model="kanji.jlptLvl">
        <option v-for="opt in jlptLvlOptions" :key="opt.value" :value="opt.value">
          {{ opt.label }}
        </option>
      </select>
    </p>

    <p><strong>Уровень 漢字検定: </strong>
      <select v-model="kanji.kankenLvl">
        <option v-for="opt in kankenLvlOptions" :key="opt.value" :value="opt.value">
          {{ opt.label }}
        </option>
      </select>
    </p>

    <p><strong>JIS Code: </strong><input v-model="kanji.jisCode" /></p>
    <p><strong>Unicode: </strong><input v-model="kanji.unicode" /></p>

    <div>
      <h3>Чтения:</h3>

      <div v-for="(reading, index) in kanji.readings" :key="index" class="reading-row">
        <select v-model="reading.readingType">
          <option disabled value="">Тип</option>
          <option value="ON">音読み (Он)</option>
          <option value="KUN">訓読み (Кун)</option>
          <option value="NANORI">名乗り (Нанори)</option>
        </select>

        <input v-model="reading.text" placeholder="Чтение" />

        <input type="checkbox" v-model="reading.joyo" />
        <label>常用</label>

        <select v-model="reading.chineseReadingCategory" :disabled="reading.readingType !== 'ON'">
          <option disabled value="">Тип онъёми</option>
          <option v-for="opt in chineseReadingCategoryOptions" :key="opt.value" :value="opt.value">
              {{ opt.label }}
          </option>
        </select>

        <button type="button" @click="removeReading(index)">🗑 Удалить</button>
      </div>

      <button type="button" @click="addReading">➕ Добавить чтение</button>
    </div>


    <div>
      <h3>Базовые значения:</h3>
      <div v-for="(baseMeaning, index) in kanji.baseMeanings" :key="index" class="baseMeaning-row">
        <input v-model="baseMeaning.meaning" placeholder="Значение" />
        <button type="button" @click="removeBaseMeaning(index)">🗑 Удалить</button>
      </div>
      <button type="button" @click="addBaseMeaning">➕ Добавить значение</button>
    </div>


    <div v-if="kanji">
      <KanjiFormList
        v-if="kanji && kanji.alternativeForms"
        v-model="kanji.alternativeForms"
        title="Альтернативные формы"
      />

      <KanjiFormList
        v-if="kanji && kanji.traditionalForms"
        v-model="kanji.traditionalForms"
        title="Традиционные формы"
      />

      <KanjiFormList
        v-if="kanji && kanji.simplifiedForms"
        v-model="kanji.simplifiedForms"
        title="Упрощённые формы"
      />
    </div>
    

    <button @click="validateAndSave">💾 Сохранить</button>
    <button @click="$emit('back')">Назад</button>
    <button v-if="kanji.id" @click="confirmAndDelete" style="color: red">🗑 Удалить иероглиф</button>
  </div>
</template>

<script setup>
import { ref, watchEffect } from 'vue'
import axios from 'axios'
import KanjiFormList from '@/components/KanjiFormList.vue'

const props = defineProps({
  initialKanji: Object,
})
const emit = defineEmits(['save', 'back', 'delete'])
const kanji = ref(JSON.parse(JSON.stringify(props.initialKanji))) 
const showRadicalPicker = ref(false)
const radicalForms = ref([])

const categoryOptions = [
  { label: 'Jōyō (常用)', value: 'JOYO' },
  { label: 'Jinmeiyō (人名用)', value: 'JINMEIYO' },
  { label: 'Other (другое)', value: 'OTHER' }
]

const jlptLvlOptions = [
  { label: 'N5', value: 5 },
  { label: 'N4', value: 4 },
  { label: 'N3', value: 3 },
  { label: 'N2', value: 2 },
  { label: 'N1', value: 1 },
  { label: 'Вне JLPT', value: 0} 
]

const kankenLvlOptions = [
  { label: '10級', value: 10 },
  { label: '9級', value: 9 },
  { label: '8級', value: 8 },
  { label: '7級', value: 7 },
  { label: '6級', value: 6 },
  { label: '5級', value: 5 },
  { label: '4級', value: 4 },
  { label: '3級', value: 3 },
  { label: '準2級', value: 2.5 },
  { label: '2級', value: 2 },
  { label: '準1級', value: 1.5 },
  { label: '1級', value: 1 },
  { label: 'Вне 漢字検定', value: 0 }
]

const chineseReadingCategoryOptions = [
  { label: 'ГО-ОН', value: 'WU'},
  { label: 'КАН-ОН', value: 'HAN'},
  { label: 'ТО:-ОН', value: 'TAN'},
  { label: 'КАНЪЁ:-ОН', value: 'CUSTOMARY'}
]

const loadRadicals = async () => {
  const { data } = await axios.get('/api/v1/radicals/forms')
  radicalForms.value = data
}

const openRadicalPicker = async () => {
  await loadRadicals()
  showRadicalPicker.value = true
}

const selectRadical = (radical) => {
  kanji.value.radicalForm = { spelling: radical.spelling, id: radical.id }
  showRadicalPicker.value = false
}

const addReading = () => {
  kanji.value.readings.push({
    readingType: '',
    text: '',
    joyo: false,
    chineseReadingCategory: ''
  })
}

const removeReading = (index) => {
  kanji.value.readings.splice(index, 1)
}

const addBaseMeaning = () => {
  kanji.value.baseMeanings.push({
    meaning: ''
  })
}

const removeBaseMeaning = (index) => {
  kanji.value.baseMeanings.splice(index, 1)
} 

const prepareDto = (k, includeId = false) => {
  const dto = {
        spelling: k.spelling,
        meanings: k.meanings,
        etymology: k.etymology,
        strokeCount: k.strokeCount,
        category: k.category,
        radicalFormId: k.radicalForm.id,
        jlptLvl: k.jlptLvl,
        kankenLvl: k.kankenLvl,
        jisCode: k.jisCode,
        unicode: k.unicode,
        baseMeanings: k.baseMeanings.map(b => ({ meaning: b.meaning })),
        readings: k.readings.map(r => ({
        text: r.text,
        readingType: r.readingType,
        joyo: r.joyo,
        chineseReadingCategory: r.readingType === 'ON' ? r.chineseReadingCategory : null,
        })),
        traditionalForms: k.traditionalForms.map(f => f.id),
        simplifiedForms: k.simplifiedForms.map(f => f.id),
        alternativeForms: k.alternativeForms.map(f => f.id),
    }

  if (includeId) {
    dto.id = k.id
  }
  return dto
}

const validateAndSave = async () => {
  const k = kanji.value
  const errors = []

  if (!kanji.value.spelling?.trim()) {
    errors.push('Добавьте начертание иероглифа.')
  }

  if (!kanji.value.meanings?.trim()) {
    errors.push('Добавьте значение иероглифа.')
  }

  if (!kanji.value.radicalForm?.id) {
    errors.push('Не выбран радикал.')
  }

  if (!kanji.value.baseMeanings || kanji.value.baseMeanings.length === 0) {
    errors.push('Добавьте хотя бы одно базовое значение.')
  }

  if (!kanji.value.readings || kanji.value.readings.length === 0) {
    errors.push('Добавьте хотя бы одно чтение.')
    } else {
      kanji.value.readings.forEach((r, index) => {
        if (!r.readingType) {
          errors.push(`У чтения №${index + 1} не выбран тип.`)
        }
        if (!r.text?.trim()) {
          errors.push(`У чтения №${index + 1} не указано значение.`)
        }
    })
  }

  if (!kanji.value.strokeCount || kanji.value.strokeCount <= 0) {
    errors.push('Число черт должно быть больше 0.')
  }

  if (errors.length > 0) {
    alert('Ошибка при сохранении:\n\n' + errors.join('\n'))
    return
  }

  emit('save', prepareDto(kanji.value, !!kanji.value.id))

}

const confirmAndDelete = () => {
  if (confirm(`Удалить иероглиф «${kanji.value.spelling}»? Это действие необратимо.`)) {
    emit('delete', kanji.value.id)
  }
}

watchEffect(() => {
  if (!kanji.value || !kanji.value.readings) return
  kanji.value.readings.forEach(reading => {
    if (reading.readingType !== 'ON' && reading.chineseReadingCategory) {
      reading.chineseReadingCategory = ''
    }
  })
})

</script>

<style scoped>

.radical-picker-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}
.radical-picker {
  background: white;
  padding: 1rem;
  border-radius: 6px;
  width: 300px;
  max-height: 80vh;
  overflow-y: auto;
}
.tiles {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-top: 10px;
}
.tile {
  padding: 8px 10px;
  background: #f0f0f0;
  border: 1px solid #ccc;
  cursor: pointer;
  font-size: 1.2rem;
}
.tile:hover {
  background-color: #d8e9ff;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

</style>
