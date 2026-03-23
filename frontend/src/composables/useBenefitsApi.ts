import { ref } from 'vue';
import type { CalculationResponse, RequestInput } from '@/types/types';

export function useCreateCalculation() {
  const isLoading = ref(false);
  const errorMessage = ref('');

  async function createCalculation(input: RequestInput): Promise<CalculationResponse | null> {
    isLoading.value = true;
    errorMessage.value = '';

    try {
      const response = await fetch('/api/v1/parental-benefits/calculations', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(input),
      });

      const data = (await response.json()) as CalculationResponse;

      if (!response.ok) {
        errorMessage.value = data.message || 'Failed to calculate benefits.';
        return null;
      }

      return data;
    } catch (error) {
      console.error('Error occurred while creating calculation:', error);
      errorMessage.value = 'Something went wrong while fetching the calculation result.';
      return null;
    } finally {
      isLoading.value = false;
    }
  }

  return {
    isLoading,
    errorMessage,
    createCalculation,
  };
}