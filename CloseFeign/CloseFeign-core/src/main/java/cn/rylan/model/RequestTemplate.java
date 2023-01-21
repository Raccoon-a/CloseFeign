package cn.rylan.model;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;



public class RequestTemplate {
    private HttpHeaders headers;

    private HttpEntity<String> entity;

    private String body;

    public RequestTemplate(){

    }
    public RequestTemplate(HttpHeaders headers, HttpEntity<String> entity) {
        this.headers = headers;
        this.entity = entity;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public HttpEntity<String> getEntity() {
        return this.entity = new HttpEntity<String>(body,this.headers);
    }


    public void body(String body){
        this.body = body;
    }
}
