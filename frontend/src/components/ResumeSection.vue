<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/Card.vue';

const router = useRouter();
const applicationId = ref('');
const errorMessage = ref('');

async function resumeById() {
  const id = applicationId.value.trim();
  const uuidRegex = /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/;

  if (!id) {
    errorMessage.value = 'Please enter an ID';
    return;
  }

  if (!uuidRegex.test(id)) {
    errorMessage.value = 'Invalid ID format';
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
    <div class="card-head">
      <span class="icon" aria-hidden="true">◈</span>
      <h2>Find existing benefits</h2>
    </div>
    <p class="card-desc">Have an application ID? Enter it below.</p>

    <div class="id-row">
      <label>
        <span class="label-text">Application ID</span>
        <input
          v-model="applicationId"
          id="applicationId"
          type="text"
          placeholder="23438983-155a-445d-83e7-621e6a1e862d"
        />
      </label>
      <button type="button" @click="resumeById">Find</button>
    </div>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

  </Card>
</template>

<style scoped>
.card-head {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.25rem;
}

.icon {
  font-size: 1rem;
  color: var(--c-sage);
  line-height: 1;
}

h2 {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--c-text);
}

.card-desc {
  font-size: 0.85rem;
  color: var(--c-text-muted);
  font-weight: 300;
  margin-bottom: 1.25rem;
}

.id-row {
  display: flex;
  gap: 0.75rem;
  align-items: flex-end;
}

.id-row label {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
  min-width: 0;
}

.label-text {
  font-size: 0.78rem;
  font-weight: 500;
  color: var(--c-text-muted);
  padding-right: 0.5rem;
}

input[type='text'] {
  padding: 0.6rem 0.8rem;
  background: var(--c-warm-gray);
  border: 1.5px solid transparent;
  border-radius: var(--radius-sm);
  color: var(--c-text);
  font-family: var(--font-family);
  font-size: 0.85rem;
  transition: border-color 0.2s, background 0.2s;
  outline: none;
  min-width: 0;
}

input[type='text']::placeholder {
  color: var(--c-text-light);
  font-size: 0.8rem;
}

input[type='text']:focus {
  background: var(--c-white);
  border-color: var(--c-sage);
}

button {
  padding: 0.6rem 1.2rem;
  background: var(--c-sage);
  color: var(--c-white);
  border: none;
  border-radius: var(--radius-sm);
  font-family: var(--font-family);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  white-space: nowrap;
  box-shadow: var(--shadow-btn);
  transition: background 0.2s;
}

button:hover {
  background: var(--c-sage-dark);
}

.error {
  margin-top: 0.9rem;
  background: var(--c-error-light);
  border: 1px solid #ECC8C8;
  color: var(--c-error);
  font-size: 0.83rem;
  padding: 0.5rem 0.85rem;
  border-radius: var(--radius-sm);
}
</style>
