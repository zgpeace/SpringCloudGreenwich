package com.zgpeace.cloud.controller;

import com.zgpeace.cloud.domain.CommonResult;
import com.zgpeace.cloud.domain.User;
import com.zgpeace.cloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

  private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private UserService userService;

  @PostMapping("/create")
  public CommonResult create(@RequestBody User user) {
    userService.create(user);
    return new CommonResult("操作成功", 200);
  }

  @GetMapping("/{id}")
  public CommonResult<User> getUser(@PathVariable Long id) {
    User user = userService.getUser(id);
    LOGGER.info("根据id获取用户信息，用户名称为：{}", user.getUserName());
    return new CommonResult<>(user);
  }

  @GetMapping("/getUserByIds")
  public CommonResult<List<User>> getUserByIds(@RequestParam List<Long> ids) {
    List<User> userList = userService.getUserByIds(ids);
    LOGGER.info("根据ids获取用户信息，用户列表为：{}", userList);
    return new CommonResult<>(userList);
  }

  @GetMapping("/getByUserName")
  public CommonResult<User> getByUserName(@RequestParam String userName) {
    User user = userService.getByUserName(userName);
    return new CommonResult<>(user);
  }

  @PostMapping("/update")
  public CommonResult update(@RequestBody User user) {
    userService.update(user);
    return new CommonResult("更新成功", 200);
  }

  @PostMapping("/delete/{id}")
  public CommonResult delete(@PathVariable Long id) {
    userService.delete(id);
    return new CommonResult("删除成功", 200);
  }

}





































