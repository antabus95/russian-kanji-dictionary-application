<template>
  <div class="handwriting-input">
    <div class="input-with-button">
      <input
        type="text"
        :value="inputValue"
        @input="e => updateValue(e.target.value)"
      />
      <button @click="toggleCanvas" type="button" title="Draw Kanji">✏️</button>

      <div v-if="showCanvas" class="draw-canvas-container-popup">
        <div class="canvas-wrapper">
          <canvas
            ref="canvas"
            width="300"
            height="300"
            class="draw-canvas"
            @mousedown="startDrawing"
            @mousemove="draw"
            @mouseup="stopDrawing"
            @mouseleave="stopDrawing"
            @touchstart.prevent="startDrawingTouch"
            @touchmove.prevent="drawTouch"
            @touchend="stopDrawing"
          ></canvas>
          <button class="eraser-btn" @click="clearCanvas" title="Clear">🧽</button>
        </div>

        <div class="candidates">
          <span
            v-for="(kanji, index) in candidates"
            :key="index"
            class="candidate"
            @click="selectCandidate(kanji)"
          >
            {{ kanji }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

import { ref, watch, nextTick } from 'vue'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])
const inputValue = ref(props.modelValue)

const updateValue = (val) => {
  if (typeof val !== 'string') {
    console.warn('Blocked non-string input to modelValue:', val)
    return
  }
  inputValue.value = val
  emit('update:modelValue', val)
}

watch(() => props.modelValue, (val) => {
  inputValue.value = typeof val === 'string' ? val : ''
})

watch(inputValue, (v) => {
  console.log('[DEBUG] inputValue =', v, 'typeof', typeof v)
  if (typeof v !== 'string') {
    throw new Error('inputValue is not a string!')
  }
})

const showCanvas = ref(false)
const canvas = ref(null)
const ctx = ref(null)
const drawing = ref(false)

let currentStroke = [[], []]
const strokes = ref([])
const candidates = ref([])

const toggleCanvas = () => {
  showCanvas.value = !showCanvas.value
}

const startDrawing = (e) => {
  drawing.value = true
  const { offsetX, offsetY } = e
  ctx.value.beginPath()
  ctx.value.moveTo(offsetX, offsetY)
  currentStroke = [[offsetX], [offsetY]]
}

const draw = (e) => {
  if (!drawing.value) return
  const { offsetX, offsetY } = e
  ctx.value.lineTo(offsetX, offsetY)
  ctx.value.stroke()
  currentStroke[0].push(offsetX)
  currentStroke[1].push(offsetY)
}

const stopDrawing = () => {
  if (!drawing.value) return
  drawing.value = false
  strokes.value.push(currentStroke)
  currentStroke = [[], []]
  requestCandidates()
}

const startDrawingTouch = (e) => {
  const rect = canvas.value.getBoundingClientRect()
  const touch = e.touches[0]
  const x = touch.clientX - rect.left
  const y = touch.clientY - rect.top
  ctx.value.beginPath()
  ctx.value.moveTo(x, y)
  drawing.value = true
  currentStroke = [[x], [y]]
}

const drawTouch = (e) => {
  if (!drawing.value) return
  const rect = canvas.value.getBoundingClientRect()
  const touch = e.touches[0]
  const x = touch.clientX - rect.left
  const y = touch.clientY - rect.top
  ctx.value.lineTo(x, y)
  ctx.value.stroke()
  currentStroke[0].push(x)
  currentStroke[1].push(y)
}

const clearCanvas = () => {
  ctx.value.clearRect(0, 0, canvas.value.width, canvas.value.height)
  strokes.value = []
  candidates.value = []
}

const requestCandidates = async () => {
  const payload = {
    app_version: '0.4',
    api_level: '537.36',
    device: 'Chrome',
    input_type: 0,
    options: 'enable_pre_space',
    requests: [
      {
        writing_guide: {
          writing_area_width: 300,
          writing_area_height: 300
        },
        ink: strokes.value,
        language: 'ja'
      }
    ]
  }

  try {
    const res = await fetch(
      'https://inputtools.google.com/request?itc=ja-t-i0-handwrit&app=chrome',
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }
    )
    const result = await res.json()
    if (result[0] === 'SUCCESS') {
      candidates.value = result[1][0][1]
    }
  } catch (err) {
    console.error('Handwriting recognition failed:', err)
  }
}

const selectCandidate = (kanji) => {
  if (typeof kanji !== 'string') 
    return
  updateValue(inputValue.value + kanji)
  showCanvas.value = false
}

watch(showCanvas, (val) => {
  if (val) {
    nextTick(() => {
      const c = canvas.value
      ctx.value = c.getContext('2d')
      ctx.value.strokeStyle = '#fff'
      ctx.value.lineWidth = 3
      ctx.value.lineCap = 'round'
    })
  }
})
</script>

<style scoped>

.handwriting-input {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-with-button {
  display: flex;
  gap: 6px;
  align-items: center;
  position: relative;
}

.input-with-button input {
  background-color: #1e1e1e;
  color: #eee;
  border: 1px solid #555;
  border-radius: 6px;
  padding: 8px 12px;
  font-size: 14px;
  transition: border-color 0.3s, background-color 0.3s;
}

.input-with-button input:focus {
  border-color: #4fc3f7;
  background-color: #2c2c2c;
  outline: none;
}

.input-with-button button {
  background-color: #333;
  color: #eee;
  border: 1px solid #555;
  border-radius: 6px;
  padding: 6px 10px;
  cursor: pointer;
  transition: background-color 0.3s, border-color 0.3s;
}
.input-with-button button:hover {
  background-color: #444;
  border-color: #777;
}

.draw-canvas-container-popup {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 8px;
  z-index: 1000;
  display: flex;
  gap: 12px;
  padding: 8px;
  background: #1c1c1c;
  border: 1px solid #444;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.4);
  border-radius: 8px;
}

.canvas-wrapper {
  position: relative;
}

.draw-canvas {
  border: 1px solid #666;
  background-color: #2a2a2a;
  touch-action: none;
  border-radius: 4px;
}

.eraser-btn {
  position: absolute;
  top: 4px;
  right: 4px;
  background: #444;
  color: #eee;
  border: 1px solid #666;
  cursor: pointer;
  font-size: 16px;
  padding: 2px 6px;
  border-radius: 4px;
}
.eraser-btn:hover {
  background: #555;
}

.candidates {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.candidate {
  font-size: 24px;
  cursor: pointer;
  background: #2f2f2f;
  color: #eee;
  border: 1px solid #555;
  border-radius: 4px;
  padding: 4px 6px;
  transition: background 0.2s;
}
.candidate:hover {
  background: #444;
}

</style>
