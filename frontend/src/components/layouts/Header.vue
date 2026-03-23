<script setup lang="ts">
import { RouterLink } from 'vue-router';
</script>

<template>
  <header>
    <RouterLink to="/">
      <h1>Parental Benefit Calculator</h1>
    </RouterLink>
  </header>
</template>

<style scoped>
header {
  padding-block: 1rem;
}

header > a {
  text-decoration: none;
  color: inherit;
}
</style>
