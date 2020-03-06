package com.zgpeace.cloud.service;

import com.zgpeace.cloud.domain.CommonResult;
import com.zgpeace.cloud.domain.User;

import java.util.concurrent.Future;

public interface UserService {

  CommonResult getUser(Long id);

  CommonResult getUserCommand(Long id);

  CommonResult getUserException(Long id);

  CommonResult getUserCache(Long id);

  CommonResult removeCache(Long id);

  Future<User> getUserFuture(Long id);
}
