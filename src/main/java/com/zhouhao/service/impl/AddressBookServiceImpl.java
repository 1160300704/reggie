package com.zhouhao.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouhao.common.R;
import com.zhouhao.entity.AddressBook;
import com.zhouhao.mapper.AddressBookMapper;
import com.zhouhao.service.AddressBookService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    public R setDefault(Map map) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(AddressBook::getIsDefault, 0);
        this.update(updateWrapper);

        Long id = Long.parseLong((String)map.get("id"));
        updateWrapper.set(AddressBook::getIsDefault, 1);
        updateWrapper.eq(AddressBook::getId, id);
        this.update(updateWrapper);
        return R.success("好嘞");
    }
}
