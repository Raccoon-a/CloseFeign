package cn.rylan.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AutoConfig {


    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3600);
        requestFactory.setReadTimeout(3600);
        return new RestTemplate(requestFactory);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return  new ObjectMapper();
    }

}
