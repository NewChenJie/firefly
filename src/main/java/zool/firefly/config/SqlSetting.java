package zool.firefly.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.datasource")
public class SqlSetting {
    private String url;
    private String driver;
    private String username;
    private String password;
    private boolean callSettersOnNulls;
}
