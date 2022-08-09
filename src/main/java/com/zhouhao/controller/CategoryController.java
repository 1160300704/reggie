package com.zhouhao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhouhao.common.R;
import com.zhouhao.entity.Category;
import com.zhouhao.entity.Dish;
import com.zhouhao.entity.Setmeal;
import com.zhouhao.service.CategoryService;
import com.zhouhao.service.DishService;
import com.zhouhao.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("category")
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmaelService;


    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("我搞定了");
    }

    @GetMapping("page")
    public R<IPage<Category>> list(int page, int pageSize){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        IPage<Category> pages = new Page<>(page, pageSize);
        categoryService.page(pages, queryWrapper);

        return R.success(pages);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);

        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        if(list.size() != 0){
            return R.error("哈哈，你删不掉");
        }

        List<Setmeal> list2 = setmaelService.list(setmealLambdaQueryWrapper);
        if(list2.size() != 0){
            return R.error("老子有后台");
        }

        categoryService.removeById(ids);
        return R.success("删掉了");
    }

    @GetMapping("list")
    public R getList(Integer type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null, Category::getType, type);
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }
}
