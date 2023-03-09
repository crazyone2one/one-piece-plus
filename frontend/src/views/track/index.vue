<template>
  <n-space align="center">
    <div>占位符</div>
    <track-header />
  </n-space>
  <router-view />
</template>

<script setup lang="ts">
import TrackHeader from './TrackHeader.vue'
import { NSpace } from 'naive-ui'
</script>

<style scoped></style>
