package com.device.service;

import com.device.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试服务层的 UserService
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserList() {
        List<User> lists = userService.getUserList();
        for (User it : lists) {
            System.out.println("名称:" + it.getUserName());
        }
    }
}