package com.zhouhao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouhao.common.BaseContext;
import com.zhouhao.entity.AddressBook;
import com.zhouhao.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhouhao.common.R;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("list")
    public R<List<AddressBook>> list(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.get());
        return R.success(addressBookService.list(queryWrapper));
    }

    @PostMapping
    public R add(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.get());
        return R.success(addressBookService.save(addressBook));
    }

    @PutMapping("default")
    public R setDefault(@RequestBody Map map){
        return addressBookService.setDefault(map);
    }

    @GetMapping("default")
    public R getDefault(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        return R.success(addressBookService.getOne(queryWrapper));
    }
}
