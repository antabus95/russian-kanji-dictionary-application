<template>
  <div class="pagination">
    <button :disabled="currentPage === 0" @click="$emit('page-changed', currentPage - 1)">Назад</button>
    <span>Страница {{ currentPage + 1 }} из {{ totalPages }}</span>
    <button :disabled="currentPage >= totalPages - 1" @click="$emit('page-changed', currentPage + 1)">Вперед</button>
  </div>
</template>

<script setup>
defineProps(['currentPage', 'totalPages'])
</script>

<style scoped>

  .pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 2rem;
  color: #f0f0f0;
  font-size: 16px;
}

.pagination button {
  padding: 6px 12px;
  background-color: #2c2c2c;
  color: #f0f0f0;
  border: 1px solid #444;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}

.pagination button:hover:not(:disabled) {
  background-color: #3a3a3a;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

</style>