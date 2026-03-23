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
  max-width: 920px;
  min-height: 100vh;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
}
</style>
