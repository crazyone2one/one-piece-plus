<script setup lang="ts">
import { computed } from 'vue'
import { NInput, NButton, NIcon, NPopover } from 'naive-ui'
import { IReqPage } from '/@/apis/interface'
interface IProps {
  condition: IReqPage
  showCreate?: boolean
}
const props = withDefaults(defineProps<IProps>(), {
  showCreate: true,
})
const emits = defineEmits(['update:condition', 'search', 'create'])
const condition = computed({
  get: () => props.condition,
  set: (val) => {
    emits('update:condition', val)
  },
})
</script>
<template>
  <div class="flex justify-center">
    <div class="-mb-5">
      <n-popover trigger="hover" :show-arrow="false">
        <template #trigger>
          <n-button v-if="showCreate" type="info" text @click="emits('create')">
            <n-icon :size="24">
              <span class="i-mdi:plus-circle-outline"></span>
            </n-icon>
          </n-button>
        </template>
        <span>{{ $t('commons.create') }}</span>
      </n-popover>

      <slot name="button"></slot>
    </div>
    <div class="ml-auto" f-c-c>
      <div>
        <n-input
          v-model:value="condition.name"
          type="text"
          size="small"
          :placeholder="$t('commons.search_by_name')"
          clearable
          @blur="emits('search')"
        />
      </div>
      <div>
        <slot name="otherSearchComp" />
      </div>

      <slot name="afterButton" />
    </div>
  </div>
</template>

<style scoped></style>
