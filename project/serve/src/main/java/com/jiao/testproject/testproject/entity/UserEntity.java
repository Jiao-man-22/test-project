package com.jiao.testproject.testproject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@Entity //告诉JPA这是一个实体类（和数据表映射的类）
@Table(name = "user")
public class UserEntity {

    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    @Column(name = "user_id")

    private Integer user_id;

    @Column(name = "user_name")
    private String user_name;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "role")
    // 0 :是 普通 1：admin
    private Integer role;

    @Column(name = "status")
    //0:存在  1：逻辑删除
    private Integer status;

    @Column(name = "creater_id")
    private String creater_id;

    @Column(name = "create_time")
    private String create_time;

    @Column(name = "updater_id")
    private String updater_id;

    @Column(name = "update_time")
    private String update_time;



}
