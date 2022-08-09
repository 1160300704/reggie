package com.zhouhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouhao.common.R;
import com.zhouhao.dto.SetmealDto;
import com.zhouhao.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    R saveWithDish(SetmealDto setmealDto);

    R list(int page, int pageSize, String name);

    R remove(Long[] ids);

    R list(Setmeal setmeal);
}
