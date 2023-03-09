package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-11
 */
@Data
@TableName("test_case_node")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Test case node ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Project ID this node belongs to
     */
    @TableField("project_id")
    private String projectId;

    /**
     * Node name
     */
    @TableField("name")
    private String name;

    /**
     * Parent node ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * Node level
     */
    @TableField("level")
    private Integer level;

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

    @TableField("pos")
    private Object pos;

    @TableField("create_user")
    private String createUser;

    @TableField(exist = false)
    private Integer caseNum;
}
