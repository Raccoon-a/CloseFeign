package cn.customer.feign;

import cn.rylan.annotation.CloseFeignClient;
import cn.rylan.annotation.FeignRequestMapping;
import model.User;
import org.springframework.web.bind.annotation.PathVariable;


@CloseFeignClient(serviceName = "provider")
public interface UserClient {

    @FeignRequestMapping(uri = "/provider/user/id/{id}", type = "GET")
    User getUserById(@PathVariable("id") Integer id);
}
