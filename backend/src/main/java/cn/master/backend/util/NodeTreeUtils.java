package cn.master.backend.util;

import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.TreeNodeDTO;
import cn.master.backend.mapper.SysProjectMapper;
import cn.master.backend.service.SysProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author create by 11's papa on 2023/1/11-10:05
 */
@Slf4j
public class NodeTreeUtils<T extends TreeNodeDTO<T>> {
    protected static final double LIMIT_POS = 64;
    public static final double DEFAULT_POS = 65536;
    protected Class<T> clazz;

    public NodeTreeUtils(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getClassInstance() {
        T instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getCause().getMessage(), e);
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    public List<T> getNodeTrees(List<T> nodes) {
        return getNodeTrees(nodes, null);
    }

    public Map<String, Integer> getCountMap(List<T> nodes) {
        return nodes.stream()
                .collect(Collectors.toMap(TreeNodeDTO::getId, TreeNodeDTO::getCaseNum));
    }

    public List<T> getNodeTrees(List<T> nodes, Map<String, Integer> countMap) {
        List<T> nodeTreeList = new ArrayList<>();
        Map<Integer, List<T>> nodeLevelMap = new HashMap<>();
        nodes.forEach(node -> {
            Integer level = node.getLevel();
            if (nodeLevelMap.containsKey(level)) {
                nodeLevelMap.get(level).add(node);
            } else {
                List<T> testCaseNodes = new ArrayList<>();
                testCaseNodes.add(node);
                nodeLevelMap.put(node.getLevel(), testCaseNodes);
            }
        });
        List<T> rootNodes = Optional.ofNullable(nodeLevelMap.get(1)).orElse(new ArrayList<>());
        rootNodes.forEach(rootNode -> nodeTreeList.add(buildNodeTree(nodeLevelMap, rootNode, countMap)));
        return nodeTreeList;
    }

    public T buildNodeTree(Map<Integer, List<T>> nodeLevelMap, T rootNode, Map<String, Integer> countMap) {

        T nodeTree = getClassInstance();
        BeanUtils.copyProperties(rootNode, nodeTree);
        nodeTree.setLabel(rootNode.getName());
        setCaseNum(countMap, nodeTree);

        List<T> lowerNodes = nodeLevelMap.get(rootNode.getLevel() + 1);
        if (lowerNodes == null) {
            return nodeTree;
        }

        List<T> children = new ArrayList<>();

        lowerNodes.forEach(node -> {
            if (node.getParentId() != null && node.getParentId().equals(rootNode.getId())) {
                children.add(buildNodeTree(nodeLevelMap, node, countMap));
                nodeTree.setChildren(children);
            }
        });
        if (countMap != null && CollectionUtils.isNotEmpty(children)) {
            Integer childrenCount = children.stream().map(TreeNodeDTO::getCaseNum).reduce(Integer::sum).get();
            nodeTree.setCaseNum(nodeTree.getCaseNum() + childrenCount);
        }
        return nodeTree;
    }

    public T buildNodeTree(Map<Integer, List<T>> nodeLevelMap, T rootNode) {
        return buildNodeTree(nodeLevelMap, rootNode, null);
    }

    private void setCaseNum(Map<String, Integer> countMap, T nodeTree) {
        if (countMap != null) {
            if (countMap.get(nodeTree.getId()) != null) {
                nodeTree.setCaseNum(countMap.get(nodeTree.getId()));
            } else {
                nodeTree.setCaseNum(0);
            }
        }
    }

    public List<T> getNodeTreeWithPruningTree(List<T> countModules,
                                              Function<List<String>, List<T>> getProjectModulesFunc) {
        if (org.springframework.util.CollectionUtils.isEmpty(countModules)) {
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>();

        Set<String> projectIdSet = new HashSet<>();
        countModules.forEach(x -> projectIdSet.add(x.getProjectId()));
        List<String> projectIds = new ArrayList<>(projectIdSet);




        SysProjectService projectService = SpringContextUtils.getBean(SysProjectService.class);
        List<SysProject> projects = projectService.getProjectByIds(new ArrayList<>(projectIds));

        // 项目->对应项目下的模块
        Map<String, List<T>> projectModuleMap = getProjectModulesFunc.apply(projectIds)
                .stream()
                .collect(Collectors.groupingBy(TreeNodeDTO::getProjectId));

        // 模块->用例数
        Map<String, Integer> countMap = countModules.stream()
                .collect(Collectors.toMap(TreeNodeDTO::getId, TreeNodeDTO::getCaseNum));

        projects.forEach((project) -> {
            if (project != null) {
                List<T> testCaseNodes = projectModuleMap.get(project.getId());

                testCaseNodes = testCaseNodes.stream().sorted(Comparator.comparingDouble(TreeNodeDTO::getPos))
                        .collect(Collectors.toList());

                testCaseNodes = getNodeTreeWithPruningTreeByCaseCount(testCaseNodes, countMap);

                // 项目设置成根节点
                T projectNode = getClassInstance();
                projectNode.setId(project.getId());
                projectNode.setName(project.getName());
                projectNode.setLabel(project.getName());
                projectNode.setChildren(testCaseNodes);
                projectNode.setCaseNum(testCaseNodes.stream().mapToInt(TreeNodeDTO::getCaseNum).sum());
                if (countMap.get(null) != null) {
                    // 如果模块删除了, 回收站中的用例归到项目模块下
                    projectNode.setCaseNum(projectNode.getCaseNum() + countMap.get(null));
                }
                if (!org.springframework.util.CollectionUtils.isEmpty(testCaseNodes)) {
                    list.add(projectNode);
                }
            }
        });
        return list;
    }

    /**
     * 生成模块树并剪枝
     *
     * @return
     */
    public List<T> getNodeTreeWithPruningTreeByCaseCount(List<T> testCaseNodes, Map<String, Integer> countMap) {
        List<T> nodeTrees = getNodeTrees(testCaseNodes, countMap);
        nodeTrees.removeIf(this::pruningTreeByCaseCount);
        return nodeTrees;
    }

    /**
     * 去除没有数据的节点
     *
     * @param rootNode
     * @param nodeIds
     * @return 是否剪枝
     */
    public boolean pruningTree(T rootNode, List<String> nodeIds) {

        List<T> children = rootNode.getChildren();

        if (children == null || children.isEmpty()) {
            //叶子节点,并且该节点无数据
            if (!nodeIds.contains(rootNode.getId())) {
                return true;
            }
        }
        if (children != null) {
            children.removeIf(subNode -> pruningTree(subNode, nodeIds));
            return children.isEmpty() && !nodeIds.contains(rootNode.getId());
        }
        return false;
    }

    public boolean pruningTreeByCaseCount(T rootNode) {
        List<T> children = rootNode.getChildren();
        if (rootNode.getCaseNum() == null || rootNode.getCaseNum() < 1) {
            // 没有用例的模块剪掉
            return true;
        }
        if (children != null) {
            children.removeIf(this::pruningTreeByCaseCount);
        }
        return false;
    }

    /**
     * 根据目标节点路径，创建相关节点
     *
     * @param pathIterator 遍历子路径
     * @param path         当前路径
     * @param treeNode     当前节点
     * @param pathMap      记录节点路径对应的nodeId
     */
    protected void createNodeByPathIterator(Iterator<String> pathIterator, String path, T treeNode,
                                            Map<String, String> pathMap, String projectId, Integer level) {
        List<T> children = treeNode.getChildren();
        if (children == null || children.isEmpty() || !pathIterator.hasNext()) {
            pathMap.put(path, treeNode.getId());
            if (pathIterator.hasNext()) {
                createNodeByPath(pathIterator, pathIterator.next().trim(), treeNode, projectId, level, path, pathMap);
            }
            return;
        }
        String nodeName = pathIterator.next().trim();
        boolean hasNode = false;

        for (T child : children) {
            if (StringUtils.equals(nodeName, child.getName())) {
                hasNode = true;
                createNodeByPathIterator(pathIterator, path + "/" + child.getName(),
                        child, pathMap, projectId, level + 1);
            }
        }
        //若子节点中不包含该目标节点，则在该节点下创建
        if (!hasNode) {
            createNodeByPath(pathIterator, nodeName, treeNode, projectId, level, path, pathMap);
        }

    }

    /**
     * @param pathIterator 迭代器，遍历子节点
     * @param nodeName     当前节点
     * @param pNode        父节点
     */
    protected void createNodeByPath(Iterator<String> pathIterator, String nodeName,
                                    T pNode, String projectId, Integer level,
                                    String rootPath, Map<String, String> pathMap) {

        StringBuilder path = new StringBuilder(rootPath);
        path.append("/").append(nodeName.trim());
        String pid;
        //创建过不创建
        if (pathMap.get(path.toString()) != null) {
            pid = pathMap.get(path.toString());
            level++;
        } else {
            pid = insertNode(nodeName, pNode == null ? null : pNode.getId(), projectId, level, path.toString());
            pathMap.put(path.toString(), pid);
            level++;
        }

        while (pathIterator.hasNext()) {
            String nextNodeName = pathIterator.next().trim();
            path.append("/").append(nextNodeName);
            if (pathMap.get(path.toString()) != null) {
                pid = pathMap.get(path.toString());
                level++;
            } else {
                pid = insertNode(nextNodeName, pid, projectId, level, path.toString());
                pathMap.put(path.toString(), pid);
                level++;
            }
        }
    }

    public String insertNode(String nodeName, String pId, String projectId, Integer level, String path) {
        return StringUtils.EMPTY;
    }
}
