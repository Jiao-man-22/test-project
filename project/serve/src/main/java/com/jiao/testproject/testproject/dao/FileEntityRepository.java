package com.jiao.testproject.testproject.dao;


import com.jiao.testproject.testproject.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity,Integer>, QuerydslPredicateExecutor<FileEntity> {
}
