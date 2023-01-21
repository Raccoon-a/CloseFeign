# CloseFeign

<details><summary>展开/收起</summary>

缘由是前天在刷知乎的时候看到这样一个问题：[为什么说Feign是伪RPC？](https://www.zhihu.com/question/298707085) 有些评论里的回答过于逆天，总是在拿传输层TCP协议和应用层HTTP协议比较这明显是认知有偏差，其实无论是下面哪种组合，本质上都是告知对方要执行哪个方法，什么参数，对方执行完后返回结果

[1] 应用层协议HTTP + HttpClient

[2] 应用层协议自定义/HTTP2 + 使用Netty自己构建的Client

首先说明个人观点，我觉得在Feign它是REST客户端的同时也能完成RPC的功能，因为不但支持服务发现并且在用法上和Dubbo，Montan等RPC框架几乎无异，都是不需要关注接口的具体实现即可完成远程服务方法的调用。

简述过程：向IOC容器中注入带有注解的接口类型对象(动态代理生成)，当执行FeignClient Bean中的方法时会触发代理对象Invoke()方法向远端发送请求，然后返回结果，这样就对于使用者屏蔽了服务发现和网络通信的细节，让使用者像调用本地接口一样简单。

正好最近在改之前写的自定义应用层协议RPC的各种bug，用Netty构建服务端，客户端实现双方通信写麻了，所以我就在想试试写一下Feign这种以访问对方暴露出HTTP REST接口的方式远程调用的框架，写个小demo由于不知道这个框架叫什么，众所周知SpringCloud有个组件叫OpenFeign，所以就叫CloseFeign了（狗头）
</details>

## DEMO项目简述

[√]  服务发现

[√]  负载均衡

[√]  超时处理

[x]  可替换HttpClient

## Getting started

进入目录转存至本地maven仓库供其他项目使用

`cd .\CloseFeign\CloseFeign-core\target\`

`mvn install:install-file -Dfile=CloseFeign-core-0.0.1-SNAPSHOT.jar -DgroupId=cn.rylan -DartifactId=CloseFeign -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar`

```yaml
server:
  port: 4000
spring:
  application:
    name: test
  cloud:
    close-feign:
      #负载均衡配置 [随机random - 轮询roundRobin]
      balancer: roundRobin
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848

```
```java
@SpringBootApplication
@EnableCloseFeign(basePackages = {"com.example.server.feign"})
public class TestServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestServerApplication.class, args);
    }
}
```

```java
@CloseFeignClient(serviceName = "core-application")
public interface FeignClient {

    @FeignRequestMapping(uri = "/material/id/1", type = "GET")
    CommonReturnType getById();

    @FeignRequestMapping(uri = "/material/id/{id}", type = "GET")
    CommonReturnType getById(@PathVariable("id") Long id );

    @FeignRequestMapping(uri = "/material/names", type = "POST")
    CommonReturnType getBatch(@RequestBody List<String> names);

    @FeignRequestMapping(uri = "/material/test", type = "POST")
    CommonReturnType test(@RequestBody Material material);
}
```
```java
@RestController
public class TestController {

    @Autowired
    FeignClient feignClient;

    @GetMapping("/test")
    public CommonReturnType test() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("西红柿", "玉米"));
        System.out.println(feignClient.getById());
        System.out.println(feignClient.getById(1L));
        System.out.println(feignClient.getBatch(list));
        return feignClient.test(new Material(1001L, "material", "icon", "category", "desc"));
    }
    
}
```
```java
    @Bean
    public RequestInterceptor interceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                assert requestAttributes != null;
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                String cookie = request.getHeader("Cookie");
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cookie", cookie);
                headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                headers.setContentType(MediaType.APPLICATION_JSON);
                template.setHeaders(headers);
            }
        };
    }
```