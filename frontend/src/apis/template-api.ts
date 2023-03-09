import { ICustomField } from './custom-field-api'
import { IReqPage, IResultData, IPageResponse } from './interface'
import service from '/@/utils/http-common'
export interface ISteps {
  num: number
  desc: string
  result: string
}
// * interface of test case template
export interface ITestCaseTemplate {
  id: string
  name: string
  description: string
  type: string
  caseName?: string
  system: boolean
  global?: boolean
  prerequisite: string
  stepDescription: string
  expectedResult: string
  actualResult: string
  stepModel: string
  steps: ISteps[] | string
  projectId?: string
  createUser?: string
  createTime?: Date
  updateTime?: Date
  options?: any
  platform?: string
  customFields?: ICustomField[]
  customFieldIds?: []
}
// * interface of custom field templates
export interface ICustomFieldTemplate {
  id: string
  fieldId: string
  templateId: string
  scene?: string
  required: boolean
  order: number
  defaultValue: string
  customData: string
  key: string
  name?: string
  type?: string
  remark?: string
  options?: string
  system?: boolean
  platform?: string
}
/**
 * test case template page list api
 * @param params parameters
 * @returns
 */
export const getCaseFieldTemplateData = (
  params: IReqPage
): Promise<IResultData<IPageResponse<ITestCaseTemplate>>> => {
  const page = params.page
  const limit = params.limit
  return service.post(`/field/template/case/list/${page}/${limit}`, params)
}

export const handleResourceSave = (url: string, param: ITestCaseTemplate) => {
  return service.post(url, param)
}

export const getCustomFieldTemplates = (
  params: any
): Promise<IResultData<ICustomFieldTemplate[]>> => {
  return service.post('/custom/field/template/list', params)
}
export const getCustomFieldDefault = (
  params: any
): Promise<IResultData<ICustomField[]>> => {
  return service.post('/custom/field/default', params)
}

export const getCustomFieldRelateData = async (
  params: any
): Promise<IResultData<ICustomField>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(
    `/custom/field/list/relate/${page}/${limit}`,
    params
  )
}
export const getCustomFieldsData = async (
  param: any
): Promise<IResultData<ICustomField[]>> => {
  return await service.post('custom/field/list', param)
}
