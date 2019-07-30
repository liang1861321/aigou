package cn.itsource.plat.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.plat.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/login")
    public AjaxResult login(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        //假登录
        if("admin".equals(username)&&"admin".equals(password)){
            return AjaxResult.me().setSuccess(true).setMessage("登录成功!").setRestObj(user);
        }
        return AjaxResult.me().setSuccess(false).setMessage("登录失败!");
    }
}
