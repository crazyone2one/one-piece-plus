<script setup lang="ts">
import { ref, computed, h, onMounted } from 'vue'
import { DataTableColumns, NSpin, NButton, NDataTable } from 'naive-ui'
import { getProjectPageData, IProject } from '/@/apis/project-api'
import { useI18n } from 'vue-i18n'
import { useConfigStore } from '/@/stores/config'
import { IReqPage } from '/@/apis/interface'
import CardComponent from '/@/components/common/CardComponent.vue'
import SearchComponent from '/@/components/common/SearchComponent.vue'
import TableOperatorComp from '/@/components/common/TableOperatorComp.vue'
import PaginationComp from '/@/components/common/PaginationComponent.vue'
import EditProject from './EditProject.vue'

const searchComponent = ref<InstanceType<typeof SearchComponent> | null>(null)
const editProject = ref<InstanceType<typeof EditProject> | null>(null)
const loading = ref(false)
const { t } = useI18n()
const configStore = useConfigStore()
const condition = ref<IReqPage>({
  name: '',
  page: 1,
  limit: 10,
})
const state = ref<{ data: IProject[]; dataCount: number }>({
  data: [],
  dataCount: 0,
})
const workspaceId = computed(() => {
  return configStore.getWorkspaceId()
})
// * 构建table column
const createColumns = (): DataTableColumns<IProject> => {
  return [
    {
      type: 'selection',
    },
    {
      title: t('commons.name'),
      key: 'name',
      align: 'center',
      minWidth: 100,
      ellipsis: true,
    },
    {
      title: t('commons.description'),
      key: 'description',
      ellipsis: true,
    },
    {
      title: t('commons.member'),
      key: 'memberSize',
      render(row) {
        return h(
          NButton,
          {
            text: true,
            tag: 'a',
            // onClick: () => cellClick(row),
          },
          { default: () => row.memberSize }
        )
      },
    },
    {
      title: t('commons.operating'),
      align: 'center',
      key: 'actions',
      fixed: 'right',
      render(row) {
        return h(
          TableOperatorComp,
          {
            showDelete: workspaceId.value !== row.id,
            onEditClick: () => handleEdit(row),
          },
          {}
        )
      },
    },
  ]
}
const columns: DataTableColumns<IProject> = createColumns()
const rowKey = (row: IProject) => {
  return row.id as string
}
const handleAdd = () => {
  editProject.value?.openEditProjectModal()
}
const handleEdit = (row: IProject) => {
  editProject.value?.openEditProjectModal(row)
}
const loadTableData = () => {
  loading.value = true
  condition.value.workspaceId = workspaceId.value
  getProjectPageData(condition.value)
    .then((resp) => {
      const { records, total } = resp.data
      state.value.data = records
      state.value.dataCount = total
      loading.value = false
    })
    .finally(() => {
      loading.value = false
    })
}
/**
 * * 翻页
 */
const handleUpdatePage = () => {
  loadTableData()
}
/**
 * * 改变每页显示数量
 */
const handleUpdatePageSize = () => {
  condition.value.page = 1
  loadTableData()
}
onMounted(() => {
  loadTableData()
})
</script>
<template>
  <n-spin :show="loading">
    <card-component>
      <template #header>
        <search-component
          ref="searchComponent"
          :condition="condition"
          @create="handleAdd"
          @search="loadTableData"
        />
      </template>
      <template #content>
        <n-data-table
          :columns="columns"
          :data="state.data"
          :bordered="false"
          :row-key="rowKey"
        />
        <pagination-comp
          ref="paginationComp"
          :condition="condition"
          :item-count="state.dataCount"
          @update-page="handleUpdatePage"
          @update-page-size="handleUpdatePageSize"
        />
      </template>
    </card-component>
  </n-spin>
  <edit-project ref="editProject" @refresh="loadTableData" />
</template>

<style scoped></style>
