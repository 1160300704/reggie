package com.zhouhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouhao.common.R;
import com.zhouhao.entity.Orders;

public interface OrderService extends IService<Orders> {
    R submit(Orders orders);
}
