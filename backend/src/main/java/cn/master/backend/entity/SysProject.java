package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-04
 */
@Data
@TableName("sys_project")
@AllArgsConstructor
@NoArgsConstructor
public class SysProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Project ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Workspace ID this project belongs to
     */
    @TableField("workspace_id")
    private String workspaceId;
    @TableField(exist = false)
    private String workspaceName;

    /**
     * Project name
     */
    @TableField("name")
    private String name;

    /**
     * Project description
     */
    @TableField("description")
    private String description;

    /**
     * Create timestamp
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime createTime;

    /**
     * Update timestamp
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime updateTime;

    /**
     * 项目使用哪个平台的模板
     */
    @TableField("platform")
    private String platform;

    /**
     * 是否使用第三方平台缺陷模板
     */
    @TableField("third_part_template")
    private Boolean thirdPartTemplate;

    @TableField("version_enable")
    private Boolean versionEnable;

    @TableField("issue_config")
    private String issueConfig;

    @TableField("api_template_id")
    private String apiTemplateId;

    @TableField("repeatable")
    private Boolean repeatable;

    /**
     * Relate test case template id
     */
    @TableField("case_template_id")
    private String caseTemplateId;

    /**
     * Relate test issue template id
     */
    @TableField("issue_template_id")
    private String issueTemplateId;

    /**
     * 是否开启自定义ID(默认关闭)
     */
    @TableField("custom_num")
    private Boolean customNum;

    @TableField("create_user")
    private String createUser;
    @TableField(exist = false)
    private String createUserName;

    @TableField("system_id")
    private String systemId;

    @TableField("tapd_id")
    private String tapdId;

    @TableField("jira_key")
    private String jiraKey;

    @TableField("zentao_id")
    private String zentaoId;

    @TableField(exist = false)
    private long memberSize;

    @TableField(exist = false)
    private Boolean customPermissionFlag;
}
