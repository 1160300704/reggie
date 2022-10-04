package com.zhouhao.controller;

import com.zhouhao.common.R;
import com.zhouhao.dto.SetmealDto;
import com.zhouhao.entity.Setmeal;
import com.zhouhao.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @CachePut(value = "setmeal", key = "#setmealDto.categoryId")
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto){
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
    public R list(Setmeal setmeal){
        return setmealService.list(setmeal);
    }
}
