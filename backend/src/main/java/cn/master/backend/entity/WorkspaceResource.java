package cn.master.backend.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author create by 11's papa on 2023/1/3-13:40
 */
@Data
public class WorkspaceResource {
    private List<SysWorkspace> workspaces = new ArrayList<>();
    private List<SysProject> projects = new ArrayList<>();
}
