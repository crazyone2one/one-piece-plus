<script setup lang="ts">
import { NLayoutHeader } from 'naive-ui'
import LogoComp from './LogoComp.vue'
import RightHeader from './RightHeaderComp.vue'
</script>
<template>
  <n-layout-header
    style="height: 64px; padding: 24px"
    bordered
    class="flex align-items"
  >
    <logo-comp />
    <right-header />
  </n-layout-header>
</template>

<style scoped></style>
