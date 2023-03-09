package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author create by 11's papa on 2023/2/1-11:12
 */
@Setter
@Getter
public class QueryCaseReviewRequest extends BaseQueryRequest {
    private List<String> nodePaths;

    private List<String> reviewIds;

    private List<String> projectIds;

    private String node;

    private String nodeId;

    private String projectName;

    private String reviewerId;

    private String versionId;

    private String id;

    private String reviewId;

    private String caseId;

    private String result;

    private String reviewer;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private Long order;

    private String status;
    private String priority;

    private Boolean isDel;
}
