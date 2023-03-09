<script setup lang="ts">
import { ref } from 'vue'
import { NModal, NButton, NSpace } from 'naive-ui'

interface IProps {
  titleContent?: string
  showFooter?: boolean // 是否展示底部操作按钮
  isShow?: boolean
  btnText?: string
  modalWidth?: string
}
withDefaults(defineProps<IProps>(), {
  titleContent: '操作', // modal title
  showFooter: true, // show footer button
  isShow: false,
  btnText: '',
  modalWidth: '', // modal width
})
const show = ref(false)
const emits = defineEmits(['confirm', 'cancel', 'saveAsEdit'])
const toggleModal = () => {
  show.value = !show.value
  return Promise.resolve(show.value)
}
// * show modal
const showModal = () => {
  show.value = true
  return Promise.resolve(true)
}
// * close modal
const closeModal = () => {
  show.value = false
  return Promise.resolve(false)
}
const onCancel = (): void => {
  show.value = false
  emits('cancel')
}
defineExpose({ toggleModal, showModal, closeModal })
</script>
<template>
  <n-modal
    v-model:show="show"
    preset="dialog"
    :title="titleContent"
    :show-icon="false"
    :mask-closable="false"
    :style="modalWidth"
  >
    <!-- <template #header>
      <div>标题</div>
    </template> -->
    <slot name="content" />
    <template #action>
      <n-space v-if="showFooter" justify="end">
        <n-button type="default" size="small" @click="onCancel">
          {{ $t('commons.cancel') }}
        </n-button>
        <n-button type="primary" size="small" @click="emits('confirm')">
          {{ $t('commons.confirm') }}
        </n-button>
        <n-button
          v-if="isShow"
          type="primary"
          size="small"
          @click="emits('saveAsEdit')"
        >
          {{ btnText }}
        </n-button>
      </n-space>
    </template>
  </n-modal>
</template>

<style scoped></style>
