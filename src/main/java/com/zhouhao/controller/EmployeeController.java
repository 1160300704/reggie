package com.zhouhao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhouhao.common.R;
import com.zhouhao.entity.Employee;
import com.zhouhao.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    /**
     * 用户登录
     * @param employee
     * @return
     */
    @PostMapping("login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /**密码md5加密
         * 1、检查用户名是否存在
         * 2、密码是否正确
         * 查看员工状态
         * 存session
         */
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(queryWrapper);

        if(one != null){
            if(md5DigestAsHex.equals(one.getPassword())){
                if(one.getStatus() == 1){
                    request.getSession().setAttribute("employee", one);
                    return R.success(one);
                }else {
                    return R.error("员工已禁用");
                }
            }else{
                return R.error("密码错误");
            }
        }else {
            return R.error("用户不存在");
        }
    }

    @PostMapping("logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R save(ServletRequest servletRequest, @RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        boolean save = employeeService.save(employee);
        if(save){
            return R.success(null);
        }else{
            return R.error("新增员工失败");
        }
    }

    @GetMapping("page")
    public R<IPage<Employee>> list(int page, int pageSize, String name){
        IPage<Employee> pages = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if(name != null){
            queryWrapper.like(Employee::getName, name);
        }
        employeeService.page(pages, queryWrapper);
        return R.success(pages);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("well");
    }

    @GetMapping("/{id}")
    public R<Employee> modify(@PathVariable("id") Long id){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, id);
        Employee one = employeeService.getOne(queryWrapper);

        return R.success(one);
    }
}
