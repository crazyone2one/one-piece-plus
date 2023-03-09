<script setup lang="ts">
import { h } from 'vue'
import { NSpace, NDropdown, NAvatar, NText, NButton } from 'naive-ui'
import { useUserStore } from '/@/stores/user'
import { useI18n } from 'vue-i18n'
import { logoutApi } from '/@/apis/user-api'
import { useRouter, useRoute } from 'vue-router'
import Workspace from '/@/components/layout/components/header/WorkspaceComp.vue'
const renderCustomHeader = () => {
  return h(
    'div',
    {
      style: 'display: flex; align-items: center; padding: 8px 12px;',
    },
    [
      h(NAvatar, {
        round: true,
        style: 'margin-right: 12px;',
        src: 'https://07akioni.oss-cn-beijing.aliyuncs.com/demo1.JPG',
      }),
      h('div', null, [
        h('div', null, [h(NText, { depth: 2 }, { default: () => '打工仔' })]),
        h('div', { style: 'font-size: 12px;' }, [
          h(
            NText,
            { depth: 3 },
            { default: () => '毫无疑问，你是办公室里最亮的星' }
          ),
        ]),
      ]),
    ]
  )
}
const { t } = useI18n()
const user = useUserStore()
const router = useRouter()
const route = useRoute()
const options = [
  {
    key: 'header',
    type: 'render',
    render: renderCustomHeader,
  },
  {
    key: 'header-divider',
    type: 'divider',
  },
  {
    label: '处理群消息 342 条',
    key: 'stmt1',
  },
  {
    label: '被 @ 58 次',
    key: 'stmt2',
  },
  {
    key: 'header-divider',
    type: 'divider',
  },
  {
    label: t('commons.exit_system'),
    key: 'stmt3',
  },
]

const handleSelect = (key: string) => {
  if (key === 'stmt3') {
    // 退出系统
    window.$dialog.warning({
      content: '此操作将退出登录, 是否继续？',
      positiveText: '确定',
      negativeText: '不确定',
      maskClosable: false,
      showIcon: false,
      onPositiveClick: () => {
        setTimeout(() => {
          logoutApi().then(() => {
            user.resetAuthStore()
            router.replace({
              name: 'Login',
              query: {
                redirect: route.fullPath,
              },
            })
          })
        }, 1000)
      },
    })
  }
}
</script>
<template>
  <n-space class="ml-auto items-center" inline>
    <div>
      <workspace />
    </div>
    <div>
      <n-button
        text
        tag="a"
        href="https://github.com/crazyone2one/one-piece.git"
        target="_blank"
      >
        Github
      </n-button>
    </div>
    <div>
      <n-dropdown trigger="hover" :options="options" @select="handleSelect">
        <n-button text>{{ user.user.username }}</n-button>
      </n-dropdown>
    </div>
  </n-space>
</template>

<style scoped></style>
