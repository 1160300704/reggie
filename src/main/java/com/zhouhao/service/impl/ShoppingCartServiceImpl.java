package com.zhouhao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouhao.common.BaseContext;
import com.zhouhao.common.R;
import com.zhouhao.entity.ShoppingCart;
import com.zhouhao.mapper.ShoppingCartMapper;
import com.zhouhao.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public R add(ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        Long userId = BaseContext.get();

        if(dishId != null){
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getDishId, dishId).eq(ShoppingCart::getUserId, userId);
            ShoppingCart one = this.getOne(queryWrapper);

            if(one == null){
                shoppingCart.setUserId(userId);
                this.save(shoppingCart);
            }
            else {
                one.setNumber(one.getNumber() + 1);
                this.updateById(one);
            }
        }

        if(setmealId != null){
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId).eq(ShoppingCart::getUserId, userId);
            ShoppingCart one = this.getOne(queryWrapper);

            if(one == null){
                shoppingCart.setUserId(userId);
                this.save(shoppingCart);
            }
            else {
                one.setNumber(one.getNumber() + 1);
                this.updateById(one);
            }
        }

        return R.success("插入");
    }

    @Override
    public R clean(Long userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        this.remove(queryWrapper);
        return R.success("猛抽");
    }

    @Override
    public R sub(ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        Long userId = BaseContext.get();

        if(dishId != null){
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getDishId, dishId).eq(ShoppingCart::getUserId, userId);
            ShoppingCart one = this.getOne(queryWrapper);

            if(one.getNumber() == 1){
                this.remove(queryWrapper);
            }
            else {
                one.setNumber(one.getNumber() - 1);
                this.updateById(one);
            }
        }

        if(setmealId != null){
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId).eq(ShoppingCart::getUserId, userId);
            ShoppingCart one = this.getOne(queryWrapper);

            if(one.getNumber() == 1){
                this.remove(queryWrapper);
            }
            else {
                one.setNumber(one.getNumber() - 1);
                this.updateById(one);
            }
        }

        return R.success("抽出");
    }
}
