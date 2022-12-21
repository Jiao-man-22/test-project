package com.jiao.testproject.testproject.services;

import com.jiao.testproject.testproject.dto.FolderDto;
import com.jiao.testproject.testproject.dto.UserDto;
import com.jiao.testproject.testproject.dto.pojo.UserRole;
import com.jiao.testproject.testproject.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/*
 * 用户表的crud
 * */
public interface IUserService {

    Integer insertUser(UserDto userDto);

    UserEntity selectUserById(UserDto userDto);

    Integer deleteUserById(UserDto userDto);

    Integer logOutUser(UserDto userDto);

    Integer updatePasswordById(UserDto userDto);

    List<UserEntity> selectUserAll();

    int delUserAndFileByUid(Integer userid);

    List<String> selectFunctions(Integer role);

    //jpa 重写
    UserEntity selectUserById(UserDto userDto, int overloadFlag);

    Integer deleteUserById(UserDto userDto, int overloadFlag);

    Map<String, UserEntity> getUserInfoMap();

    UserRole getUserRoleInfo();

    /****
    * @Description:
    * @Param: [username]
    * @return: com.jiao.testproject.testproject.entity.UserEntity
    * @Author: JRJ
    * @Date: 2022/12/19
    */

    UserEntity getByUsername(String username);


}
