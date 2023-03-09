<script setup lang="ts">
import { NLayout, NBackTop } from 'naive-ui'
</script>
<template>
  <n-layout content-style="padding: 24px;" :native-scrollbar="false">
    <router-view />
    <n-back-top :right="40" :bottom="160">
      <div
        style="
          width: 200px;
          height: 40px;
          line-height: 40px;
          text-align: center;
          font-size: 14px;
        "
      >
        改变位置
      </div>
    </n-back-top>
  </n-layout>
</template>

<style scoped></style>
