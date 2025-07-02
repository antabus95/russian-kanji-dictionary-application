<template>
  <div v-if="radical" class="radical-edit-form">
    <p v-if="radical.id" class="radical-id">ID: {{ radical.id }}</p>

    <div class="form-field">
      <label>№</label>
      <input type="number" v-model="radical.number" />
    </div>

    <div class="form-field">
      <label>Ключ:</label>
      <input v-model="radical.spelling" />
    </div>

    <div class="form-field">
      <label>Название:</label>
      <input v-model="radical.name" />
    </div>

    <div class="form-field">
      <label>Число черт:</label>
      <input type="number" v-model="radical.strokeCount" />
    </div>

    <div class="form-section">
      <h3>Формы:</h3>
      <div
        class="form-row"
        v-for="(radicalForm, index) in radical.radicalForms.filter(f => !f._deleted)"
        :key="index"
      >
        <input v-model="radicalForm.spelling" placeholder="Форма ключа" />
        <input v-model="radicalForm.formName" placeholder="Название формы" />
        <button type="button" class="delete-btn" @click="removeRadicalForm(index)">🗑</button>
      </div>
      <button type="button" class="add-form-btn" @click="addRadicalForm">➕ Добавить форму</button>
    </div>

    <div class="action-buttons">
      <button class="save-btn" @click="validateAndSave">💾 Сохранить</button>
      <button class="back-btn" @click="$emit('back')">← Назад</button>
      <button
        v-if="radical.id"
        class="delete-key-btn"
        @click="confirmAndDelete"
      >🗑 Удалить ключ</button>
    </div>
    <div v-if="validationErrors.length" class="validation-errors">
      <ul>
        <li v-for="(err, i) in validationErrors" :key="i">{{ err }}</li>
      </ul>
    </div>
  </div>
</template>


<script setup>
import { ref, reactive, watch } from 'vue'
import axios from 'axios'

const props = defineProps({
  initialRadical: Object
})

const emit = defineEmits(['save', 'back', 'delete'])

const radical = reactive({ ...props.initialRadical })

const validationErrors = []

const addRadicalForm = () => {
  radical.radicalForms.push({
    spelling: '',
    formName: ''
  })
}

const removeRadicalForm = (index) => {
  const form = radical.radicalForms[index]
  if (form.id) {
    form._deleted = true
  } else {
    radical.radicalForms.splice(index, 1)
  }
}

const prepareDto = (r) => {
  const dto = {
        number: r.number,
        spelling: r.spelling,
        name: r.name,
        strokeCount: r.strokeCount,
        radicalForms: r.radicalForms.map(f => ({ id: f.id, spelling: f.spelling, formName: f.formName, _deleted: f._deleted || false })),
    }

  if (r.id) {
    dto.id = r.id
  }
  return dto
}

const validateAndSave = async () => {
  const r = radical

  validationErrors.value = []

  if (!r.spelling?.trim()) validationErrors.value.push('Добавьте начертание ключа.')
  if (!r.name?.trim()) validationErrors.value.push('Добавьте имя ключа.')
  if (!r.strokeCount || r.strokeCount <= 0) validationErrors.value.push('Число черт должно быть больше 0.')
  const activeForms = r.radicalForms.filter(f => !f._deleted)
  if (activeForms.length === 0) validationErrors.value.push('Добавьте хотя бы одну форму ключа.')
  if (validationErrors.value.length > 0) return

  try {
    await emit('save', prepareDto(r, !!r.id))
    const { data } = await axios.get(`/api/v1/radicals/${r.id}`)

    Object.assign(radical, {
      name: data.name,
      number: data.number,
      spelling: data.spelling,
      strokeCount: data.strokeCount,
    })

    radical.radicalForms.splice(0, radical.radicalForms.length, ...data.radicalForms)

  } catch (e) {
    validationErrors.value.push('Ошибка при обновлении данных после сохранения.')
  }
}

const confirmAndDelete = () => {
  if (confirm(`Удалить ключ «${radical.spelling}»? Это действие необратимо.`)) {
    emit('delete', radical.id)
  }
}

watch(() => props.initialRadical, (newVal) => {
  Object.assign(radical, newVal)
}, { deep: true, immediate: true })

</script>

<style scoped>

.radical-edit-form {
  background-color: #2a2a2a;
  padding: 2rem;
  border-radius: 8px;
  color: #f0f0f0;
}

.radical-id {
  font-size: 0.9rem;
  color: #aaa;
  margin-bottom: 1rem;
}

.form-field {
  margin-bottom: 1rem;
}

.form-field label {
  display: block;
  margin-bottom: 0.3rem;
  font-weight: bold;
  color: #ddd;
}

input {
  width: 100%;
  padding: 0.4rem;
  border: 1px solid #555;
  border-radius: 4px;
  background-color: #1e1e1e;
  color: #f0f0f0;
  font-size: 1rem;
}

.form-section {
  margin-top: 2rem;
}

.form-section h3 {
  font-size: 1.2rem;
  margin-bottom: 0.8rem;
  color: #f0f0f0;
}

.form-row {
  display: flex;
  gap: 10px;
  margin-bottom: 0.7rem;
}

.form-row input {
  flex: 1;
}

.delete-btn {
  background-color: transparent;
  border: none;
  cursor: pointer;
  color: #f44336;
  font-size: 1.2rem;
}

.add-form-btn {
  background-color: #4caf50;
  color: white;
  padding: 0.4rem 0.8rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-form-btn:hover {
  background-color: #45a049;
}

.action-buttons {
  margin-top: 2rem;
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.save-btn {
  background-color: #2196f3;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.save-btn:hover {
  background-color: #1976d2;
}

.back-btn {
  background-color: #666;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.back-btn:hover {
  background-color: #555;
}

.delete-key-btn {
  background-color: #d32f2f;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.delete-key-btn:hover {
  background-color: #b71c1c;
}

.validation-errors {
  background: #f44336;
  color: #fff;
  padding: 1rem;
  border-radius: 6px;
  margin-top: 1rem;
  font-size: 0.95rem;
}
.validation-errors ul {
  padding-left: 1.2rem;
  margin: 0;
}
.validation-errors li {
  list-style-type: disc;
}

</style>