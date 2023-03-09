import { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/setting',
    component: () => import('/@/components/layout/index.vue'),
    children: [
      {
        path: '/setting/workspace',
        component: () => import('/@/views/system-setting/workspace/index.vue'),
      },
    ],
  },
]

export default routes
