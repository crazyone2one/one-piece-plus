<script setup lang="ts">
import {
  ref,
  computed,
  watch,
  onMounted,
  onUnmounted,
  getCurrentInstance,
} from 'vue'
import { NPopselect, SelectOption, NIcon, NInput } from 'naive-ui'
import { useConfigStore } from '/@/stores/config'
import { useI18n } from 'vue-i18n'
import {
  getUserWorkspaceList,
  IWorkspace,
  switchWorkspace,
} from '/@/apis/workspace-api'
import { useRouter } from 'vue-router'
import { IUserInfo } from '/@/apis/user-api'

const config = useConfigStore()
const { t } = useI18n()
const searchString = ref('')
const currentWorkspaceId = computed(() => {
  return config.getWorkspaceId()
})
const instance = getCurrentInstance()
const currentWorkspaceName = ref<string>(config.getWorkspaceName())
const value = ref(currentWorkspaceId.value)
const options = ref<SelectOption[]>([])
const workspaceList = ref<IWorkspace[]>([])
const wsListCopy = ref<IWorkspace[]>([])
const router = useRouter()
const handleUpdateValue = (value: string, option: SelectOption) => {
  if (!value || currentWorkspaceId.value === value) {
    return false
  }
  _changeWs(option)
}

const _changeWs = (option: SelectOption) => {
  config.setWorkspaceName(option.label as string)
  currentWorkspaceName.value = option.label as string
  switchWorkspace(option.value as string).then((resp) => {
    config.setProjectId(resp.data.lastProjectId)
    config.setWorkspaceId(resp.data.lastWorkspaceId)
    //  发布一个事件
    instance?.proxy?.$Bus.emit('projectChange')
    // router.push(getRedirectUrl(resp.data))
  })
}
const getRedirectUrl = (user: IUserInfo) => {
  if (!user.lastProjectId || !user.lastWorkspaceId) {
    // 没有项目级的权限直接回到 /setting/project/:type
    // 只是某一个工作空间的用户组也转到 /setting/project/:type
    return '/setting/project/:type'
  }
  return '/setting'
}
const initMenuData = () => {
  getUserWorkspaceList().then((response) => {
    wsListCopy.value = response.data
    workspaceList.value = response.data
    const workspace = response.data.filter(
      (r) => r.id === currentWorkspaceId.value
    )
    if (workspace.length > 0) {
      currentWorkspaceName.value = workspace[0].name
      workspaceList.value = response.data.filter(
        (r) => r.id !== currentWorkspaceId.value
      )
      workspaceList.value.unshift(workspace[0])
    }
  })
}
const query = (queryString: string) => {
  workspaceList.value = queryString
    ? wsListCopy.value.filter((item) => item.name.indexOf(queryString) !== -1)
    : wsListCopy.value
}
watch(
  () => workspaceList.value,
  (newValue) => {
    options.value = []
    newValue.forEach((item) => {
      const option: SelectOption = {}
      option.label = item.name
      option.value = item.id
      options.value.push(option)
    })
  }
)
watch(
  () => searchString.value,
  (newValue) => {
    query(newValue)
  }
)
onMounted(() => {
  // 订阅一个具体的事件
  instance?.proxy?.$Bus.on('changeWs', (workspaceId) => {
    _changeWs(workspaceId)
  })
  initMenuData()
})
onUnmounted(() => {
  // 只传一个参数，取消订阅同名事件
  instance?.proxy?.$Bus.off('changeWs')
})
</script>
<template>
  <n-popselect
    v-model:value="value"
    :options="options"
    size="medium"
    scrollable
    @update:value="handleUpdateValue"
  >
    <template #empty> {{ $t('workspace.none') }}</template>
    <template #action>
      <n-input
        v-model:value="searchString"
        size="small"
        :placeholder="$t('workspace.search_by_name')"
        clearable
      >
        <template #prefix>
          <n-icon>
            <div class="i-custom:search" />
          </n-icon>
        </template>
      </n-input>
    </template>
    <div style="margin-right: 8px">
      {{
        currentWorkspaceName || t('workspace.please_select_a_workspace_first')
      }}
    </div>
  </n-popselect>
</template>

<style scoped></style>
