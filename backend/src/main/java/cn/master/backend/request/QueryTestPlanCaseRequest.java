package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author create by 11's papa on 2023/1/31-11:07
 */
@Setter
@Getter
public class QueryTestPlanCaseRequest extends BaseQueryRequest {
    private List<String> nodePaths;

    private List<String> planIds;

    private List<String> projectIds;

    private String workspaceId;

    private String status;

    private String node;

    private String method;

    private String nodeId;

    private String planId;

    private String executor;

    private String id;

    private String projectName;

    private Map<String, Object> combine;
}
