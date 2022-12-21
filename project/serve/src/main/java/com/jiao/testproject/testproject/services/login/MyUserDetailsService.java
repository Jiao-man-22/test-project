package com.jiao.testproject.testproject.services.login;

import com.alibaba.fastjson.JSON;
import com.jiao.testproject.testproject.dto.SysPermission;
import com.jiao.testproject.testproject.entity.UserEntity;
import com.jiao.testproject.testproject.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/***
* @Description:
* @Param:
* @return:
* @Author: JRJ
* @Date: 2022/12/19
*/

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    //自定义关于User的业务
    @Autowired
    private IUserService userService;

    @Resource
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         UserEntity sysUser = userService.getByUsername(username);
        if (null == sysUser) {
            log.warn("用户{}不存在", username);
            throw new UsernameNotFoundException(username);
        }
        // 查询用户的所有权限
        List<SysPermission> permissionList = permissionService.findByUserId(String.valueOf(sysUser.getUser_id()));
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            for (SysPermission sysPermission : permissionList) {
                authorityList.add(new SimpleGrantedAuthority(sysPermission.getRole_code()));
            }
        }

        MyUser myUser = new MyUser(sysUser.getUser_name(), passwordEncoder.encode(sysUser.getUser_password()), authorityList);

        log.info("登录成功！用户: {}", JSON.toJSONString(myUser));
        return myUser;
    }
}
