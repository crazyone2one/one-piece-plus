<script setup lang="ts">
import { FormInst, NForm, NFormItem, NInput } from 'naive-ui'
import { ref, reactive, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useConfigStore } from '/@/stores/config'
import ModalDialogComp from '/@/components/common/ModalDialogComp.vue'
import { resetModel } from '/@/utils/common'
import { ITestCase } from '/@/apis/test-case-api'
import { useUserStore } from '/@/stores/user'
import { getProjectMemberOption } from '/@/apis/user-api'

const { t } = useI18n()
const configStore = useConfigStore()
const userStore = useUserStore()
const modalDialogComp = ref<InstanceType<typeof ModalDialogComp> | null>(null)
const formRef = ref<FormInst | null>(null)
const model = reactive<ITestCase>({
  id: '',
  name: '',
} as ITestCase)
const userOptions = ref([])
const rules = {
  name: [
    {
      required: true,
      message: t('test_track.case.input_name'),
      trigger: 'blur',
    },
    {
      max: 100,
      message: t('test_track.length_less_than') + '100',
      trigger: 'blur',
    },
  ],
  maintainer: [
    {
      required: true,
      message: t('api_test.automation.scenario.select_principal'),
      trigger: 'change',
    },
  ],
}
const projectId = computed(() => {
  return configStore.getProjectId()
})
const moduleOptions = computed(() => {
  return configStore.commonStore.testCaseModuleOptions
})
const emits = defineEmits(['refresh'])
// * 提交数据
const handleSubmit = (): void => {
  formRef.value?.validate((errors) => {
    if (!errors) {
      console.log(1)
      emits('refresh')
    }
  })
}
/**
 * * 打开编辑框
 * @param params
 */
const openCreateCaseDialog = () => {
  modalDialogComp.value?.toggleModal()
  formRef.value?.restoreValidation()
  resetModel(model)
  model.maintainer = userStore.getSessionUser().id
  getMaintainerOptions()
}
/**
 * 初始化人员信息
 */
const getMaintainerOptions = () => {
  getProjectMemberOption(configStore.getProjectId()).then((resp) => {
    console.log(resp.data)
  })
}
defineExpose({ openCreateCaseDialog })
</script>
<template>
  <modal-dialog-comp
    ref="modalDialogComp"
    :title-content="$t('test_track.case.create')"
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
            :placeholder="$t('commons.name')"
          />
        </n-form-item>
        <n-form-item
          :label="$t('api_test.automation.scenario.principal')"
          path="maintainer"
        >
          <n-select
            v-model:value="model.maintainer"
            :options="userOptions"
            :placeholder="$t('api_test.automation.scenario.principal')"
            filterable
          />
        </n-form-item>
        <n-form-item
          :label="$t('api_test.automation.scenario.follow_people')"
          path="followPeople"
        >
        </n-form-item>
        <n-form-item :label="$t('commons.remark')" path="description">
          <n-input v-model:value="model.remark" type="textarea" />
        </n-form-item>
      </n-form>
    </template>
  </modal-dialog-comp>
</template>

<style scoped></style>
