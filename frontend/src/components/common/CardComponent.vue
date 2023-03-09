<template>
  <n-card embedded :bordered="false">
    <template #header>
      <slot name="header" />
    </template>
    <slot name="content" />

    <template #footer> <slot name="footer" /> </template>
    <template #action> <slot name="action" /></template>
  </n-card>
</template>

<script setup lang="ts">
import { NCard } from 'naive-ui'
</script>

<style scoped></style>
