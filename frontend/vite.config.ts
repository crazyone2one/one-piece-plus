import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import UnoCSS from 'unocss/vite'
import presetIcons from '@unocss/preset-icons'
import { presetAttributify, presetUno } from 'unocss'
import fs from 'node:fs/promises'

const pathResolve = (dir: string) => {
  return path.resolve(__dirname, '.', dir)
}
// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // 根据当前工作目录中的 `mode` 加载 .env 文件
  // 设置第三个参数为 '' 来加载所有环境变量，而不管是否有 `VITE_` 前缀。
  const env = loadEnv(mode, process.cwd(), '')
  return {
    // vite 配置
    plugins: [
      vue(),
      UnoCSS({
        presets: [
          presetAttributify({
            /* preset options */
          }),
          presetUno(),
          // https://github.com/unocss/unocss/tree/main/packages/preset-icons#configuration
          presetIcons({
            extraProperties: {
              display: 'inline-block',
              'vertical-align': 'middle',
            },
            collections: {
              custom: {
                alarm:
                  '<svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"><path fill="currentColor" d="M12 20a7 7 0 0 1-7-7a7 7 0 0 1 7-7a7 7 0 0 1 7 7a7 7 0 0 1-7 7m0-16a9 9 0 0 0-9 9a9 9 0 0 0 9 9a9 9 0 0 0 9-9a9 9 0 0 0-9-9m.5 4H11v6l4.75 2.85l.75-1.23l-4-2.37V8M7.88 3.39L6.6 1.86L2 5.71l1.29 1.53l4.59-3.85M22 5.72l-4.6-3.86l-1.29 1.53l4.6 3.86L22 5.72Z"/></svg>',
                /* ... */
                search:
                  '<svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"><path fill="currentColor" d="M7 9H2V7h5v2zm0 3H2v2h5v-2zm13.59 7l-3.83-3.83c-.8.52-1.74.83-2.76.83c-2.76 0-5-2.24-5-5s2.24-5 5-5s5 2.24 5 5c0 1.02-.31 1.96-.83 2.75L22 17.59L20.59 19zM17 11c0-1.65-1.35-3-3-3s-3 1.35-3 3s1.35 3 3 3s3-1.35 3-3zM2 19h10v-2H2v2z"/></svg>',
              },

              'my-icons': {
                // load your custom icon lazily
                logo: () =>
                  fs.readFile('./src/assets/one-piece-logo.svg', 'utf-8'),
                /* ... */
                settings: () =>
                  fs.readFile('./src/assets/icons/settings.svg', 'utf-8'),
              },
            },
            // customizations: {
            //   iconCustomizer(collection, icon, props) {
            //     if (['custom', 'my-icons', 'mdi'].concat(collection)) {
            //       props.width = '2em'
            //       props.height = '2em'
            //     }
            //     // customize this icon in this collection
            //     if (collection === 'my-icons' && icon === 'settings') {
            //       props.width = '1em'
            //       props.height = '1em'
            //     }
            //   },
            // },
          }),
        ],
        shortcuts: [
          ['wh-full', 'w-full h-full'],
          ['f-c-c', 'flex justify-center items-center'],
          ['f-col-c-c', 'flex-col justify-center items-center'],
          ['flex-col', 'flex flex-col'],
          ['absolute-lt', 'absolute left-0 top-0'],
          ['absolute-lb', 'absolute left-0 bottom-0'],
          ['absolute-rt', 'absolute right-0 top-0'],
          ['absolute-rb', 'absolute right-0 bottom-0'],
          ['absolute-center', 'absolute-lt f-c-c wh-full'],
          ['text-ellipsis', 'truncate'],
        ],
      }),
    ],
    resolve: {
      alias: [
        // /@/xxxx => src/xxxx
        {
          find: /\/@\//,
          replacement: pathResolve('src') + '/',
        },
      ],
    },
    // 服务器选项
    server: {
      host: true,
      proxy: {
        // 带选项写法：http://localhost:5173/api/bar -> http://jsonplaceholder.typicode.com/bar
        [env.VITE_APP_BASE_API]: {
          target: env.VITE_APP_PROXY_URL,
          changeOrigin: true,
          rewrite: (path) =>
            path.replace(new RegExp('^' + env.VITE_APP_BASE_API), ''),
        },
      },
    },
    css: {
      // CSS模块化配置
      modules: {},
      // CSS预处理器配置
      preprocessorOptions: {},
    },
    // 构建选项
    build: {},
    define: {
      __APP_ENV__: env.APP_ENV,
      __VUE_I18N_FULL_INSTALL__: true,
      __VUE_I18N_LEGACY_API__: false,
      __INTLIFY_PROD_DEVTOOLS__: false,
    },
  }
})
