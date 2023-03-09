export interface ITreeNode {
  id: string
  key: string
  projectId: string
  name: string
  parentId: string
  level: number
  createTime?: string
  updateTime?: string
  pos: number
  label: string
  children: ITreeNode[]
  caseNum: number
  parentName?: string
  path?: string
  [key: string]: string | number | ITreeNode[] | undefined
}
import { IResultData } from './interface'
import service from '/@/utils/http-common'
/**
 * 添加节点
 * @param node 节点参数
 * @returns
 */
export const addNode = async (node: any): Promise<IResultData<string>> => {
  return await service.post(`/case/node/add`, node)
}
/**
 * 编辑节点
 * @param node 节点参数
 * @returns
 */
export const editNode = async (node: any): Promise<IResultData<string>> => {
  return await service.post(`/case/node/edit`, node)
}

export const testCaseNodeListPlanRelate = async (param: any) => {
  return await service.post(`/case/node/list/plan/relate`, param)
}
/**
 * 查询对应项目下节点
 * @param projectId project id
 * @returns
 */
export const getNodeDataByProjectId = async (
  projectId: string
): Promise<IResultData<ITreeNode[]>> => {
  return await service.get(`/case/node/list/${projectId}`)
}
