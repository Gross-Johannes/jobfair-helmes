<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/Card.vue';
import { useCreateCalculation } from '@/composables/useBenefitsApi';

const router = useRouter();
const { isLoading, errorMessage, createCalculation } = useCreateCalculation();

const formData = ref({
  grossSalary: null,
  babyBirthDate: '',
});

async function getCalculationResult() {
  if (!formData.value.grossSalary || !formData.value.babyBirthDate) {
    errorMessage.value = 'Please fill in gross salary and baby birth date';

    return;
  }

  const calculationResponse = await createCalculation({
    grossSalary: formData.value.grossSalary,
    babyBirthDate: formData.value.babyBirthDate,
  });

  if (!calculationResponse) {
    return;
  }

  const data = {
    message: calculationResponse.message,
    data: calculationResponse.data,
    errors: calculationResponse.errors,
    grossSalary: formData.value.grossSalary,
    babyBirthDate: formData.value.babyBirthDate,
  };

  await router.push({
    name: 'benefits',
    state: { calculationResult: data },
  });
}
</script>

<template>
  <Card>
    <h2>Input</h2>

    <form @submit.prevent="getCalculationResult">
      <div class="grid">
        <label>
          Gross salary (EUR)
          <input
            v-model.number="formData.grossSalary"
            type="number"
            min="0"
            step="0.01"
            placeholder="3000.00"
          />
        </label>

        <label>
          Baby birth date
          <input v-model="formData.babyBirthDate" type="date" />
        </label>
      </div>

      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

      <div class="actions">
        <input
          type="submit"
          id="calculateBtn"
          :value="isLoading ? 'Calculating...' : 'Calculate'"
          :disabled="isLoading"
        />
      </div>
    </form>
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

#calculateBtn {
  padding: 8px 12px;
  border: 0;
  border-radius: 6px;
  background: #2d6cdf;
  color: white;
  cursor: pointer;
}

#calculateBtn:hover {
  background: #1f56b6;
}

.error {
  margin-top: 10px;
  color: #c81e1e;
  font-size: 14px;
  font-weight: 600;
}
</style>
