<script setup lang="ts">
import { FormInst, NForm, NFormItem, NInput } from 'naive-ui'
import { ref, nextTick } from 'vue'
import { useI18n } from 'vue-i18n'
import {
  addWorkspaceSpecial,
  IWorkspace,
  updateWorkspaceSpecial,
} from '/@/apis/workspace-api'
import ModalDialogComp from '/@/components/common/ModalDialogComp.vue'

const { t } = useI18n()

const modalDialogComp = ref<InstanceType<typeof ModalDialogComp> | null>(null)
const title = ref<string>(t('workspace.create'))
const formRef = ref<FormInst | null>(null)
const model = ref<IWorkspace>({
  id: '',
  name: '',
  description: '',
})
const rules = {
  name: [
    { required: true, trigger: ['blur'], message: t('workspace.input_name') },
  ],
  description: {
    max: 50,
    trigger: ['blur'],
    message: t('commons.input_limit', [0, 50]),
  },
}
const emits = defineEmits(['refresh'])
// * 提交数据
const handleSubmit = (): void => {
  formRef.value?.validate((errors) => {
    if (!errors) {
      if (model.value.id) {
        updateWorkspaceSpecial(model.value).then(() => {
          modalDialogComp.value?.closeModal()
          emits('refresh')
          window.$message?.success(t('commons.modify_success'))
        })
      } else {
        addWorkspaceSpecial(model.value).then(() => {
          modalDialogComp.value?.closeModal()
          emits('refresh')
          window.$message?.success(t('commons.save_success'))
        })
      }
    }
  })
}
/**
 * * 打开编辑框
 * @param params
 */
const openEditWorkspaceModal = (params?: IWorkspace) => {
  modalDialogComp.value?.toggleModal()
  formRef.value?.restoreValidation()
  resetModel()
  nextTick(() => {
    if (params) {
      title.value = t('workspace.update')
      model.value.id = params.id
      model.value.name = params ? params.name : ''
      model.value.description = params ? params.description : ''
    }
  })
}
defineExpose({ openEditWorkspaceModal })
// * 重置表单
const resetModel = (): void => {
  model.value.id = ''
  model.value.name = ''
  model.value.description = ''
  title.value = t('workspace.create')
}
</script>
<template>
  <modal-dialog-comp
    ref="modalDialogComp"
    :title-content="title"
    @confirm="handleSubmit"
  >
    <template #content>
      <n-form
        ref="formRef"
        :model="model"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-form-item :label="$t('commons.name')" path="name">
          <n-input
            v-model:value="model.name"
            maxlength="100"
            show-count
            :placeholder="$t('workspace.input_name')"
          />
        </n-form-item>
        <n-form-item :label="$t('commons.description')" path="description">
          <n-input v-model:value="model.description" type="textarea" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog-comp>
</template>

<style scoped></style>
