<script setup lang="ts">
// import { Component, h } from 'vue'
import { NResult, NButton, NIcon } from 'naive-ui'
</script>
<template>
  <n-result
    status="info"
    title="Dashboard"
    description="在这个年代，信息就是金钱，金钱就是信息。"
  >
    <template #footer>
      <n-button>{{ $t('commons.example') }}</n-button>
      <span class="i-mdi:translate text-orange-400" />
      <span class="i-custom:alarm text-orange-400" />
      <span class="i-my-icons:logo text-orange-400" />
      <n-icon>
        <span class="i-custom:alarm text-orange-400" />
      </n-icon>
    </template>
  </n-result>
</template>

<style scoped></style>
