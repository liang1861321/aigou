package cn.itsource.plat.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.plat.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/login")
    public AjaxResult login(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setId(18L);
        return AjaxResult.me().setSuccess(true).setMessage("登录成功").setRestObj(user);
    }
}
