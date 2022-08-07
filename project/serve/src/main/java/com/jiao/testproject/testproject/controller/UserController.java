package com.jiao.testproject.testproject.controller;

import com.jiao.testproject.testproject.dto.AjaxResult;
import com.jiao.testproject.testproject.dto.UserDto;
import com.jiao.testproject.testproject.entity.UserEntity;
import com.jiao.testproject.testproject.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService UserService;

    private static final Integer integer_0 = 0;
    private static final Integer integer_1 = 1;


    /*查询所有用户*/
    @GetMapping("/getUserAll")
    public AjaxResult getAllUser(){
        List<UserEntity> userEntities = UserService.selectUserAll();
        if (userEntities!= null && userEntities.size()>0){
            return AjaxResult.success("200",userEntities);
        }
        return AjaxResult.error("userEntities == null");
    }
    /*删除 user*/
    @PostMapping("/delUser")
    public AjaxResult deleteUserById(@RequestParam(required = false) Integer userid){
        if (userid != null){
            UserDto userDto = new UserDto();
            userDto.setUuid(userid);
            //Integer integer = UserService.deleteUserById(userDto);
            int jpaFlag=0;
            Integer integer = UserService.deleteUserById(userDto, jpaFlag);
            if (integer>0){
                return AjaxResult.success("操作成功",integer);
            }
        }
        return AjaxResult.error("500");
    };
    @PostMapping("/loginOutUser")
    public AjaxResult loginOutUser(@RequestParam String username,@RequestParam Integer userid){
        if (userid != null){
            UserDto userDto = new UserDto();
            userDto.setUserName(username);
            userDto.setUuid(userid);
            UserEntity userEntity = UserService.selectUserById(userDto);

            userDto.setStatus(userEntity.getStatus());
            if (userEntity != null && !(userEntity.getStatus().equals(integer_1))){
                Integer integer = UserService.logOutUser(userDto);
                if (integer>0){
                    return AjaxResult.success("注销登录","1");
                }
            }
        }
        return AjaxResult.error("1");
    }


    /*修改密码*/
    @PostMapping("/updateUserPassword")
    public AjaxResult updateUserPassword(@RequestParam String password,@RequestParam String username ,@RequestParam Integer userid){
        if (password != null && username != null && userid != null){
            UserDto userDto = new UserDto();
            userDto.setPassword(password);
            userDto.setUuid(userid);
            Integer integer = UserService.updatePasswordById(userDto);
            if (integer>0){
                return AjaxResult.success("200");
            }
        }
        return AjaxResult.error();
    }
    /*删除 User file*/
    @PostMapping("/delUserAndFileById")
    public AjaxResult delUserAndFileById(String userid){

        if (userid ==null || userid.equals("''")){
            return AjaxResult.error("userid 不能为空");
        }
        int i = UserService.delUserAndFileByUid(Integer.valueOf(userid));
        if (i==0){
            return AjaxResult.error("NG",i);
        }
        return AjaxResult.success("Ok",i);
    }

    @GetMapping("/getUserPermission")
    public AjaxResult getUserPermission(String role){
        if (role ==null || role.equals("''")){
            return AjaxResult.error("userid 不能为空");
        }
        List<String> strings = UserService.selectFunctions(Integer.valueOf(role));
        return AjaxResult.success("成功",strings);
    }

}
