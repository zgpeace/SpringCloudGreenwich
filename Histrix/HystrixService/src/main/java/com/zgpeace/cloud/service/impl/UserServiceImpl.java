package com.zgpeace.cloud.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.zgpeace.cloud.domain.CommonResult;
import com.zgpeace.cloud.domain.User;
import com.zgpeace.cloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class UserServiceImpl implements UserService {

  private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
  @Autowired
  private RestTemplate restTemplate;
  @Value("${service-url.user-service}")
  private String userServiceUrl;

  @HystrixCommand(fallbackMethod = "getDefaultUser")
  @Override
  public CommonResult getUser(Long id) {
    return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
  }

  public CommonResult getDefaultUser(@PathVariable Long id) {
    User defaultUser = new User(-1L, "defaultUser", "123456");
    return new CommonResult<>(defaultUser);
  }

  @HystrixCommand(fallbackMethod = "getDefaultUser",
    commandKey = "getUserCommand",
    groupKey = "getUserGroup",
    threadPoolKey = "getUserThreadPool")
  @Override
  public CommonResult getUserCommand(Long id) {
    return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
  }

  @HystrixCommand(fallbackMethod = "getDefaultUser2",
    ignoreExceptions = {NullPointerException.class})
  @Override
  public CommonResult getUserException(Long id) {
    if (id == 1) {
      throw new IndexOutOfBoundsException();
    } else if (id == 2) {
      throw new NullPointerException();
    }
    return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
  }


  public CommonResult getDefaultUser2(Long id, Throwable e) {
    LOGGER.error("getDefaultUser2 id:{}, throwable class:{}", id, e.getClass());
    User defaultUser = new User(-2L, "defaultUser2", "123456");
    return new CommonResult(defaultUser);
  }

  @CacheResult(cacheKeyMethod = "getCacheKey")
  @HystrixCommand(fallbackMethod = "getDefaultUser",
    commandKey = "getUserCache")
  @Override
  public CommonResult getUserCache(Long id) {
    LOGGER.info("getUserCache id:{}", id);
    return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
  }

  public String getCacheKey(Long id) {
    LOGGER.info("enter method getChachKey id>>> {}", id);
    return String.valueOf(id);
  }

  @CacheRemove(commandKey = "getUserCache", cacheKeyMethod = "getCacheKey")
  @HystrixCommand
  @Override
  public CommonResult removeCache(Long id) {
    LOGGER.info("removeCache id:{}", id);
    return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, CommonResult.class, id);
  }

  @HystrixCollapser(batchMethod = "getUserByIds", collapserProperties = {
      @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
  })
  @Override
  public Future<User> getUserFuture(Long id) {
    return new AsyncResult<User>() {
      @Override
      public User invoke() {
        CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
        Map data = (Map)commonResult.getData();
        User user = BeanUtil.mapToBean(data, User.class, true);
        LOGGER.info("getUserById username:{}", user.getUserName());
        return user;
      }
    };
  }

  @HystrixCommand
  public List<User> getUserByIds(List<Long> ids) {
    LOGGER.info("getUserByIds:{}", ids);
    CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/getUserByIds?ids={1}", CommonResult.class, CollUtil.join(ids, ","));

    return (List<User>)commonResult.getData();
  }
}
