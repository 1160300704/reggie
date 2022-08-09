package com.zhouhao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouhao.common.R;
import com.zhouhao.dto.SetmealDto;
import com.zhouhao.entity.Category;
import com.zhouhao.entity.Setmeal;
import com.zhouhao.entity.SetmealDish;
import com.zhouhao.mapper.CategoryMapper;
import com.zhouhao.mapper.SetmealDishMapper;
import com.zhouhao.mapper.SetmealMapper;
import com.zhouhao.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public R saveWithDish(SetmealDto setmealDto) {
        setmealMapper.insert(setmealDto);

        Long setmealDtoId = setmealDto.getId();
        for(SetmealDish setmealDish: setmealDto.getSetmealDishes()) {
            setmealDish.setSetmealId(setmealDtoId);
            setmealDishMapper.insert(setmealDish);
        }

        return R.success("我成功了");
    }

    @Override
    public R list(int page, int pageSize, String name) {
        IPage<Setmeal> pages = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        if(name != null){
            queryWrapper.like(Setmeal::getName, name);
        }
        setmealMapper.selectPage(pages, queryWrapper);

        IPage<SetmealDto> dtoIPage = new Page<>();
        BeanUtils.copyProperties(pages, dtoIPage, "records");
        List<Setmeal> setmeals = pages.getRecords();
        List<SetmealDto> setmealDtos = new ArrayList<>();
        for(Setmeal setmeal: setmeals){
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category category = categoryMapper.selectById(setmealDto.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            setmealDtos.add(setmealDto);
        }
        dtoIPage.setRecords(setmealDtos);

        return R.success(dtoIPage);
    }

    @Override
    public R remove(Long[] ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        List<Setmeal> setmeals = setmealMapper.selectList(queryWrapper);

        for(Setmeal setmeal: setmeals){
            if(setmeal.getStatus() == 1){
                return R.error("不好意思，有人钉子户");
            }
        }

        for(Setmeal setmeal: setmeals){
            Long setmealId = setmeal.getId();
            setmealMapper.deleteById(setmealId);
            LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(SetmealDish::getSetmealId, setmealId);
            setmealDishMapper.delete(queryWrapper2);
        }

        return R.success("got it");
    }

    @Override
    public R list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus, setmeal.getStatus());
        List<Setmeal> list = this.list(queryWrapper);

        List<SetmealDto> setmealDtos = list.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper2);
            setmealDto.setSetmealDishes(setmealDishes);

            return setmealDto;
        }).collect(Collectors.toList());

        return R.success(setmealDtos);
    }
}
