package cn.customer.controller;

import cn.customer.feign.UserClient;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TController {

    @Autowired
    private UserClient userClient;


    @GetMapping("/customer/user/id/{id}")
    public User getUserInfo(@PathVariable("id") Integer id) {
        return userClient.getUserById(id);
    }

}
