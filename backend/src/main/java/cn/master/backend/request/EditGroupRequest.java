package cn.master.backend.request;

import cn.master.backend.entity.SysGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author create by 11's papa on 2023/1/1-16:47
 */
@Getter
@Setter
public class EditGroupRequest extends SysGroup {
    private List<String> types = new ArrayList<>();
    private List<String> scopes = new ArrayList<>();

    /**
     * 是否是全局用户组
     */
    private Boolean global;

    private String projectId;
    private String userGroupId;
    private boolean onlyQueryCurrentProject = false;
    private boolean onlyQueryGlobal = false;
}
