<script setup lang="ts">
import { NLayoutSider } from 'naive-ui'
import MenuComp from './MenuComp.vue'
</script>
<template>
  <n-layout-sider
    show-trigger="arrow-circle"
    collapse-mode="width"
    :collapsed-width="64"
    :width="240"
    bordered
  >
    <menu-comp />
  </n-layout-sider>
</template>

<style scoped></style>
