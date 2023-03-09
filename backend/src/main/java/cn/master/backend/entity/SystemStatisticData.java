package cn.master.backend.entity;

import lombok.Data;

/**
 * @author create by 11's papa on 2023/2/21-13:58
 */
@Data
public class SystemStatisticData {
    private long userSize = 0;
    private long workspaceSize = 0;
    private long projectSize = 0;
}
