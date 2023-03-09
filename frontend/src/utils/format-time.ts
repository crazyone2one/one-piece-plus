/**
 * 时间问候语
 * @param param 当前时间，new Date() 格式
 * @returns 返回拼接后的时间字符串
 */
export const formatAxis = (param: Date): string => {
  const hour: number = new Date(param).getHours()
  if (hour < 6) return '凌晨好'
  else if (hour < 9) return '早上好'
  else if (hour < 12) return '上午好'
  else if (hour < 14) return '中午好'
  else if (hour < 17) return '下午好'
  else if (hour < 19) return '傍晚好'
  else if (hour < 22) return '晚上好'
  else return '夜里好'
}
