package cn.master.backend.service;

/**
 * @author create by 11's papa on 2023/1/4-12:26
 */
public interface BaseCheckPermissionService {
    /**
     * desc: 严重项目是否属于workspace
     *
     * @param projectId   projectId
     * @param workspaceId workspaceId
     */
    void checkProjectBelongToWorkspace(String projectId, String workspaceId);
}
