package com.zhouhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouhao.common.R;
import com.zhouhao.entity.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {
    R login(Map map, HttpSession session);

    void sendMsg(String phone, HttpSession session);
}
