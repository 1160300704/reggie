package com.zhouhao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouhao.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
