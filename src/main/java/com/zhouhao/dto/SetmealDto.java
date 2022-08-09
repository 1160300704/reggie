package com.zhouhao.dto;

import com.zhouhao.entity.Setmeal;
import com.zhouhao.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
