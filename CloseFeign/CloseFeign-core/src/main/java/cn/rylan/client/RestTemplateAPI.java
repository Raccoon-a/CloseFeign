package cn.rylan.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestTemplateAPI {

    private ObjectMapper objectMapper = new ObjectMapper();


    private RestTemplate restTemplate = new RestTemplate();

    public Object get(String address, String port, String uri, Class<?> returnTYpe) throws JsonProcessingException {
        String json = restTemplate.getForObject("http://" + address + ":" + port + uri, String.class);
        return objectMapper.readValue(json, returnTYpe);
    }

    public Object get(String address, String port, String uri, Object[] parmaValues, Class<?> returnTYpe) throws JsonProcessingException {
       String pathVariable = null;
       for (Object value : parmaValues){
           pathVariable = value.toString()+"/";
       }
        System.out.println("http://" + address + ":" + port + uri+pathVariable);
        String json = restTemplate.getForObject("http://" + address + ":" + port + uri+pathVariable, String.class);
        return objectMapper.readValue(json, returnTYpe);
    }

    public Object post(String address, String port, String uri, Object parmaValues, Class<?> returnTYpe) throws JsonProcessingException {
        String json = restTemplate.postForObject("http://" + address + ":" + port + uri, parmaValues, String.class);
        return objectMapper.readValue(json, returnTYpe);
    }



}
