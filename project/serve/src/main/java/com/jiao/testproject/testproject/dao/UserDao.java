package com.jiao.testproject.testproject.dao;

import com.jiao.testproject.testproject.dto.UserDto;
import com.jiao.testproject.testproject.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper

public interface UserDao {

    /*update password*/
    int updatePasswordById(UserEntity userEntity);

    /*注销登录*/
    int logOutUserById(UserEntity userEntity);

    /*delete account*/
    int deleteAccountById(@Param("user_id") Integer user_id);

    /*查询用户 */
    UserEntity selectUserById(@Param("user_id") Integer user_id);

    /*查询所有的用户*/
    List<UserEntity>selectUserAll();

    int deleteUserAndFileById(Integer userid);

    List<String> selectFunctions(@Param("role") Integer role);



}
