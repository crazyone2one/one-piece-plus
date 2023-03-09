package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author create by 11's papa on 2023/2/28-14:49
 */
@Setter
@Getter

public class EditGroupUserRequest {
    private List<String> userIds;
    private List<String> sourceIds;
    private String groupId;
}
