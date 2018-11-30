package zool.firefly.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("redis")
public class RedisSetting {

    private boolean usingSentinel;
    private Integer database;
    private String host;
    private Integer port;
    private Integer timeout;
    private Integer maxIdle;
    private Integer minIdle;
    private Integer maxActive;
    private Integer maxWait;
    private String luaPath;


}
