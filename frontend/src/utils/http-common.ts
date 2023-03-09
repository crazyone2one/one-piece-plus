import axios, { AxiosResponse } from 'axios'
import { useConfigStore } from '../stores/config'
import { useUserStore } from '../stores/user'

enum StatusCode {
  Unauthorized = 401,
  Forbidden = 403,
  TooManyRequests = 429,
  InternalServerError = 500,
}
const instance = axios.create({
  // `baseURL` will be prepended to `url` unless `url` is absolute.
  // It can be convenient to set `baseURL` for an instance of axios to pass relative URLs
  // to methods of that instance.
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // `withCredentials` indicates whether or not cross-site Access-Control requests
  // should be made using credentials
  withCredentials: true, // default
  timeout: 50000,
})
axios.defaults.headers.post['Access-Control-Allow-Origin-Type'] = '*'
// Add a request interceptor
instance.interceptors.request.use(
  (config) => {
    const appStore = useConfigStore()
    const userStore = useUserStore()
    const token = userStore.getToken()
    // const token = '112'
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
      config.headers['WORKSPACE'] = appStore.getWorkspaceId()
      config.headers['PROJECT'] = appStore.getProjectId()
    }
    return config
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error)
  }
)

// Add a response interceptor
instance.interceptors.response.use(
  function (response: AxiosResponse) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    const { responseType } = response.config
    if (responseType === 'blob') {
      return response
    }
    if (response.data.code === 200) {
      return Promise.resolve(response.data)
    } else {
      return Promise.reject(response)
    }
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    if (error.response?.status === StatusCode.InternalServerError) {
      window.$message?.error('系统错误，联系管理员')
    }
    if (
      [400, StatusCode.Unauthorized].includes(error.response?.status as number)
    ) {
      // eslint-disable-next-line @typescript-eslint/restrict-template-expressions
      window.$message?.error(`${error.response?.data.message}`)
    }
    if (error.response.status === StatusCode.Forbidden) {
      const userStore = useUserStore()
      window.$dialog?.warning({
        title: '',
        showIcon: false,
        content: '登录超时',
        positiveText: '重新登录',
        negativeText: '不确定',
        maskClosable: false,
        onPositiveClick: async () => {
          userStore.resetAuthStore()
          window.location.href = '/'
        },
      })
    }
    return Promise.reject(error)
  }
)
export default instance
