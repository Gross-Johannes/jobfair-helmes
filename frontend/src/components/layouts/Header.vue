<script setup lang="ts">
import { RouterLink } from 'vue-router';
</script>

<template>
  <header>
    <RouterLink to="/">
      <header class="site-header">
        <span class="icon">✦</span>
        <span class="title"> Parental Benefit Calculator</span>
      </header>
    </RouterLink>
  </header>
</template>

<style scoped>
.site-header {
  padding: 1.75rem 0 1.25rem;
  border-bottom: 3px dotted var(--c-border);
  text-align: center;
  margin-bottom: 2rem;
}

.title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--c-text);
  letter-spacing: 0.1em;
}

.icon {
  font-size: 1.5rem;
  color: var(--c-sage);
  line-height: 1;
  padding-right: 0.5rem;
}

a {
  text-decoration: none;
}
</style>
