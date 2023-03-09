import { IReqPage, IResultData, IPageResponse } from './interface'
import service from '/@/utils/http-common'

export interface IOption {
  value: string
  label: string
}
export interface ICustomField {
  id?: string | null
  name: string
  scene: string
  type: string
  remark: string
  options: string | IOption[]
  system: boolean
  global?: boolean
  projectId: string
  third_part?: boolean
  fieldId?: string
  defaultValue?: string[] | string
  required?: boolean
  key?: string
  [key: string]: string | string[] | boolean | null | IOption[] | undefined
}
export const stageOptions = [
  { label: '单元测试', value: 'unit' },
  { label: '冒烟测试', value: 'smoke' },
  { label: '系统测试', value: 'system' },
  { label: '回归测试', value: 'reback' },
  { label: '验收测试', value: 'review' },
]
/**
 * * 查询列表
 * @param params 查询参数
 * @returns
 */
export const getCustomFieldPages = (
  params: IReqPage
): Promise<IResultData<IPageResponse<ICustomField>>> => {
  const page = params.page
  const limit = params.limit
  return service.post(`/custom/field/list/${page}/${limit}`, params)
}

export const modifyFieldTemplateByUrl = async (
  url: string,
  params: ICustomField
): Promise<IResultData<string>> => {
  return await service.post(url, params)
}
/**
 * * delete custom field by id
 * @param id id
 * @returns
 */
export const deleteCustomField = async (id: string) => {
  return await service.get(`/custom/field/delete/${id}`)
}
