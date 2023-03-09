import { IReqPage, IResultData, IPageResponse } from './interface'
import service from '/@/utils/http-common'

/**
 * 登录参数类型
 */
export interface IReqLogin {
  name: string
  password: string
}
export interface IUserInfo {
  id: string
  userId?: string
  email: string
  username?: string
  name: string
  phone: string
  token?: string
  password?: string
  status?: string
  lastWorkspaceId: string
  lastProjectId: string
  authorities: Array<{ authority: string }>
  source?: string
  isLocalUser?: boolean
}

interface IWsMember extends IReqPage {
  workspaceId: string
}

/**
 * login api methods
 * @param params login parameters
 * @returns
 */
export const loginApi = async (
  params: IReqLogin
): Promise<IResultData<IUserInfo>> => {
  return await service.post('/user/login', params)
}
/**
 * 退出功能api
 * @returns
 */
export const logoutApi = () => {
  return service.get('user/logout')
}
export const getWorkspaceMemberSpecial = async (
  param: object
): Promise<IResultData<IUserInfo[]>> => {
  return await service.post('/user/special/ws/member/list/all', param)
}
// * register api
export const registerApi = async (
  params: unknown
): Promise<IResultData<unknown>> => {
  return await service.post('/user/register', params)
}
// * update current user
export const updateCurrentUser = async (user: unknown) => {
  return await service.post('/user/update/current', user)
}
// * get current user
export const getCurrentUser = async (
  userId: string
): Promise<IResultData<IUserInfo>> => {
  return await service.get(`/user/current/${userId}`)
}

export const getWorkspaceMemberPages = async (
  params: IWsMember
): Promise<IResultData<IPageResponse<IUserInfo>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(`/user/ws/member/list/${page}/${limit}`, params)
}

export const getWorkspaceMemberGroup = (
  workspaceId: string,
  userId: string
) => {
  return service.get(`/user/group/list/ws/${workspaceId}/${userId}`)
}

/**
 *  列表数据查询*/
export const specialListUsers = async (
  params: IReqPage
): Promise<IResultData<IPageResponse<IUserInfo>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(`/user/special/list/${page}/${limit}`, params)
}

export const specialGetUserGroup = (userId: string) => {
  return service.get(`/user/special/user/group/${userId}`)
}
// * add user
export const specialCreateUser = (param: IUserInfo) => {
  return service.post('/user/special/add', param)
}
//* update user
export const specialModifyUser = (param: IUserInfo) => {
  return service.post('/user/special/update', param)
}
export const specialModifyUserDisable = async (param: IUserInfo) => {
  return await service.post('/user/special/update_status', param)
}
/**
 * 更新用户密码
 * @param param 参数
 */
export const specialModifyPassword = (param: {
  id: string
  newPassword: string
  confirmPassword: string
}) => {
  return service.post('/user/special/password', param)
}

export const getProjectMemberOption = (
  projectId: string
): Promise<IResultData<IUserInfo[]>> => {
  return service.get(`/user/project/member/option/${projectId}`)
}

export const getUserListByResourceUrl = (
  url: string
): Promise<IResultData<IUserInfo[]>> => {
  return service.get(url)
}

export const addWorkspaceMember = (member: any) => {
  return service.post('user/ws/member/add', member)
}

export const getWorkspaceMember = (
  param: any
): Promise<IResultData<IUserInfo[]>> => {
  return service.post(`user/special/ws/member/list/all`, param)
}

export const getProjectMemberPages = async (
  workspaceId: string,
  params: IReqPage
): Promise<IResultData<IUserInfo[]>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(
    `/user/ws/project/member/list/${workspaceId}/${page}/${limit}`,
    params
  )
}

export const addProjectMember = (param: any) => {
  return service.post('user/project/member/add', param)
}

export const delProjectMember = async (projectId: string, memberId: string) => {
  return await service.get(
    `/user/project/member/delete/${projectId}/${memberId}`
  )
}

export const specialDeleteUserById = async (userId: string) => {
  return await service.get(`/user/special/delete/${userId}`)
}
export const getUserGroupByResourceUrlAndPage = async (
  url: string,
  params: any
): Promise<IResultData<IPageResponse<IUserInfo>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(`${url}${page}/${limit}`, params)
}
