package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

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
@TableName("test_case_review_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseReviewUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("review_id")
    private String reviewId;

    @TableField("user_id")
    private String userId;
}
