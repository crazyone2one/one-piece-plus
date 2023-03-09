package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2023-02-01
 */
@Getter
@Setter
@TableName("test_case_review")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("name")
    private String name;

    @TableField("creator")
    private String creator;

    @TableField("status")
    private String status;

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

    @TableField("end_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime endTime;

    @TableField("description")
    private String description;

    /**
     * 用例评审所属项目
     */
    @TableField("project_id")
    private String projectId;

    @TableField("tags")
    private String tags;

    @TableField("create_user")
    private String createUser;

    @TableField(exist = false)
    private String projectName;
    @TableField(exist = false)
    private String reviewerName;
    @TableField(exist = false)
    private String creatorName;

    @TableField(exist = false)
    private List<String> projectIds;
    @TableField(exist = false)
    private List<String> userIds;
    @TableField(exist = false)
    private List<String> followIds;
    @TableField(exist = false)
    private String reviewer;
}
