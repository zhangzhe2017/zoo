package weixin.zoo;

import org.apache.log4j.PropertyConfigurator;
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
@ComponentScan(basePackages = {"weixin.zoo"})
@ImportResource("classpath:*.xml")
@EnableAutoConfiguration
public class Application {
    static {
        //初始化log4j
        PropertyConfigurator.configure(Application.class.getResource("/log4j.properties"));
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}