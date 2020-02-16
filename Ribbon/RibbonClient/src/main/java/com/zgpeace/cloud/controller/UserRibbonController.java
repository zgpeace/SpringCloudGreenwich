package com.zgpeace.cloud.controller;

import com.zgpeace.cloud.domain.CommonResult;
import com.zgpeace.cloud.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class UserRibbonController {

  @Autowired
  private RestTemplate restTemplate;
  @Value("${service-url.user-service}")
  private String userServiceUrl;

  @GetMapping("/{id}")
  public CommonResult getUser(@PathVariable Long id) {
    return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
  }

  @GetMapping("/getByUserName")
  public CommonResult getByUserName(@RequestParam String userName) {
    return restTemplate.getForObject(userServiceUrl + "/user/getByUserName?userName={1}", CommonResult.class, userName);
  }

  @GetMapping("/getEntityByUserName")
  public CommonResult getEntityByUserName(@RequestParam String userName) {
    ResponseEntity<CommonResult> entity = restTemplate.getForEntity(userServiceUrl + "/user/getByUserName?userName={1}", CommonResult.class, userName);
    if (entity.getStatusCode().is2xxSuccessful()) {
      return entity.getBody();
    } else {
      return new CommonResult("获取对象失败", 500);
    }
  }

  @PostMapping("/create")
  public CommonResult create(@RequestBody User user) {
    return restTemplate.postForObject(userServiceUrl + "/user/create", user, CommonResult.class);
  }

  @PostMapping("/update")
  public CommonResult update(@RequestBody User user) {
    return restTemplate.postForObject(userServiceUrl + "/user/update", user, CommonResult.class);
  }

  @PostMapping("/delete/{id}")
  public CommonResult delete(@PathVariable Long id) {
    return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, CommonResult.class);
  }

}










































