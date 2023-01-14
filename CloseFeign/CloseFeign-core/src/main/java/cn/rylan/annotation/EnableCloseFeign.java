package cn.rylan.annotation;

import cn.rylan.springboot.FeignClientScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(FeignClientScan.class)
public @interface EnableCloseFeign {
    String[] basePackages() default {};
    Class<?>[] classes() default {};
}
