package zool.firefly.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zool.firefly.domain.vo.AliCustomerVo;

import java.util.List;

/**
 * @author : zoolye
 * @date : 2018-11-03 12:10
 * @describe :
 */
@Repository
public interface AliCustomerMapper {

    List<AliCustomerVo> selectCustomer(@Param("name") String name);

}
