<script setup lang="ts">
import { ref, computed, h, onMounted } from 'vue'
import { DataTableColumns, NSpin, NButton, NDataTable } from 'naive-ui'
import { ITestCase, testCasePageList } from '/@/apis/test-case-api'
import { useI18n } from 'vue-i18n'
import { useConfigStore } from '/@/stores/config'
import { IReqPage } from '/@/apis/interface'
import CardComponent from '/@/components/common/CardComponent.vue'
import SearchComponent from '/@/components/common/SearchComponent.vue'
import TableOperatorComp from '/@/components/common/TableOperatorComp.vue'
import PaginationComp from '/@/components/common/PaginationComponent.vue'
import EditCase from './EditCase.vue'
import TestCaseCreate from './TestCaseCreate.vue'

const searchComponent = ref<InstanceType<typeof SearchComponent> | null>(null)
const editCase = ref<InstanceType<typeof EditCase> | null>(null)
const testcaseCreate = ref<InstanceType<typeof TestCaseCreate> | null>(null)

const loading = ref(false)
const { t } = useI18n()
const configStore = useConfigStore()
const condition = ref<IReqPage>({
  name: '',
  page: 1,
  limit: 10,
})
const state = ref<{ data: ITestCase[]; dataCount: number }>({
  data: [],
  dataCount: 0,
})
const workspaceId = computed(() => {
  return configStore.getWorkspaceId()
})
// * 构建table column
const createColumns = (): DataTableColumns<ITestCase> => {
  return [
    {
      type: 'selection',
    },
    {
      title: t('commons.name'),
      key: 'name',
      align: 'center',
      minWidth: 120,
      ellipsis: true,
    },
    {
      title: t('test_track.case.case_desc'),
      key: 'remark',
      ellipsis: true,
      minWidth: 100,
    },
    {
      title: t('test_track.case.status'),
      key: 'reviewStatus',
      minWidth: 120,
    },
    {
      title: t('test_track.plan_view.execute_result'),
      key: 'lastExecuteResult',
      minWidth: 120,
    },
    {
      title: t('commons.tag'),
      key: 'tags',
      minWidth: 80,
    },
    {
      title: t('test_track.case.module'),
      key: 'nodePath',
      minWidth: 150,
      ellipsis: true,
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
const columns: DataTableColumns<ITestCase> = createColumns()
const rowKey = (row: ITestCase) => {
  return row.id as string
}
const handleAdd = () => {
  testcaseCreate.value?.openCreateCaseDialog()
}
const handleEdit = (row: ITestCase) => {
  editCase.value?.openEditCaseDialog(row)
}
const loadTableData = () => {
  loading.value = true
  condition.value.workspaceId = workspaceId.value
  testCasePageList(condition.value)
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
  <test-case-create ref="testcaseCreate" />
  <edit-case ref="editCase" />
</template>

<style scoped></style>
