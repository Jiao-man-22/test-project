package com.jiao.testproject.testproject.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "function_table")
public class PermissionEntity {
    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    @Column(name = "permission_id")
    private int permission_id;

    @Column(name = "role")
    private int role;

    @Column(name = "function")
    private String function;

    @Column(name = "pid")
    private int pid;

    @Column(name = "update_time")
    private String update_time;

}