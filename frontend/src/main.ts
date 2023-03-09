import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './routers'
// 通用字体
import 'vfonts/Lato.css'
// 等宽字体
import 'vfonts/FiraCode.css'
import 'uno.css'
import i18n from './plugins/i18n'
import pinia from './plugins/pinia'
import mitt, { Emitter } from 'mitt'

const app = createApp(App)
app.use(router).use(i18n).use(pinia)
// 定义类型别名，因全局使用并且需要自定义事件名称，所以使用索引签名定义内容
type Events = {
  [propName: string]: any
}
const Mit: Emitter<Events> = mitt<Events>()

// 全局声明 获取mitt所有的类型
declare module 'vue' {
  export interface ComponentCustomProperties {
    $Bus: typeof Mit
  }
}
app.config.globalProperties.$Bus = Mit
app.mount('#app')
