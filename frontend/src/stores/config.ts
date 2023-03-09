import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface ICommonState {
  currentWorkspaceId: string
  currentProjectId: string
  workspaceName: string
  projectName: string
  // testCaseSelectNode: TreeOption
  testCaseSelectNodeIds: string[]
  testCaseModuleOptions: { path: string; id?: string }[]
  // testCaseTemplate: ICaseTemplate
}
export const useConfigStore = defineStore(
  'commonStore',
  () => {
    const commonStore = ref<ICommonState>({
      currentWorkspaceId: '',
      currentProjectId: '',
      workspaceName: '',
      projectName: '',
      testCaseSelectNodeIds: [],
      testCaseModuleOptions: [],
    })
    const setProjectId = (projectId: string): void => {
      commonStore.value.currentProjectId = projectId
    }
    const getProjectId = (): string => {
      return commonStore.value.currentProjectId
    }
    const setWorkspaceId = (workspaceId: string): void => {
      commonStore.value.currentWorkspaceId = workspaceId
    }

    const getWorkspaceId = (): string => {
      return commonStore.value.currentWorkspaceId
    }
    const setWorkspaceName = (workspaceName: string): void => {
      commonStore.value.workspaceName = workspaceName
    }
    const getWorkspaceName = (): string => {
      return commonStore.value.workspaceName
    }
    const setProjectName = (val: string): void => {
      commonStore.value.projectName = val
    }
    const getProjectName = (): string => {
      return commonStore.value.projectName
    }
    // * 重置信息
    const resetcommonStoreStore = () => {
      commonStore.value.currentWorkspaceId = ''
      commonStore.value.currentProjectId = ''
      commonStore.value.workspaceName = ''
      commonStore.value.projectName = ''
    }

    return {
      commonStore,
      setProjectId,
      setWorkspaceId,
      getProjectId,
      getWorkspaceId,
      setWorkspaceName,
      getWorkspaceName,
      resetcommonStoreStore,
      setProjectName,
      getProjectName,
    }
  },
  {
    persist: true,
  }
)
