export const resetModel = (obj: { [key: string]: any }) => {
  if (obj) {
    Object.keys(obj).forEach((key) => {
      if (typeof obj[key] === 'string') {
        obj[key] = ''
      }
      if (typeof obj[key] === 'number') {
        obj[key] = 0
      }
      if (typeof obj[key] === 'object') {
        resetModel(obj[key])
      }
    })
    return obj
  }
}
