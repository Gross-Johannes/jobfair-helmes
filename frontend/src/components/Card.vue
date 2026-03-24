<template>
  <section class="card">
    <slot></slot>
  </section>
</template>

<style scoped>
.card {
  background: rgba(255, 255, 255, 0.5);
  border: 1px solid var(--c-border);
  border-radius: var(--radius-lg);
  padding: 2rem 2.25rem;
  box-shadow: var(--shadow-card);
  border-left: 3px solid var(--c-sage);
}
</style>
