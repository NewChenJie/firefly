package zool.firefly.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ali_new_customer")
public class AliNewCustomer {
    @Id
    @Column(name = "id" )
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 公司名称
     */
    @Column(name = "company")
    private String company;

    /**
     * 完整的url
     */
    @Column(name = "origin_url")
    private String originUrl;

    /**
     * 阿里店铺地址
     */
    @Column(name = "ali_url")
    private String aliUrl;

    /**
     * 认证日期
     */
    @Column(name = "gmt_identification")
    private String gmtIdentification;

    /**
     * 公司所在地
     */
    @Column(name = "address")
    private String address;

    /**
     * 行业
     */
    @Column(name = "industry")
    private String industry;

    /**
     * 主营业务
     */
    @Column(name = "mianbusiness")
    private String mianbusiness;

    /**
     * 抓取日期
     */
    @Column(name = "gmt_get")
    private String gmtGet;

    @Column(name = "gmt_create")
    private Date gmtCreate;

}