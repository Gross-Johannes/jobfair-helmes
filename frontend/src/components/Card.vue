<template>
  <section class="card">
    <slot></slot>
  </section>
</template>

<style scoped>
.card {
  background: var(--c-white);
  border: 1px solid var(--c-border);
  border-radius: var(--radius-lg);
  padding: 2rem 2.25rem;
  box-shadow: var(--shadow-card);
}
</style>
