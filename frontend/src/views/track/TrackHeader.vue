<script setup lang="ts">
import { MenuOption, NMenu } from 'naive-ui'
import { ref, h } from 'vue'
import { useI18n } from 'vue-i18n'
import { RouterLink, useRoute } from 'vue-router'

const { t } = useI18n()
const route = useRoute()
const activeKey = ref<string | null>(route.path.split('/')[2])
const menuOptions: MenuOption[] = [
  {
    label: () =>
      h(
        RouterLink,
        {
          to: {
            name: 'TrackHome',
          },
        },
        { default: () => t('i18n.home') }
      ),

    key: 'home',
  },
  {
    label: () =>
      h(
        RouterLink,
        {
          to: {
            name: 'TrackCase',
          },
        },
        { default: () => t('test_track.case.test_case') }
      ),
    key: 'case',
  },
  {
    label: t('test_track.review.test_review'),
    key: 'a-wild-sheep-chase',
    disabled: true,
  },
  {
    label: t('test_track.plan.test_plan'),
    key: 'a-wild-sheep-chase',
    disabled: true,
  },

  {
    label: t('commons.report'),
    key: 'a-wild-sheep-chase',
    disabled: true,
  },
]
const handleSelect = (key: string) => {
  activeKey.value = key
}
</script>

<template>
  <div id="menu-bar">
    <n-menu
      v-model:value="activeKey"
      mode="horizontal"
      :options="menuOptions"
      @update:value="handleSelect"
    />
  </div>
</template>

<style scoped>
#menu-bar {
  border-bottom: 1px solid #e6e6e6;
  background-color: #fff;
}
</style>
