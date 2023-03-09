<script setup lang="ts">
import { NLayout } from 'naive-ui'
import { useClientHeight } from '/@/composables/common'
import HeaderComp from '/@/components/layout/components/header/index.vue'
import AsideComp from '/@/components/layout/components/side/index.vue'
import ContentComp from '/@/components/layout/components/content/index.vue'

const { height } = useClientHeight()
</script>
<template>
  <n-layout :style="{ height: height + 'px' }">
    <header-comp />
    <n-layout position="absolute" style="top: 64px" has-sider>
      <aside-comp />
      <content-comp />
    </n-layout>
  </n-layout>
</template>

<style scoped></style>
