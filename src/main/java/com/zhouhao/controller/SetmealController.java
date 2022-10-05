package com.zhouhao.controller;

import com.zhouhao.common.R;
import com.zhouhao.dto.SetmealDto;
import com.zhouhao.entity.Setmeal;
import com.zhouhao.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @CachePut(value = "setmeal", key = "#setmealDto.categoryId")
    @PostMapping
    @ApiOperation(value = "新增套餐接口")
    public Object save(@RequestBody SetmealDto setmealDto){
        return setmealService.saveWithDish(setmealDto);
    }

    @GetMapping("page")
    public R list(int page, int pageSize, String name){
        return setmealService.list(page, pageSize, name);
    }

    @CacheEvict(value = "setmeal", allEntries = true)
    @DeleteMapping
    public R remove(Long[] ids){
        return setmealService.remove(ids);
    }

    @GetMapping("list")
    @Cacheable(value = "setmeal", key = "#setmeal.categoryId")
    public Object list(Setmeal setmeal){
        return setmealService.list(setmeal);
    }
}
