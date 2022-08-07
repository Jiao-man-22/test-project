package com.jiao.testproject.testproject.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class FileViewVo {
    //文件共有属性
    private Integer id;
    private Integer pid;
    private String name;
    private String parentName;
    private String upload;
    private String fileSize;
    private List<String> operatorList;
    private String path;
    private Integer userId;
    //文件 ： 1  文件夹：0
    private Integer role;
    private Integer share;
    private Integer isDelete;
    private boolean notAllowOperator;
    //0文件夹  1 定义文件 2 定义文件
    private Integer extensionFlag;
    //绝对路径
    private String absolutePath;

    //字节点集合
    private List<FileViewVo> childrenList;

}
