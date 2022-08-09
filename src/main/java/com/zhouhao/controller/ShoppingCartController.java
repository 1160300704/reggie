package com.zhouhao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouhao.common.BaseContext;
import com.zhouhao.entity.ShoppingCart;
import com.zhouhao.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhouhao.common.R;

@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("list")
    public R list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.get());
        return R.success(shoppingCartService.list(queryWrapper));
    }

    @PostMapping("add")
    public R add(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.add(shoppingCart);
    }

    @DeleteMapping("clean")
    public R clean(){
        return shoppingCartService.clean(BaseContext.get());
    }

    @PostMapping("sub")
    public R sub(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.sub(shoppingCart);
    }
}
