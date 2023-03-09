package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-17
 */
@Data
@TableName("test_case")
@AllArgsConstructor
@NoArgsConstructor
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Test case ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Node ID this case belongs to
     */
    @TableField("node_id")
    private String nodeId;

    @TableField("test_id")
    private String testId;

    /**
     * Node path this case belongs to
     */
    @TableField("node_path")
    private String nodePath;

    /**
     * Project ID this test belongs to
     */
    @TableField("project_id")
    private String projectId;

    @TableField(exist = false)
    private String projectName;

    /**
     * Test case name
     */
    @TableField("`name`")
    private String name;

    /**
     * Test case type
     */
    @TableField("type")
    private String type;

    /**
     * Test case maintainer
     */
    @TableField("maintainer")
    private String maintainer;
    @TableField(exist = false)
    private String maintainerName;

    /**
     * Test case priority
     */
    @TableField("priority")
    private String priority;

    /**
     * Test case method type
     */
    @TableField("method")
    private String method;

    /**
     * Test case prerequisite condition
     */
    @TableField("prerequisite")
    private String prerequisite;

    /**
     * Test case remark
     */
    @TableField("remark")
    private String remark;

    /**
     * Test case steps (JSON format)
     */
    @TableField("steps")
    private String steps;

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
     * Manually controlled growth identifier
     */
    @TableField("num")
    private Integer num;

    @TableField("other_test_name")
    private String otherTestName;

    @TableField("review_status")
    private String reviewStatus;

    @TableField("tags")
    private String tags;

    @TableField("demand_id")
    private String demandId;

    @TableField("demand_name")
    private String demandName;

    @TableField("follow_people")
    private String followPeople;

    @TableField("`status`")
    private String status;

    @TableField("create_user")
    private String createUser;

    @TableField("step_description")
    private String stepDescription;

    @TableField("expected_result")
    private String expectedResult;

    /**
     * CustomField
     */
    @TableField("custom_fields")
    private String customFields;

    @TableField("original_status")
    private String originalStatus;

    /**
     * Delete user id
     */
    @TableField("delete_user_id")
    private String deleteUserId;

    /**
     * Delete timestamp
     */
    @TableField(value = "delete_time")
    private LocalDateTime deleteTime;

    /**
     * 自定义排序，间隔5000
     */
    @TableField("`order`")
    private Long order;
    @TableField("custom_num")
    private String customNum;

    @TableField("ref_id")
    private String refId;

    @TableField("latest")
    private boolean latest;

    @TableField("last_execute_result")
    private String lastExecuteResult;
}
