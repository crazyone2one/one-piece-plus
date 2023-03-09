/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_APP_BASEURL: string
  readonly VITE_APP_PROXY_URL: string
  // 更多环境变量...
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  import type {
    MessageProviderInst,
    DialogProviderInst,
    NotificationProviderInst,
  } from 'naive-ui'
  // 增加全局类型
  global {
    interface Window {
      $message: MessageProviderInst
      $dialog: DialogProviderInst
      $notification: NotificationProviderInst
    }
  }
  // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
  const component: DefineComponent<{}, {}, any>
  export default component
}
