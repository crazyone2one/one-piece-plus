import { IResultData } from './interface'
import service from '/@/utils/http-common'
export interface ICaseTemplate {
  id: string
  name: string
  type: string
  description: string
  caseName: string
  system: boolean
  global: boolean
  prerequisite: string
  stepDescription: any
  expectedResult: any
  actualResult: any
  createTime: string
  updateTime: string
  stepModel: any
  steps: any
  createUser: any
  projectId: string
}
export const getTestTemplate = async (
  projectId: string
): Promise<IResultData<ICaseTemplate>> => {
  return await service.get('field/template/case/get/relate/' + projectId)
}
