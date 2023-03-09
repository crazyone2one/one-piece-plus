package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author create by 11's papa on 2023/1/24-16:12
 */
@Setter
@Getter
public class BatchOperateRequest {
    private List<String> ids;
    boolean selectAll;
    private List<String> unSelectIds;
    private QueryTestPlanRequest queryTestPlanRequest;
    private String projectId;
}
