package com.zgpeace.cloud.service;

import com.zgpeace.cloud.domain.User;

import java.util.List;

public interface UserService {
  void create(User user);

  User getUser(Long id);

  void update(User user);

  void delete(Long id);

  User getByUserName(String userName);

  List<User> getUserByIds(List<Long> ids);
}
