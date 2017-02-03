package weixin.connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by viczhang.zhangz on 2017/2/3.
 *
 *  springboot入口
 *
 */
@Configuration
@SpringBootApplication
@ComponentScan(basePackages = {"weixin.connection"})
@ImportResource("classpath:applicationContext-mybatis.xml")
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}