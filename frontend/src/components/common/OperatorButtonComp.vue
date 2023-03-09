<script setup lang="ts">
import { NPopover, NButton } from 'naive-ui'
withDefaults(
  defineProps<{
    tip?: string
    iconType?: string
    btnType?:
      | 'default'
      | 'tertiary'
      | 'primary'
      | 'success'
      | 'info'
      | 'warning'
      | 'error'
    btnSize?: 'tiny' | 'small' | 'medium' | 'large'
    disabled?: boolean
  }>(),
  {
    tip: '',
    iconType: 'i-mdi:progress-question',
    btnType: 'primary',
    disabled: false,
    btnSize: 'small',
  }
)
const emit = defineEmits(['execute'])
</script>
<template>
  <n-popover v-if="tip" trigger="hover" placement="bottom" :show-arrow="false">
    <template #trigger>
      <n-button
        :type="btnType"
        :size="btnSize"
        :disabled="disabled"
        secondary
        circle
        @click="emit('execute')"
      >
        <template #icon>
          <span :class="iconType" />
        </template>
      </n-button>
    </template>
    <span>{{ tip }}</span>
  </n-popover>
  <n-button
    v-else
    :type="btnType"
    :size="btnSize"
    :disabled="disabled"
    secondary
    circle
    @click="emit('execute')"
  >
    <template #icon>
      <span :class="iconType" />
    </template>
  </n-button>
</template>

<style scoped></style>
