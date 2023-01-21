package cn.rylan.springboot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.cloud.close-feign")
public class Properties {

    private String balancer;
}
