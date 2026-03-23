<script setup lang="ts">
import { RouterView } from 'vue-router';
import Header from '@/components/layouts/Header.vue';
</script>

<template>
  <div class="container">
    <Header />
    <RouterView />
  </div>
</template>

<style scoped>
.container {
  max-width: 860px;
  min-height: 100vh;
  margin: 0 auto;
  padding: 0 1.5rem 4rem;
  display: flex;
  flex-direction: column;
}
</style>
