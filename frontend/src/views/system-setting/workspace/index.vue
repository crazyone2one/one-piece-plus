<script setup lang="ts">
import { ref, computed, h, onMounted } from 'vue'
import { DataTableColumns, NSpin, NButton, NDataTable } from 'naive-ui'
import {
  getWorkspaceMemberSpecial,
  getWorkspacePageData,
  IWorkspace,
} from '/@/apis/workspace-api'
import { useI18n } from 'vue-i18n'
import { useConfigStore } from '/@/stores/config'
import CardComponent from '/@/components/common/CardComponent.vue'
import SearchComponent from '/@/components/common/SearchComponent.vue'
import TableOperatorComp from '/@/components/common/TableOperatorComp.vue'
import PaginationComp from '/@/components/common/PaginationComponent.vue'
import EditWorkspace from './EditWorkspace.vue'

const searchComponent = ref<InstanceType<typeof SearchComponent> | null>(null)
const editWorkspace = ref<InstanceType<typeof EditWorkspace> | null>(null)
const loading = ref(false)
const { t } = useI18n()
const configStore = useConfigStore()
const condition = ref({
  name: '',
  page: 1,
  limit: 10,
})
const state = ref<{ data: IWorkspace[]; dataCount: number }>({
  data: [],
  dataCount: 0,
})
const workspaceId = computed(() => {
  return configStore.getWorkspaceId()
})
// * 构建table column
const createColumns = (): DataTableColumns<IWorkspace> => {
  return [
    {
      type: 'selection',
    },
    {
      title: t('commons.name'),
      key: 'name',
      align: 'center',
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
            class: 'color-primary',
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
const columns: DataTableColumns<IWorkspace> = createColumns()
const rowKey = (row: IWorkspace) => {
  return row.id as string
}
const handleAdd = () => {
  editWorkspace.value?.openEditWorkspaceModal()
}
const handleEdit = (row: IWorkspace) => {
  editWorkspace.value?.openEditWorkspaceModal(row)
}
const loadTableData = () => {
  loading.value = true
  getWorkspacePageData(condition.value).then((resp) => {
    state.value.data = resp.data.records
    state.value.dataCount = resp.data.total
    state.value.data.forEach((item) => {
      getWorkspaceMemberSpecial({ name: '', workspaceId: item.id }).then(
        (res) => {
          const member = res.data
          item.memberSize = member.length
        }
      )
    })
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
  <edit-workspace ref="editWorkspace" @refresh="loadTableData" />
</template>

<style scoped></style>
