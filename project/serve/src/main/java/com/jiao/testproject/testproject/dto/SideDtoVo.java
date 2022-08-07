package com.jiao.testproject.testproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class SideDtoVo {

    private Integer id;

    private String label;

    private Integer pid;

    private String name;

    // 0 文件夹
    private Integer role;

    private Integer isShare;

    private Integer isDelete;

    List<SideDtoVo> children;
}
