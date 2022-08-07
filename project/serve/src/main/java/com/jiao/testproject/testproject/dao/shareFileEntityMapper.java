package com.jiao.testproject.testproject.dao;

import com.jiao.testproject.testproject.entity.shareFileEntity;
import com.jiao.testproject.testproject.entity.shareFileEntityExample;
import com.jiao.testproject.testproject.entity.shareFileEntityKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface shareFileEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int countByExample(shareFileEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int deleteByExample(shareFileEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(shareFileEntityKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int insert(shareFileEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int insertSelective(shareFileEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    List<shareFileEntity> selectByExample(shareFileEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    shareFileEntity selectByPrimaryKey(shareFileEntityKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") shareFileEntity record, @Param("example") shareFileEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") shareFileEntity record, @Param("example") shareFileEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(shareFileEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sharefile_table
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(shareFileEntity record);
}