package cn.rylan.http;

import cn.rylan.model.RequestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestTemplateAPI {

    private ObjectMapper objectMapper = new ObjectMapper();


    private RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders headers = new HttpHeaders();


    public Object get(String url, Class<?> returnTYpe, RequestInterceptor interceptor) throws JsonProcessingException {
        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        HttpEntity<String> entity = new HttpEntity<>(template.getHeaders());
        template.setEntity(entity);
        HttpEntity<String> content = template.getEntity();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, content, String.class);
        String body = response.getBody();
        return objectMapper.readValue(body, returnTYpe);
    }

    public Object post(String url, Object body, Class<?> returnTYpe, RequestInterceptor interceptor) throws JsonProcessingException {
        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        String json = objectMapper.writeValueAsString(body);
        HttpEntity<String> entity = new HttpEntity<>(json,template.getHeaders());
        template.setEntity(entity);
        HttpEntity<String> content = template.getEntity();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, content, String.class);
        return objectMapper.readValue(response.getBody(), returnTYpe);
    }


}
