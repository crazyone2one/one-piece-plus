package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-24
 */
@Getter
@Setter
@TableName("test_plan")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Test Plan ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Workspace ID this plan belongs to
     */
    @TableField("workspace_id")
    private String workspaceId;

    /**
     * Test plan report
     */
    @TableField("report_id")
    private String reportId;

    /**
     * Plan name
     */
    @TableField("name")
    private String name;

    /**
     * Plan description
     */
    @TableField("description")
    private String description;

    /**
     * Plan status
     */
    @TableField("status")
    private String status;

    /**
     * Plan stage
     */
    @TableField("stage")
    private String stage;

    /**
     * Plan principal
     */
    @TableField("principal")
    private String principal;

    /**
     * Test case match rule
     */
    @TableField("test_case_match_rule")
    private String testCaseMatchRule;

    /**
     * Executor match rule)
     */
    @TableField("executor_match_rule")
    private String executorMatchRule;

    /**
     * Test plan tags (JSON format)
     */
    @TableField("tags")
    private String tags;

    /**
     * Create timestamp
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update timestamp
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * Planned start time timestamp
     */
    @TableField("planned_start_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime plannedStartTime;

    /**
     * Planned end time timestamp
     */
    @TableField("planned_end_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime plannedEndTime;

    /**
     * Actual start time timestamp
     */
    @TableField("actual_start_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime actualStartTime;

    /**
     * Actual end time timestamp
     */
    @TableField("actual_end_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime actualEndTime;

    @TableField("creator")
    private String creator;

    /**
     * 测试计划所属项目
     */
    @TableField("project_id")
    private String projectId;

    @TableField("execution_times")
    private Integer executionTimes;

    /**
     * 是否自定更新功能用例状态
     */
    @TableField("automatic_status_update")
    private Boolean automaticStatusUpdate;

    /**
     * 是否允许重复添加用例
     */
    @TableField("repeat_case")
    private Boolean repeatCase;

    /**
     * 测试计划报告总结
     */
    @TableField("report_summary")
    private String reportSummary;

    /**
     * 测试计划报告配置
     */
    @TableField("report_config")
    private String reportConfig;

    /**
     * request (JSON format)
     */
    @TableField("run_mode_config")
    private String runModeConfig;
}
