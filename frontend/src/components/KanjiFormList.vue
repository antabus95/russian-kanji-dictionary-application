<template>
  <div>
    <h3>{{ title }}</h3>
    <div
      v-for="(form, index) in forms"
      :key="index"
      style="margin-bottom: 8px;"
    >
      <input
        v-model.number="form.id"
        placeholder="ID"
        @change="fetchSpelling(form)"
      />
      <input
        :value="form.spelling"
        readonly
      />
      <button @click="remove(index)">Удалить</button>
      <span v-if="form.error" style="color: red; margin-left: 8px;">{{ form.error }}</span>
    </div>

    <button @click="add">Добавить</button>
  </div>
</template>

<script setup>
import { toRefs } from 'vue'
import axios from 'axios'

const props = defineProps({
  modelValue: Array,
  title: String,
})

const emit = defineEmits(['update:modelValue'])

const { modelValue: forms } = toRefs(props)

const fetchSpelling = async (form) => {
  if (!forms.value) return
  form.error = ''
  form.spelling = ''

  if (!form.id) {
    form.error = 'Введите ID'
    return
  }

  try {
    const { data } = await axios.get(`/api/v1/kanji/${form.id}`)
    form.spelling = data.spelling
  } catch (e) {
    form.error = 'Иероглиф с таким ID не найден'
    form.spelling = ''
  }
}

const add = () => {
    if (!forms.value) return
    emit('update:modelValue', [...forms.value, { id: null, spelling: '', error: '' }])
}

const remove = (index) => {
  const updated = [...forms.value]
  updated.splice(index, 1)
  emit('update:modelValue', updated)
}
</script>
