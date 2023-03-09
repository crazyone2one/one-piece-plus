package cn.master.backend.entity;

import lombok.Data;


/**
 * @author create by 11's papa on 2023/1/1-16:57
 */
@Data
public class SysGroupDTO extends SysGroup{
    private String scopeName;
    private Long memberSize;
}
