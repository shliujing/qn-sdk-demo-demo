package com.device.service;

import com.device.entity.User;
import com.device.entity.vo.UserLoginVO;

import java.util.List;

public interface UserService {

    public List<User> getUserList();

    public User findUserById(long id);

    public void save(User user);

    public User edit(User user);

    public void delete(long id);

    public void delete(String account);

    public User findByAccountAndPassword(UserLoginVO user);

    public void updateQuaAndUnQua(User user);
}
