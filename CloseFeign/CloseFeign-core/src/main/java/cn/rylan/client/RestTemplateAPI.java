package cn.rylan.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.client.RestTemplate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestTemplateAPI {

    private ObjectMapper objectMapper = new ObjectMapper();


    private RestTemplate restTemplate = new RestTemplate();

    public Object get(String url, Class<?> returnTYpe) throws JsonProcessingException {
        String json = restTemplate.getForObject(url, String.class);
        return objectMapper.readValue(json, returnTYpe);
    }

    public Object post(String url, Object body, Class<?> returnTYpe) throws JsonProcessingException {
        String json = restTemplate.postForObject(url,body, String.class);
        return objectMapper.readValue(json, returnTYpe);
    }



}
