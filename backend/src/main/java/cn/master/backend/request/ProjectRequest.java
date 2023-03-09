package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author create by 11's papa on 2023/1/4-10:07
 */
@Setter
@Getter
public class ProjectRequest {
    private String workspaceId;
    private String projectId;
    private String userId;
    private String name;
    private Map<String, List<String>> filters;
    private Map<String, Object> combine;
    private List<OrderRequest> orders;
}
