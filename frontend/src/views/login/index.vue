<script setup lang="ts">
import {
  FormInst,
  FormRules,
  NForm,
  NFormItem,
  NButton,
  NH1,
  NCheckbox,
  NInput,
  NCard,
} from 'naive-ui'
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import CryptoJS from 'crypto-js'
import { IReqLogin, loginApi } from '/@/apis/user-api'
import { useUserStore } from '/@/stores/user'
import { useConfigStore } from '/@/stores/config'
import { useRoute, useRouter } from 'vue-router'
import { formatAxis } from '/@/utils/format-time'

const title = import.meta.env.VITE_APP_TITLE
const isRemember = ref(false)
const loading = ref(false)
const { t } = useI18n()
const userStore = useUserStore()
const configStore = useConfigStore()
const formRef = ref<FormInst | null>(null)
const model = ref<{ name: string | null; password: string | null }>({
  name: 'admin',
  password: '123456',
})
const rules: FormRules = {
  name: [
    {
      required: true,
      message: t('commons.input_login_username'),
      trigger: 'blur',
    },
  ],
  password: [
    {
      required: true,
      message: t('commons.input_password'),
      trigger: 'blur',
    },
    {
      min: 6,
      max: 30,
      message: t('commons.input_limit', [6, 30]),
      trigger: 'blur',
    },
  ],
}
const route = useRoute()
const router = useRouter()
const currentTime = computed(() => {
  return formatAxis(new Date())
})
/**
 * 登录功能
 */
const handleLogin = () => {
  formRef.value?.validate((error) => {
    if (!error) {
      loading.value = true
      const param: IReqLogin = { name: '', password: '' }
      param.name = model.value.name as string
      param.password = CryptoJS.MD5(model.value.password as string).toString()
      loginApi(param)
        .then((resp) => {
          const { authorities, lastProjectId, lastWorkspaceId } = resp.data
          const _authorities: string[] = []
          authorities.forEach((authority) => {
            _authorities.push(authority.authority)
          })
          userStore.$patch((state) => {
            state.user.id = resp.data.userId as string
            state.user.token = resp.data.token as string
            state.user.username = resp.data.username as string
            state.user.authorities = _authorities
          })
          configStore.resetcommonStoreStore()
          const currentProjectId = configStore.getProjectId()
          if (!currentProjectId) {
            configStore.setProjectId(lastProjectId)
          } else {
            // TODO:根据用户的权限设置project id
          }
          if (!configStore.getWorkspaceId()) {
            configStore.setWorkspaceId(lastWorkspaceId)
          }
          loading.value = false
          if (route.query?.redirect) {
            router.push({
              path: <string>route.query?.redirect,
              query: route.query?.params
                ? Object.keys(<string>route.query?.params).length > 0
                  ? JSON.parse(<string>route.query?.params)
                  : ''
                : '',
            })
          } else {
            router.push('/')
          }
          window.$notification.success({
            content: currentTime.value,
            duration: 2500,
          })
        })
        .finally(() => {
          loading.value = false
        })
    }
  })
}
</script>
<template>
  <div class="flex min-h-full items-center justify-center login-container">
    <n-card class="max-w-md space-y-8 shadow-xl">
      <div class="flex flex-col items-center justify-around">
        <img
          class="h-12 w-auto"
          src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600"
          alt="Your Company"
        />
        <n-h1
          class="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900"
        >
          {{ title }}
        </n-h1>
      </div>
      <n-form ref="formRef" :model="model" :rules="rules">
        <n-form-item path="name">
          <n-input
            v-model:value="model.name"
            autofocus
            :placeholder="$t('commons.account')"
            :maxlength="20"
          >
            <template #prefix>
              <span class="i-mdi:account" />
            </template>
          </n-input>
        </n-form-item>
        <n-form-item path="password">
          <n-input
            v-model:value="model.password"
            type="password"
            show-password-on="mousedown"
            :placeholder="$t('commons.password')"
            :maxlength="20"
            @keyup.enter="handleLogin"
          >
            <template #prefix>
              <span class="i-mdi:account-lock" />
            </template>
          </n-input>
        </n-form-item>
        <div>
          <n-checkbox
            :checked="isRemember"
            label="记住我"
            :on-update:checked="(val:boolean) => (isRemember = val)"
          />
        </div>

        <div class="py-2">
          <n-button
            type="primary"
            :loading="loading"
            class="group relative flex w-full justify-center rounded-md"
            @click="handleLogin"
          >
            登 录
          </n-button>
        </div>
      </n-form>
    </n-card>
  </div>
</template>

<style scoped>
.login-container {
  background-image: url('/@/assets/login_bg.svg');
}
</style>
