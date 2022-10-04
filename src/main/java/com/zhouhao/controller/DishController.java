package com.zhouhao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhouhao.common.R;
import com.zhouhao.dto.DishDto;
import com.zhouhao.entity.Category;
import com.zhouhao.entity.Dish;
import com.zhouhao.entity.DishFlavor;
import com.zhouhao.service.CategoryService;
import com.zhouhao.service.DishFlavorService;
import com.zhouhao.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("page")
    public R list(int page, int pageSize, String name){
        IPage<Dish> pages = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        queryWrapper.like(name != null, Dish::getName, name);
        dishService.page(pages, queryWrapper);

        IPage<DishDto> dtoPages = new Page<>(page, pageSize);
        BeanUtils.copyProperties(pages, dtoPages, "records");
        List<DishDto> dishDtos = new ArrayList<>();
        List<Dish> dishes = pages.getRecords();
        for(Dish dish: dishes){
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            LambdaQueryWrapper<Category> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(Category::getId, dishDto.getCategoryId());
            Category category = categoryService.getOne(queryWrapper2);
            dishDto.setCategoryName(category.getName());
            dishDtos.add(dishDto);
        }
        dtoPages.setRecords(dishDtos);

        return R.success(dtoPages);
    }

    @PostMapping
    public R save(@RequestBody DishDto dishDto){
        dishService.save(dishDto);

        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor flavor: flavors){
            flavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);

        redisTemplate.delete(dishDto.getCategoryId().toString());
        return R.success("成功");
    }

    @GetMapping("/{id}")
    public R getDish(@PathVariable("id") Long id){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId, id);
        Dish dish = dishService.getOne(queryWrapper);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(DishFlavor::getDishId, id);
        dishDto.setFlavors(dishFlavorService.list(queryWrapper2));

        return R.success(dishDto);
    }

    @PutMapping
    public R update(@RequestBody DishDto dishDto){
        dishService.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor flavor: flavors){
            flavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);

        redisTemplate.delete(dishDto.getCategoryId().toString());
        return R.success("睡觉");
    }

    @GetMapping("list")
    public R DishList(Long categoryId){
        Object o = redisTemplate.opsForValue().get(categoryId.toString());
        if(o != null){
            return R.success(o);
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dtoList = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper2);
            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(categoryId.toString(), dtoList);

        return R.success(dtoList);
    }
}
