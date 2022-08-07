package com.jiao.testproject.testproject.dto;

import java.util.List;

/**
 * @ClassName TreeData
 * @Description 返回给前端做菜单显示
 * @AUTHOR jiaorongjin
 * @Date 2022/8/5 10:58
 * @Version 1.0
 **/
public class TreeData {
    // 唯一标识
    private String id;
    //做父子关系
    private String parentId;
    //展示内容
    private String label;
    //子节点集合
    private List<Object> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }
}
