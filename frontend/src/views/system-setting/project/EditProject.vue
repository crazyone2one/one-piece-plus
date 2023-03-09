<script setup lang="ts">
import { FormInst, NForm, NFormItem, NInput } from 'naive-ui'
import { ref, nextTick, reactive } from 'vue'
import { useI18n } from 'vue-i18n'
import { useConfigStore } from '/@/stores/config'
import { useUserStore } from '/@/stores/user'
import { IProject, modifyProject, saveProject } from '/@/apis/project-api'
import ModalDialogComp from '/@/components/common/ModalDialogComp.vue'
import { resetModel } from '/@/utils/common'

const { t } = useI18n()
const configStore = useConfigStore()
const user = useUserStore()
const modalDialogComp = ref<InstanceType<typeof ModalDialogComp> | null>(null)
const title = ref<string>(t('project.create'))
const formRef = ref<FormInst | null>(null)
const model = ref<IProject>({
  id: '',
  name: '',
  description: '',
})
const state = reactive({
  issueTemplateId: '',
  platformOptions: [],
})
const rules = {
  name: [
    { required: true, trigger: ['blur'], message: t('project.input_name') },
    {
      min: 2,
      max: 50,
      message: t('commons.input_limit', [2, 50]),
      trigger: 'blur',
    },
  ],
  description: {
    max: 250,
    trigger: ['blur'],
    message: t('commons.input_limit', [0, 250]),
  },
}
const emits = defineEmits(['refresh'])
// * 提交数据
const handleSubmit = (): void => {
  formRef.value?.validate((errors) => {
    if (!errors) {
      model.value.workspaceId = configStore.getWorkspaceId()
      model.value.createUser = user.getSessionUser().id
      if (model.value.id !== '') {
        modifyProject(model.value).then(() => {
          window.$message.success(t('commons.save_success'))
          modalDialogComp.value?.toggleModal()
          emits('refresh')
        })
      } else {
        saveProject(model.value).then(() => {
          window.$message.success(t('commons.save_success'))
          modalDialogComp.value?.toggleModal()
          emits('refresh')
        })
      }
    }
  })
}
/**
 * * 打开编辑框
 * @param params
 */
const openEditProjectModal = (params?: IProject) => {
  modalDialogComp.value?.toggleModal()
  formRef.value?.restoreValidation()
  resetModel(model.value)
  nextTick(() => {
    if (params) {
      title.value = t('project.edit')
      model.value = Object.assign({}, params)
      state.issueTemplateId = params.issueTemplateId as string
    }
  })
}
defineExpose({ openEditProjectModal })
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
            :placeholder="$t('project.input_name')"
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
