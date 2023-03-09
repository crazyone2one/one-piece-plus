package cn.master.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author create by 11's papa on 2023/1/11-9:37
 */
@Data
public class TreeNodeDTO<T> {
    private String id;
    private String key;
    private String projectId;

    private String name;

    private String parentId;
    private String parentName;

    private Integer level;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Double pos;

    private String label;

    private List<T> children;

    private Integer caseNum;

    private static final long serialVersionUID = 1L;
}
