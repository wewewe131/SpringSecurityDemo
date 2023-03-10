package com.example.firstdemo.Security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper;
import com.example.firstdemo.Entity.ChatUser;
import com.example.firstdemo.Entity.Upassword;
import com.example.firstdemo.Service.ChatUserService;
import com.example.firstdemo.Service.UpasswordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MyUserDetailService implements UserDetailsService {

      private final ChatUserService chatUserService;

      private final UpasswordService upasswordService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ChatUser uname = chatUserService.getOne(
                new QueryWrapper<ChatUser>().eq("uname", username)
        );

        if (Objects.isNull(uname) ||uname.getUid().isBlank())
            throw new UsernameNotFoundException("用户不存在");

        UserDetail userDetail = new UserDetail();
        BeanUtils.copyProperties(uname,userDetail);
        Upassword uid = upasswordService.getOne(new QueryWrapper<Upassword>().eq("uid", uname.getUid()));
        userDetail.setPassword(uid.getUpassword());
        ArrayList<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        userDetail.setRoles(roles);
        return userDetail;
    }
}
