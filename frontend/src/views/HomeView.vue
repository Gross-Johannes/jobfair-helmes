<script setup lang="ts">
import InputSection from '@/components/InputSection.vue';
import ResumeSection from '@/components/ResumeSection.vue';
</script>

<template>
  <main>
    <InputSection />
    <ResumeSection />
  </main>
</template>

<style scoped>
main {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 2rem;
}
</style>
