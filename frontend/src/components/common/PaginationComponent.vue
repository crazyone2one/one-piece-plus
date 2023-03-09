<script setup lang="ts">
import { computed } from 'vue'
import { NPagination } from 'naive-ui'
import { IReqPage } from '/@/apis/interface'

interface IProps {
  condition: IReqPage
  itemCount?: number
}
const props = withDefaults(defineProps<IProps>(), {
  itemCount: 0,
})
const emits = defineEmits(['update:condition', 'updatePage', 'updatePageSize'])
const condition = computed({
  get: () => props.condition,
  set: (val) => {
    emits('update:condition', val)
  },
})
// * 翻页
const handleUpdatePage = (page: number) => {
  emits('updatePage', page)
}
// * 改变每页显示数量
const handleUpdatePageSize = (pageSize: number) => {
  emits('updatePageSize', pageSize)
}
</script>
<template>
  <div mt-5>
    <n-pagination
      v-model:page="condition.page"
      v-model:page-size="condition.limit"
      :page-sizes="[10, 20, 30, 40]"
      show-size-picker
      :item-count="itemCount"
      class="justify-end"
      @update-page="handleUpdatePage"
      @update-page-size="handleUpdatePageSize"
    />
  </div>
</template>

<style scoped></style>
