package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author create by 11's papa on 2022/12/31-21:11
 */
@Getter
@Setter
public class QueryMemberRequest {
    private String name;
    private String workspaceId;
    private String projectId;
}
