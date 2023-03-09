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
 * @since 2023-01-29
 */
@Getter
@Setter
@TableName("test_plan_test_case")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestPlanTestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Plan ID relation to
     */
    @TableField("plan_id")
    private String planId;

    /**
     * Case ID relation to
     */
    @TableField("case_id")
    private String caseId;

    /**
     * Test report ID relation to
     */
    @TableField("report_id")
    private String reportId;

    /**
     * Test case executor
     */
    @TableField("executor")
    private String executor;

    /**
     * Test case status
     */
    @TableField("status")
    private String status;

    /**
     * Test case result
     */
    @TableField("results")
    private String results;

    /**
     * Test case result issues
     */
    @TableField("issues")
    private String issues;

    /**
     * Test case remark
     */
    @TableField("remark")
    private String remark;

    /**
     * Create timestamp
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime createTime;

    /**
     * Update timestamp
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime updateTime;

    /**
     * 关联的用例是否放入回收站
     */
    @TableField("is_del")
    private Boolean isDel;

    @TableField("actual_result")
    private String actualResult;

    @TableField("create_user")
    private String createUser;

    @TableField("issues_count")
    private Integer issuesCount;

    /**
     * 自定义排序，间隔5000
     */
    @TableField("`order`")
    private Long order;
}
