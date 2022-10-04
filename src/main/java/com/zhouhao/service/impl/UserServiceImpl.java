package com.zhouhao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouhao.common.R;
import com.zhouhao.entity.User;
import com.zhouhao.mapper.UserMapper;
import com.zhouhao.service.UserService;
import com.zhouhao.utils.SMSUtils;
import com.zhouhao.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public R login(Map map, HttpSession session) {
        String phone = (String)map.get("phone");
        Integer code = Integer.parseInt((String)map.get("code"));
//        Object validate = session.getAttribute(phone);
        Object validate = redisTemplate.opsForValue().get(phone);

        if(code.equals(validate)){
            redisTemplate.delete(phone);
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userMapper.selectOne(queryWrapper);
            session.setAttribute("user", user);
            //注册
            if(user == null){
                user = new User();
                user.setStatus(1);
                user.setPhone(phone);
                userMapper.insert(user);
                session.setAttribute("user", user);
            }
            return R.success("ok");
        }
        return R.error("没登上");
    }

    @Override
    public void sendMsg(String phone, HttpSession session) {
        int code = ValidateCodeUtils.generateValidateCode(4);
        log.info(String.valueOf(code));
        // 发送至手机
//        SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", "18846178317", String.valueOf(code));
        redisTemplate.opsForValue().set(phone, code, 10, TimeUnit.SECONDS);
//        session.setAttribute(phone, code);
    }
}
