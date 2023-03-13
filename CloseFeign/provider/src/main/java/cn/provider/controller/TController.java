package cn.provider.controller;


import model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TController {

    @GetMapping("/provider/user/id/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        if (id != 1001) {
            return null;
        }
        return new User(1001, "fucker", "123456");
    }
}
