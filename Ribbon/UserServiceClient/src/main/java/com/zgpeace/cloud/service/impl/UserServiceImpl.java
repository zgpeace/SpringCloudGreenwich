package com.zgpeace.cloud.service.impl;

import com.zgpeace.cloud.domain.User;
import com.zgpeace.cloud.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
  private List<User> userList;

  @Override
  public void create(User user) {
    userList.add(user);
  }

  @Override
  public User getUser(Long id) {
    List<User> findUserList = userList.stream()
        .filter(userItem -> userItem.getId().equals(id))
        .collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(findUserList)) {
      return findUserList.get(0);
    }
    return null;
  }

  @Override
  public void update(User user) {
    userList.stream().filter(userItem -> userItem.getId().equals(user.getId()))
        .forEach(userItem -> {
          userItem.setUserName(user.getUserName());
          userItem.setPassword(user.getPassword());
        });
  }

  @Override
  public void delete(Long id) {
    User user = getUser(id);
    if (user != null) {
      userList.remove(user);
    }
  }

  @Override
  public User getByUserName(String userName) {
    List<User> findUserList = userList.stream().filter(userItem -> userItem.getUserName().equals(userName))
        .collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(findUserList)) {
      return findUserList.get(0);
    }
    return null;
  }

  @Override
  public List<User> getUserByIds(List<Long> ids) {
    return userList.stream().filter(userItem -> ids.contains(userItem.getId()))
        .collect(Collectors.toList());
  }

  @PostConstruct
  public void initData() {
    userList = new ArrayList<>();
    userList.add(new User(1L, "zgpeace", "123456"));
    userList.add(new User(2L, "John", "123456"));
    userList.add(new User(3L, "Gary", "123456"));
  }
}
