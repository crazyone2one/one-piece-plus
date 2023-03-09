package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author create by 11's papa on 2023/1/8-12:50
 */
@Setter
@Getter
public class BaseQueryRequest {
    private String projectId;

    private String name;

    /**
     * 状态不等于 notEqStatus
     */
    private String notEqStatus;

    private String workspaceId;

    private List<String> ids;

    private List<String> moduleIds;

    private List<String> nodeIds;

    /**
     * 排除哪些id
     */
    private List<String> notInIds;

    /**
     * 是否选中所有数据
     */
    private boolean selectAll;

    /**
     * 全选之后取消选中的id
     */
    private List<String> unSelectIds;

    /**
     * 排序条件
     */
    private List<OrderRequest> orders;

    /**
     * 过滤条件
     */
    private Map<String, List<String>> filters;

    /**
     * 高级搜索
     */
    private Map<String, Object> combine;

    /**
     * 要查询的字段
     */
    private List<String> selectFields;

    /**
     * 版本 ID
     */
    private String versionId;

    /**
     * 版本来源字段
     */
    private String refId;

    /**
     * 测试计划关联场景过滤掉步骤为0的场景
     */
    private boolean hasStep;

    private String scenarioType;
}
