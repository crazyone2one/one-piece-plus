<script setup lang="ts">
import OperatorButton from './OperatorButtonComp.vue'
import { EDIT_BTN_TIP, DELETE_BTN_TIP } from '/@/utils/constants'
import { NSpace } from 'naive-ui'

withDefaults(
  defineProps<{
    showEdit?: boolean
    showDelete?: boolean
    editTip?: string
    deleteTip?: string
    editDisabled?: boolean
    deleteDisabled?: boolean
  }>(),
  {
    showEdit: true,
    showDelete: true,
    editTip: EDIT_BTN_TIP,
    deleteTip: DELETE_BTN_TIP,
    editDisabled: false,
    deleteDisabled: false,
  }
)
const emits = defineEmits(['editClick', 'deleteClick'])
</script>

<template>
  <n-space justify="center">
    <slot name="front" />
    <operator-button
      v-if="showEdit"
      icon-type="i-mdi:square-edit-outline"
      btn-type="warning"
      :tip="editTip"
      :disabled="editDisabled"
      @execute="emits('editClick')"
    />
    <slot name="middle" />
    <operator-button
      v-if="showDelete"
      icon-type="i-mdi:trash-can-outline"
      btn-type="error"
      :tip="deleteTip"
      :disabled="deleteDisabled"
      @execute="emits('deleteClick')"
    />
    <slot name="behind" />
  </n-space>
</template>

<style scoped></style>
