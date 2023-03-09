import { createI18n } from 'vue-i18n'

import en from '../languages/en-US.json'
import zhCN from '../languages/zh-CN.json'

type MessageSchema = typeof zhCN

const i18n = createI18n<[MessageSchema], 'zh-CN' | 'en-US'>({
  legacy: false, // 要把 legacy 設為 false，才可以使用 Composition API
  locale: 'zh-CN',
  fallbackLocale: 'zh-CN',
  globalInjection: true,
  messages: {
    'zh-CN': zhCN,
    'en-US': en,
  },
})

export default i18n
