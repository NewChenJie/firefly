package zool.firefly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "zool.firefly.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class FireflyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireflyApplication.class, args);
    }

}
