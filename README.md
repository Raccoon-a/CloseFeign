# CloseFeign

## 简述
DEMO项目
[√]  服务发现
[√]  基于注解
[x]  超时处理
[x]  可替换HttpClient
<details><summary>展开/收起</summary>

缘由是前天在刷知乎的时候看到这样一个问题：为什么说Feign是伪RPC？https://www.zhihu.com/question/298707085 有些评论里的回答过于逆天，总是在拿传输层TCP协议和应用层HTTP协议比较

首先说明个人观点，我认为Feign就是RPC框架，因为在用法上它和Dubbo，Montan等RPC框架无异，都是不需要关注接口的具体实现即可完成远程调用，正巧最近在改之前写的RPC框架bug，那个是自定义应用层协议使用Netty构建了客户端和服务端进行通信，所以我就在想试试写一下Feign这种以访问对方暴露出HTTP REST接口的方式远程调用的框架，原理和之前写的基本没区别：


只不过需要按照http协议的格式去访问对方暴露的接口，但本质上都是告知对方要执行哪个方法，什么参数，对方执行完后返回结果

简述过程：向IOC容器中注入带有注解的接口对象(动态代理生成)，当执行bean的方法时会触发代理对象的Invoke()方法向远端发送请求，然后返回结果，这样就对于使用者屏蔽了服务发现和网络通信的细节，让使用者像调用本地接口一样简单。

正好最近在改之前写的自定义应用层协议RPC的各种bug，用Netty构建服务端，客户端实现双方通信写麻了，而像Feign这种使用HttpClient发送请求太方便了，所以写个小demo，由于不知道这个框架叫什么，众所周知SpringCloud有个组件叫OpenFeign，所以就叫CloseFeign了（狗头）
</details>

## Getting started
自定义注解： `@FeignClient` `@FeignRequestMapping` `@EnableCloseFeign`

SpringMVC注解：`@PathVariable`  `@RequestBody` 
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
@FeignClient(serviceName = "core-application")
public interface CloseFeignClient {

    @FeignRequestMapping(uri = "/material/id/1", type = "GET")
    public CommonReturnType getById();

    @FeignRequestMapping(uri = "/material/id/{id}", type = "GET")
    public CommonReturnType getById(@PathVariable("id") Long id );

    @FeignRequestMapping(uri = "/material/names", type = "POST")
    public CommonReturnType getBatch(@RequestBody List<String> names);

    @FeignRequestMapping(uri = "/material/test", type = "POST")
    public CommonReturnType test(@RequestBody Material material);
}
```
```java
@RestController
public class TestController {

    @Autowired
    CloseFeignClient closeFeignClient;

    @GetMapping("/test")
    public CommonReturnType test() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("西红柿", "玉米"));
        System.out.println(closeFeignClient.getById());
        System.out.println(closeFeignClient.getById(1L));
        System.out.println(closeFeignClient.getBatch(list));
        return closeFeignClient.test(new Material(1001L, "material", "icon", "category", "desc"));
    }
    
}
```
