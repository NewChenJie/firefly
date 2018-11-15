package zool.firefly.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : zoolye
 * @date : 2018-11-03 17:07
 * @describe :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AliCustomerVo {

    private String company;

    private String ali_url;

    private String address;

    private String gmt_identification;

    private String industry;

    private String mianbusiness;

}
