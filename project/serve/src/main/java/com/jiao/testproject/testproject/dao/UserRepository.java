package com.jiao.testproject.testproject.dao;

import com.jiao.testproject.testproject.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//继承JpaRepository来完成对数据库的操作
@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer>  {


}
