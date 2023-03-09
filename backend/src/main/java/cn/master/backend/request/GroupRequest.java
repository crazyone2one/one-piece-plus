package cn.master.backend.request;

import lombok.Data;

/**
 * @author create by 11's papa on 2023/1/12-13:47
 */
@Data
public class GroupRequest {
    private String resourceId;
    private String projectId;
    private String type;
}
