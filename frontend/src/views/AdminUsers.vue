<template>
  <div>
    <h2>Управление пользователями</h2>

    <input
      type="text"
      placeholder="Поиск по имени"
      v-model="search"
      @input="fetchUsers"
    />

    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Имя</th>
          <th>Роль</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>
            <select v-model="selectedRoles[user.id]">
              <option v-for="role in roles" :value="role.name" :key="role.name">
                {{ role.displayName }}
              </option>
            </select>

            <button @click="changeUserRole(user.id)">Сохранить</button>
          </td>

          <td>
            <button @click="toggleBlockUser(user)">
              {{ user.enabled ? 'Заблокировать' : 'Разблокировать' }}
            </button>
            <button @click="deleteUser(user.id)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div style="margin-top: 1rem;">
      <button :disabled="currentPage === 0" @click="currentPage--; fetchUsers()">
        Назад
      </button>
      <span>Страница {{ currentPage + 1 }} из {{ totalPages }}</span>

      <button :disabled="currentPage >= totalPages - 1" @click="currentPage++; fetchUsers()">
        Вперед
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'


const users = ref([])
const roles = ref([])
const selectedRoles = ref({})
const search = ref('')
const auth = useAuthStore()
const router = useRouter()
const currentPage = ref(0)       
const totalPages = ref(1)        
const pageSize = 30              


async function fetchUsers() {
  try {
    const res = await axios.get('/api/v1/users', {
      params: { search: search.value, page: currentPage.value, size: pageSize, }
    })
    users.value = res.data.content
    totalPages.value = res.data.totalPages
    selectedRoles.value = {}
    for (const user of users.value) {
      if (user.role.startsWith('ROLE_')) {
        selectedRoles.value[user.id] = user.role.substring(5)
      } else {
        selectedRoles.value[user.id] = user.role
      }
    }

  } catch (e) {
    console.error('Ошибка загрузки пользователей:', e)
  }
}

async function fetchRoles() {
  try {
    const res = await axios.get('/api/v1/users/roles')
    roles.value = res.data
  } catch (e) {
    console.error('Ошибка загрузки ролей:', e)
  }
}

async function changeUserRole(id) {
  let role = selectedRoles.value[id]
  if (!role) {
    alert('Выберите роль')
    return
  }

  try {
    await axios.patch(`/api/v1/users/${id}/role`, { role })
    fetchUsers()
  } catch (e) {
    console.error('Ошибка смены роли:', e)
    alert('Не удалось изменить роль')
  }
}

async function deleteUser(id) {
  if (!confirm('Удалить пользователя?')) return
  try {
    await axios.delete(`/api/v1/users/${id}`)
    fetchUsers()
  } catch (e) {
    console.error('Ошибка удаления:', e)
  }
}

async function blockUser(id) {
  if (!confirm('Заблокировать пользователя?')) return
  try {
    await axios.patch(`/api/v1/users/${id}/block`)
    fetchUsers()
  } catch (e) {
    console.error('Ошибка блокировки:', e)
  }
}

async function toggleBlockUser(user) {
  const action = user.enabled ? 'заблокировать' : 'разблокировать'
  if (!confirm(`${action.charAt(0).toUpperCase() + action.slice(1)} пользователя?`)) return

  try {
    if (user.enabled) {
      await axios.patch(`/api/v1/users/${user.id}/block`)
    } else {
      await axios.patch(`/api/v1/users/${user.id}/unblock`)
    }
    fetchUsers() 
  } catch (e) {
    console.error(`Ошибка при попытке ${action} пользователя:`, e)
    alert(`Не удалось ${action} пользователя`)
  }
}

onMounted(() => {
  if (auth.user?.role !== 'ROLE_ADMIN') {
    alert('Доступ запрещён')
    router.push('/')
  } else {
    fetchUsers()
    fetchRoles()
  }
})

watch(search, () => {
  currentPage.value = 0
  fetchUsers()
})


</script>

<style scoped>
table {
  border-collapse: collapse;
  width: 100%;
  margin-top: 1rem;
}
th, td {
  border: 1px solid #ccc;
  padding: 0.5rem;
}
input {
  margin-bottom: 1rem;
  padding: 0.5rem;
  width: 300px;
}

</style>
