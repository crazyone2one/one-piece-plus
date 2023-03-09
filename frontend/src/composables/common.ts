import { ref, onMounted, nextTick } from 'vue'

/**
 * 获取页面高度
 * @returns 页面高度
 */
export const useClientHeight = () => {
  const height = ref(
    document.documentElement.clientHeight || document.body.clientHeight
  )
  onMounted(() => {
    nextTick(() => {
      window.onresize = () => {
        return () => {
          height.value =
            document.documentElement.clientHeight || document.body.clientHeight
        }
      }
    })
  })
  return { height }
}
