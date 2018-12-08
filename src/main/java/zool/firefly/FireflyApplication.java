package zool.firefly;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
@EnableDubboConfiguration
@ServletComponentScan
@EnableCaching
@tk.mybatis.spring.annotation.MapperScan(basePackages = "zool.firefly.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class FireflyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireflyApplication.class, args);
    }

}
