package com.zhouhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouhao.common.R;
import com.zhouhao.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart>{
    R add(ShoppingCart shoppingCart);

    R clean(Long userId);

    R sub(ShoppingCart shoppingCart);
}
