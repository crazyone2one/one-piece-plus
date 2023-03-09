import { IReqPage, IResultData, IPageResponse } from './interface'
import { IUserInfo } from './user-api'
import service from '/@/utils/http-common'
import { ICustomFieldTemplate } from './template-api'
import { ITreeNode } from './test-case-node'

export interface IProject {
  id: string
  name: string
  description: string
  memberSize?: number
  createUserName?: string
  createTime?: string
  updateTime?: string
  platform?: string
  thirdPartTemplate?: string
  tapdId?: string
  caseTemplateId?: string | null
  issueTemplateId?: string | null
  workspaceId?: string
  createUser?: string
  [key: string]: string | number | null | undefined
}
interface IReqParams extends IReqPage {
  id?: string
}
/**
 * * 查询列表数据接口
 * @param params
 * @returns
 */
export const getProjectPageData = async (
  params: IReqParams
): Promise<IResultData<IPageResponse<IProject>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(`/project/list/${page}/${limit}`, params)
}

export const getProjectMemberListData = async (
  workspaceId: string,
  param: unknown
): Promise<IResultData<IUserInfo[]>> => {
  return await service.post(
    `/user/ws/project/member/list/${workspaceId}`,
    param
  )
}
/**
 * save project
 * @param param
 * @returns
 */
export const saveProject = async (
  param: IProject
): Promise<IResultData<IProject>> => {
  return await service.post(`/project/add`, param)
}
/**
 * modify project
 * @param param
 * @returns
 */
export const modifyProject = async (
  param: IProject
): Promise<IResultData<IProject>> => {
  return await service.post(`/project/update`, param)
}
/**
 * delete project
 * @param projectId projectId
 * @returns
 */
export const delProjectById = (projectId: string) => {
  return service.get(`/project/delete/${projectId}`)
}

export const getUserProjectList = (param: {
  userId: string
  workspaceId: string
}): Promise<IResultData<IProject[]>> => {
  return service.post(`/project/list/related`, param)
}

export const switchProject = async (param: {
  id: string
  lastProjectId: string
}): Promise<IResultData<IUserInfo>> => {
  return await service.post(`/user/update/current`, param)
}

export const getNodeByProjectId = async (
  id: string
): Promise<IResultData<ITreeNode[]>> => {
  return await service.post(`/case/node/list/${id}`, {})
}

export const getProjectListAll = (): Promise<IResultData<IProject[]>> => {
  return service.get(`/project/list/all`)
}
/**
 * 查询项目相关测试用例模板
 * @param projectId
 * @returns
 */
export const getFieldTemplateCaseOption = async (
  projectId: string
): Promise<IResultData<ICustomFieldTemplate[]>> => {
  return await service.post(`/field/template/case/list/all/${projectId}`, {})
}

export const getProjectById = async (
  projectId: string
): Promise<IResultData<IProject>> => {
  return await service.get(`/project/get/${projectId}`)
}
