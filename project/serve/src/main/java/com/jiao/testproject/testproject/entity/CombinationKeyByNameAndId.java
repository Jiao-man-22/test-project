package com.jiao.testproject.testproject.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class CombinationKeyByNameAndId implements Serializable {

    /**
     * 新建组合主键类
     */

    private Integer id;

    private String name;

}
