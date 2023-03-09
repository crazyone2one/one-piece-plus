import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import { useConfigStore } from '../stores/config'
import { useUserStore } from '../stores/user'
import i18n from '../plugins/i18n'
// Define some routes
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('/@/views/login/index.vue'),
    meta: { title: 'commons.login' },
  },
  {
    path: '/',
    redirect: '/dashboard',
    component: () => import('/@/components/layout/index.vue'),
    children: [
      {
        path: '/dashboard',
        component: () => import('../views/dashboard/index.vue'),
        meta: {
          title: 'commons.workspace',
          permissions: ['SYSTEM_WORKSPACE:READ'],
        },
      },
      {
        path: '/track',
        name: 'Track',
        redirect: '/track/home',
        component: () => import('/@/views/track/index.vue'),
        meta: {
          title: 'test_track.test_track',
          permissions: ['SYSTEM_WORKSPACE:READ'],
        },
        children: [
          {
            path: '/track/home',
            name: 'TrackHome',
            component: () => import('/@/views/track/home/index.vue'),
            meta: {
              title: 'commons.home',
              permissions: ['SYSTEM_WORKSPACE:READ'],
            },
          },
          {
            path: '/track/case/all',
            name: 'TrackCase',
            component: () => import('/@/views/track/case/index.vue'),
            meta: {
              title: 'test_track.case.test_case',
              permissions: ['SYSTEM_WORKSPACE:READ'],
            },
          },
        ],
      },
      {
        path: '/setting/workspace',
        name: 'Workspace',
        component: () => import('/@/views/system-setting/workspace/index.vue'),
        meta: {
          title: 'commons.workspace',
          permissions: ['SYSTEM_WORKSPACE:READ'],
        },
      },
      {
        path: '/setting/project',
        name: 'Project',
        component: () => import('/@/views/system-setting/project/index.vue'),
        meta: {
          title: 'commons.project',
          permissions: ['SYSTEM_WORKSPACE:READ'],
        },
      },
    ],
  },
]
// Create the router instance and pass the `routes` option
const router = createRouter({
  // Provide the history implementation to use. We are using the hash history for simplicity here.
  history: createWebHashHistory(),
  routes,
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 设置标题
  const title = i18n.global.t(to.meta.title as string)
  document.title = `${title} | ${import.meta.env.VITE_APP_TITLE}`
  const userStore = useUserStore()
  const configStore = useConfigStore()
  const token = userStore.getToken()
  if (to.path === '/login' && !token) {
    next()
  } else {
    if (!token) {
      next(
        `/login?redirect=${to.path}&params=${JSON.stringify(
          to.query ? to.query : to.params
        )}`
      )
      configStore.resetcommonStoreStore()
    } else if (token && to.path === '/login') {
      next('/')
    } else {
      next()
    }
  }
})

export default router
