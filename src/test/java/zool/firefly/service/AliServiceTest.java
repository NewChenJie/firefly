package zool.firefly.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zool.firefly.FireflyApplicationTests;

import java.util.List;

import static org.junit.Assert.*;

@Component
public class AliServiceTest extends FireflyApplicationTests{

    @Autowired
    AliService aliService;

    @Value("${version}")
    String version;

    @Test
    public void selectAliCustomer() {
        System.out.println(version);
        List list = aliService.selectAliCustomer("宜春");
        Assertions.assertThat(list).size();
    }
}