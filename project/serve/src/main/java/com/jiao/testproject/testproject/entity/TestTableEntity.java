package com.jiao.testproject.testproject.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "test_table")
//复合主键要建立主键类
//@IdClass(com.jiao.testproject.testproject.entity.CombinationKeyByNameAndId.class)
public class TestTableEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    @Column(name = "id")
    private  int id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name="time")
    private Date time_1;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name="time_2" ,columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date time_2;

    @Column(precision=8, scale=2 ,name="salary")
    private BigDecimal salary;
}
