import { defineStore } from 'pinia'
import { ref, reactive } from 'vue'

export const useAppStore = defineStore(
  'main',
  () => {
    const someState = ref('hello pinia')
    const lang = reactive({
      // 默认语言，可选值<zh-cn|en>
      defaultLang: 'zh-CN',
      // 当在默认语言包找不到翻译时，继续在 fallbackLang 语言包内查找翻译
      fallbackLang: 'zh-CN',
      // 支持的语言列表
      langArray: [
        { value: 'zh-CN', label: '中文简体' },
        { value: 'en-US', label: 'English' },
      ],
    })
    const setLang = (val: string) => {
      lang.defaultLang = val
    }
    return { someState, lang, setLang }
  },
  {
    persist: true,
  }
)
