package zool.firefly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zool.firefly.dao.AliCustomerMapper;

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

    public List selectAliCustomer(String name){

        return aliCustomerMapper.selectCustomer(name);
    }

}
