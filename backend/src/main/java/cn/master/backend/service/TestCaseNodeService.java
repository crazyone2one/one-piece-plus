package cn.master.backend.service;

import cn.master.backend.entity.TestCaseNode;
import cn.master.backend.entity.TestCaseNodeDTO;
import cn.master.backend.request.DragNodeRequest;
import cn.master.backend.request.QueryTestCaseRequest;
import cn.master.backend.request.QueryTestPlanCaseRequest;
import cn.master.backend.util.NodeTreeUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-11
 */
public interface TestCaseNodeService extends IService<TestCaseNode> {

    /**
     * desc: search node tree by project id
     *
     * @param projectId project id
     * @return java.util.List<cn.master.backend.entity.TestCaseNodeDTO>
     */
    List<TestCaseNodeDTO> getNodeTreeByProjectId(String projectId);

    /**
     * desc: search node tree by project id
     *
     * @param projectId project id
     * @param request   parameters
     * @return java.util.List<cn.master.backend.entity.TestCaseNodeDTO>
     */
    List<TestCaseNodeDTO> getNodeTreeByProjectId(String projectId, QueryTestCaseRequest request);

    /**
     * desc: get default node
     *
     * @param projectId project id
     * @return cn.master.backend.entity.TestCaseNode
     */
    TestCaseNode getDefaultNode(String projectId);

    /**
     * desc: add test case node
     *
     * @param node parameter
     * @return java.lang.String
     */
    String addNode(TestCaseNode node);

    /**
     * desc: edit test case node
     *
     * @param request parameter
     * @return java.lang.String
     */
    String editNode(DragNodeRequest request);

    /**
     * desc: 根据计划查询node
     *
     * @param planId  计划id
     * @param request 查询条件
     * @return java.util.List<cn.master.backend.entity.TestCaseNodeDTO>
     */
    List<TestCaseNodeDTO> getNodeByPlanId(String planId, QueryTestPlanCaseRequest request);

    List<TestCaseNodeDTO> getRelatePlanNodes(QueryTestCaseRequest request);
}
