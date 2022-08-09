package com.zhouhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouhao.common.R;
import com.zhouhao.entity.AddressBook;

import java.util.Map;

public interface AddressBookService extends IService<AddressBook> {
    R setDefault(Map map);
}
