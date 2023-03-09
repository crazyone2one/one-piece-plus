package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author 11's papa
 * @since 2023-02-02
 */
@Getter
@Setter
@TableName("test_case_review_test_case")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseReviewTestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("review_id")
    private String reviewId;


    @TableField("case_id")
    private String caseId;

    @TableField("`status`")
    private String status;

    @TableField("result")
    private String result;

    @TableField("reviewer")
    private String reviewer;
    @TableField(exist = false)
    private String reviewerName;
    /**
     * Create timestamp
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * Update timestamp
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_user")
    private String createUser;

    /**
     * 自定义排序，间隔5000
     */
    @TableField("`order`")
    private Long order;

    /**
     * 关联的用例是否放入回收站
     */
    @TableField("is_del")
    private Boolean isDel;

    @TableField(exist = false)
    private TestCase testCase;
}
