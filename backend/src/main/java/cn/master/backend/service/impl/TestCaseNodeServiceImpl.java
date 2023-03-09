package cn.master.backend.service.impl;

import cn.master.backend.constants.TestCaseConstants;
import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.TestCaseNode;
import cn.master.backend.entity.TestCaseNodeDTO;
import cn.master.backend.entity.TreeNodeDTO;
import cn.master.backend.exception.CustomException;
import cn.master.backend.mapper.TestCaseNodeMapper;
import cn.master.backend.request.DragNodeRequest;
import cn.master.backend.request.QueryTestCaseRequest;
import cn.master.backend.request.QueryTestPlanCaseRequest;
import cn.master.backend.service.SysProjectService;
import cn.master.backend.service.TestCaseNodeService;
import cn.master.backend.service.TestPlanService;
import cn.master.backend.util.NodeTreeUtils;
import cn.master.backend.util.SessionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-11
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TestCaseNodeServiceImpl extends ServiceImpl<TestCaseNodeMapper, TestCaseNode> implements TestCaseNodeService {
    final SysProjectService sysProjectService;
    final TestPlanService testPlanService;
    NodeTreeUtils<TestCaseNodeDTO> nodeTreeUtils = new NodeTreeUtils<>(TestCaseNodeDTO.class);

    @Override
    public List<TestCaseNodeDTO> getNodeTreeByProjectId(String projectId) {
        return getNodeTreeByProjectId(projectId, new QueryTestCaseRequest());
    }

    @Override
    public List<TestCaseNodeDTO> getNodeTreeByProjectId(String projectId, QueryTestCaseRequest request) {
        getDefaultNode(projectId);
        request.setProjectId(projectId);
        request.setUserId(SessionUtils.getUserId());
        request.setNodeIds(null);
        List<TestCaseNodeDTO> countNodes = baseMapper.getCountNodes(request);
        List<TestCaseNodeDTO> testCaseNodes = baseMapper.getNodeTreeByProjectId(projectId);
        return nodeTreeUtils.getNodeTrees(testCaseNodes, nodeTreeUtils.getCountMap(countNodes));
    }

    @Override
    public TestCaseNode getDefaultNode(String projectId) {
        LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCaseNode::getProjectId, projectId).eq(TestCaseNode::getName, "未规划用例").isNull(TestCaseNode::getParentId);
        List<TestCaseNode> nodes = baseMapper.selectList(wrapper);
        //        判断当前项目下是否有默认模块，没有添加默认模块
        if (CollectionUtils.isEmpty(nodes)) {
            TestCaseNode caseNode = TestCaseNode.builder().projectId(projectId).createUser(SessionUtils.getUserId()).name("未规划用例").pos(1.0).level(1).build();
            baseMapper.insert(caseNode);
            caseNode.setCaseNum(0);
            return caseNode;
        } else {
            return nodes.get(0);
        }
    }

    @Override
    public String addNode(TestCaseNode node) {
        validateNode(node);
        node.setCreateUser(SessionUtils.getUserId());
        double pos = getNextLevelPos(node.getProjectId(), node.getLevel(), node.getParentId());
        node.setPos(pos);
        baseMapper.insert(node);
        return node.getId();
    }

    @Override
    public String editNode(DragNodeRequest request) {
        checkTestCaseNodeExist(request);
        if (CollectionUtils.isNotEmpty(request.getNodeIds())) {

        }
        baseMapper.updateById(request);
        return request.getId();
    }

    @Override
    public List<TestCaseNodeDTO> getNodeByPlanId(String planId, QueryTestPlanCaseRequest request) {
        request.setPlanId(planId);
        request.setProjectId(null);
        request.setNodeIds(null);
        List<TestCaseNodeDTO> countModules = baseMapper.getTestPlanCountNodes(request);
        return getNodeTreeWithPruningTree(countModules);
    }

    @Override
    public List<TestCaseNodeDTO> getRelatePlanNodes(QueryTestCaseRequest request) {
        request.setNodeIds(null);
        if (testPlanService.isAllowedRepeatCase(request.getPlanId())) {
            request.setRepeatCase(true);
        }
        List<TestCaseNodeDTO> countNodes = baseMapper.getTestPlanRelateCountNodes(request);
        List<TestCaseNodeDTO> testCaseNodes = baseMapper.getNodeTreeByProjectId(request.getProjectId());
        return nodeTreeUtils.getNodeTreeWithPruningTreeByCaseCount(testCaseNodes, nodeTreeUtils.getCountMap(countNodes));
    }

    private List<TestCaseNodeDTO> getNodeTreeWithPruningTree(List<TestCaseNodeDTO> countModules) {
        return nodeTreeUtils.getNodeTreeWithPruningTree(countModules, baseMapper::getNodeTreeByProjectIds);
    }

    private double getNextLevelPos(String projectId, Integer level, String parentId) {
        List<TestCaseNode> list = getPos(projectId, level, parentId);
        if (!CollectionUtils.isEmpty(list) && list.get(0) != null && list.get(0).getPos() != null) {
            return (double) list.get(0).getPos() + NodeTreeUtils.DEFAULT_POS;
        } else {
            return NodeTreeUtils.DEFAULT_POS;
        }
    }

    private List<TestCaseNode> getPos(String projectId, Integer level, String parentId) {
        LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCaseNode::getProjectId, projectId).eq(TestCaseNode::getLevel, level);
        if (level != 1 && StringUtils.isNotBlank(parentId)) {
            wrapper.eq(TestCaseNode::getParentId, parentId);
        }
        wrapper.orderByDesc(TestCaseNode::getPos);
        return baseMapper.selectList(wrapper);
    }

    /**
     * desc: validate node data
     *
     * @param node node parameter
     */
    private void validateNode(TestCaseNode node) {
        if (node.getLevel() > TestCaseConstants.MAX_NODE_DEPTH) {
            throw new RuntimeException("模块树最大深度为" + TestCaseConstants.MAX_NODE_DEPTH + "层");
        }
        checkTestCaseNodeExist(node);
    }

    private void checkTestCaseNodeExist(TestCaseNode node) {
        if (Objects.nonNull(node.getName())) {
            LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCaseNode::getName, node.getName()).eq(TestCaseNode::getProjectId, node.getProjectId());
            if (StringUtils.isNoneBlank(node.getParentId())) {
                wrapper.eq(TestCaseNode::getParentId, node.getParentId());
            } else {
                wrapper.eq(TestCaseNode::getLevel, node.getLevel());
            }
            wrapper.ne(StringUtils.isNotBlank(node.getId()), TestCaseNode::getId, node.getId());
            if (baseMapper.exists(wrapper)) {
                throw new CustomException("test_case_module_already_exists");
            }
        }
    }
}
