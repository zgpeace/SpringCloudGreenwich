package com.zgpeace.cloud.controller;

import com.zgpeace.cloud.domain.CommonResult;
import com.zgpeace.cloud.domain.User;
import com.zgpeace.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import cn.hutool.core.thread.ThreadUtil;

@RequestMapping("/user")
@RestController
public class UserHystrixController {

  @Autowired
  private UserService userService;

  @GetMapping("/testFallback/{id}")
  public CommonResult testFallback(@PathVariable Long id) {
    return userService.getUser(id);
  }

  @GetMapping("/testCommand/{id}")
  public CommonResult testCommand(@PathVariable Long id) {
    return userService.getUserCommand(id);
  }

  @GetMapping("/testException/{id}")
  public CommonResult testException(@PathVariable Long id) {
    return userService.getUserException(id);
  }

  @GetMapping("/testCache/{id}")
  public CommonResult testCache(@PathVariable Long id) {
    userService.getUserCache(id);
    userService.getUserCache(id);
    userService.getUserCache(id);

    return new CommonResult("testCache operate success" ,200);
  }

  @GetMapping("/testRemoveCache/{id}")
  public CommonResult testRemoveCache(@PathVariable Long id) {
    userService.getUserCache(id);
    userService.removeCache(id);
    userService.getUserCache(id);
    return new CommonResult("testRemoveCache operate success", 200);
  }

  @GetMapping("/testCollapser")
  public CommonResult testCollapser() throws ExecutionException, InterruptedException {
    Future<User> future1 = userService.getUserFuture(1L);
    Future<User> future2 = userService.getUserFuture(2L);
    future1.get();
    future2.get();
    ThreadUtil.safeSleep(200);
    Future<User> future3 = userService.getUserFuture(3L);
    future3.get();

    return new CommonResult("testCollapser operate success", 200);
  }

}
