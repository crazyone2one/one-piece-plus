package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author create by 11's papa on 2023/1/12-14:00
 */
@Setter
@Getter
public class AddMemberRequest {
    private String workspaceId;
    private List<String> userIds;
    private List<String> roleIds;
    private List<String> groupIds;
    private String projectId;
}
