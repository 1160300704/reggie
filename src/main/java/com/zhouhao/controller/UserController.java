package com.zhouhao.controller;

import com.zhouhao.common.R;
import com.zhouhao.entity.User;
import com.zhouhao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("login")
    public R login(@RequestBody Map map, HttpSession session){
        return userService.login(map, session);
    }

    @PostMapping("sendMsg")
    public void sendMsg(@RequestBody User user, HttpSession session){
        userService.sendMsg(user.getPhone(), session);
    }
}
