package zool.firefly.service;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zool.firefly.mapper.AliCustomerMapper;
import zool.firefly.util.KeyValue;

import java.util.List;

/**
 * @author : zoolye
 * @date : 2018-11-03 12:14
 * @describe :
 */
@Service
public class AliService {

    @Autowired
    AliCustomerMapper aliCustomerMapper;

    public KeyValue selectAliCustomer(String name){
        return KeyValue.ok(aliCustomerMapper.selectCustomer(name));
    }

}
