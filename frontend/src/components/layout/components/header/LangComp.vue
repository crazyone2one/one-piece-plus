<script setup lang="ts">
import { NPopselect, NIcon } from 'naive-ui'
import { useAppStore } from '/@/stores/app'
import { computed } from 'vue'

import { storeToRefs } from 'pinia'

const stores = useAppStore()
const { lang } = storeToRefs(stores)
const value = computed(() => lang.value.defaultLang)
const handleSelect = (value: string) => {
  stores.setLang(value)
}
</script>
<template>
  <n-popselect
    v-model:value="value"
    :options="lang.langArray"
    trigger="click"
    @update:value="handleSelect"
  >
    <n-icon>
      <span
        class="i-mdi:translate text-orange-400"
        style="height: 1.5em; width: 1.5em"
      />
    </n-icon>
  </n-popselect>
</template>

<style scoped></style>
