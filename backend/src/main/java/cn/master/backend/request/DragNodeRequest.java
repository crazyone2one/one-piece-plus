package cn.master.backend.request;

import cn.master.backend.entity.ModuleNode;
import cn.master.backend.entity.TestCaseNodeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author create by 11's papa on 2023/1/11-11:17
 */
@Setter
@Getter
public class DragNodeRequest extends ModuleNode {
    List<String> nodeIds;
    TestCaseNodeDTO nodeTree;
}
