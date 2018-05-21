package com.qiniu.service.impl;

import com.qiniu.entity.User;
import com.qiniu.entity.vo.UserLoginVO;
import com.qiniu.repository.UserRepository;
import com.qiniu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User edit(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    public void delete(String acount) {
        userRepository.deleteByAccount(acount);
    }

    @Override
    public User findByAccountAndPassword(UserLoginVO user) {
        return userRepository.findByAccountAndPassword(user.getAccount(), user.getPassword());
    }

    @Override
    public void updateQuaAndUnQua(User user) {
        userRepository.save(user);
//        userRepository.updateQuaNumAndUnQuaNum(user.getQuaNum(), user.getUnQuaNum(), user.getId());
    }
}


