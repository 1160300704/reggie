package com.zhouhao.controller;

import com.zhouhao.entity.Orders;
import com.zhouhao.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhouhao.common.R;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("submit")
    public R submit(@RequestBody Orders orders){
        return orderService.submit(orders);
    }
}
