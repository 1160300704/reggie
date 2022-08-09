package com.zhouhao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouhao.common.R;
import com.zhouhao.entity.Orders;
import com.zhouhao.mapper.OrderMapper;
import com.zhouhao.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Override
    public R submit(Orders orders) {


        return null;
    }
}
