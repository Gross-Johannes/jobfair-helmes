<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/Card.vue';

const router = useRouter();
const applicationId = ref('');
const errorMessage = ref('');

async function resumeById() {
  const id = applicationId.value.trim();

  if (!id) {
    errorMessage.value = 'Please enter a valid ID';

    return;
  }

  errorMessage.value = '';

  await router.push({
    name: 'benefits-id',
    params: { id },
  });
}
</script>

<template>
  <Card>
    <h2>Find existing benefits</h2>

    <div class="grid">
      <label>
        ID
        <input
          v-model="applicationId"
          id="applicationId"
          type="text"
          placeholder="23438983-155a-445d-83e7-621e6a1e862d"
        />
      </label>
    </div>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    <div class="actions">
      <button id="resumeBtn" type="button" @click="resumeById">Find by ID</button>
    </div>
  </Card>
</template>

<style scoped>
h2 {
  color: #333;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1rem;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-weight: 600;
  color: black;
}

input {
  padding: 8px;
  border: 1px solid #b7c0cb;
  border-radius: 6px;
}

.actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

#resumeBtn {
  padding: 8px 12px;
  border: 0;
  border-radius: 6px;
  background: #2d6cdf;
  color: white;
  cursor: pointer;
}

#resumeBtn:hover {
  background: #1f56b6;
}

.error {
  margin-top: 10px;
  color: #c81e1e;
  font-size: 14px;
  font-weight: 600;
}
</style>
