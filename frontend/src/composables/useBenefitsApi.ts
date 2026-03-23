import { ref } from 'vue';
import type { CalculationResponse, RequestInput, BenefitResponse } from '@/types/types';

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

export function useBenefitById() {
  const isLoading = ref(false);
  const errorMessage = ref('');
  const benefitResponse = ref<BenefitResponse | null>(null);

  async function fetchBenefitById(id: string) {
    if (!id) {
      errorMessage.value = 'Benefit ID is missing from the URL';
      benefitResponse.value = null;
      isLoading.value = false;
      return;
    }

    isLoading.value = true;
    errorMessage.value = '';

    try {
      const response = await fetch(`/api/v1/parental-benefits/${encodeURIComponent(id)}`);
      const data = (await response.json()) as BenefitResponse;

      if (!response.ok) {
        errorMessage.value = data.message || 'Failed to fetch benefits by ID';
        benefitResponse.value = null;
        return;
      }

      benefitResponse.value = data;
    } catch (error) {
      console.error('Error occurred while fetching benefits by ID:', error);
      errorMessage.value = 'Something went wrong while fetching benefits by ID.';
      benefitResponse.value = null;
    } finally {
      isLoading.value = false;
    }
  }

  return {
    isLoading,
    errorMessage,
    benefitResponse,
    fetchBenefitById,
  };
}
