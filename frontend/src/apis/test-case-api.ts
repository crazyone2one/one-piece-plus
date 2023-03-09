import { IPageResponse, IReqPage, IResultData } from './interface'
import { ITreeNode } from './test-case-node'
import service from '/@/utils/http-common'

export interface ITestCaseStep {
  num: number
  desc: string
  result: string
}
export interface ITestCase {
  id: string
  name: string
  module: string
  nodePath: string
  maintainer: string
  priority: string
  type: string
  method: string
  prerequisite: string
  testId: string
  steps: ITestCaseStep[] | string
  stepDesc?: string
  stepResult?: string
  selected?: [] | string
  remark: string
  tags: string | string[]
  demandId: string
  demandName: string
  status: string
  reviewStatus: string
  stepDescription: string
  expectedResult: string
  stepModel: string
  customNum: string
  followPeople: string
  testName?: string
  follows?: string[]
  projectId?: string
  nodeId?: string
  lastExecuteResult?: string
  isCopy?: boolean
  copyCaseId?: string
  maintainerName?: string
  issuesSize?: number
}
export interface testCaseReviewTestCase {
  testCase: ITestCase
  id: string
  status: string
}
interface IReqParams extends IReqPage {
  id?: string
}
const BASE_URL = '/test/case/'

export const getTestCaseNodesByCaseFilter = (
  projectId: string,
  param: any
): Promise<IResultData<ITreeNode[]>> => {
  return service.post('/case/node/list/' + projectId, param)
}
/**
 * add test case
 * @param param parameters
 * @returns
 */
export const testCaseAdd = async (
  param: ITestCase
): Promise<IResultData<ITestCase>> => {
  return await service.post(BASE_URL + 'save', param)
}
export const testCasePageList = async (
  params: IReqParams
): Promise<IResultData<IPageResponse<ITestCase>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(BASE_URL + `/list/${page}/${limit}`, params)
}

export const getTestCaseById = async (
  id: string
): Promise<IResultData<ITestCase>> => {
  return await service.get(BASE_URL + `get/${id}`)
}
export const testCaseEdit = async (
  param: ITestCase
): Promise<IResultData<ITestCase>> => {
  return await service.post(BASE_URL + 'edit', param)
}

/**
 * delete case
 * @param caseId caseId
 * @returns
 */
export const testCaseDeleteToGc = (
  caseId: string
): Promise<IResultData<number>> => {
  return service.post(BASE_URL + `deleteToGc/${caseId}`)
}

export const getTestPlanTestCase = async (
  params: IReqParams
): Promise<IResultData<IPageResponse<any>>> => {
  const page = params.page
  const limit = params.limit
  return service.post(`/test/plan/case/list/${page}/${limit}`, params)
}

export const getTestPlanCaseNodesByCaseFilter = async (
  planId: string,
  param: any
) => {
  return await service.post('/case/node/list/plan/' + planId, param)
}

export const testCaseRelateList = async (
  param: IReqParams
): Promise<IResultData<IPageResponse<any>>> => {
  const page = param.page
  const limit = param.limit
  return await service.post(`/test/case/relate/${page}/${limit}`, param)
}

export const getTestCaseReviewsCasePage = async (
  param: IReqParams
): Promise<IResultData<IPageResponse<any>>> => {
  const page = param.page
  const limit = param.limit
  return await service.post(`/test/case/reviews/case/${page}/${limit}`, param)
}
/**
 * 查询评审任务关联的测试用例数据
 * @param param
 * @returns
 */
export const getTestReviewTestCase = async (
  param: any
): Promise<IResultData<IPageResponse<testCaseReviewTestCase>>> => {
  const page = param.page
  const limit = param.limit
  return await service.post(`/test/review/case/list/${page}/${limit}`, param)
}
