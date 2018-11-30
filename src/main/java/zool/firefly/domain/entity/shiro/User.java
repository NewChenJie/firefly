package zool.firefly.domain.entity.shiro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name = "t_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "username")
    @NotNull
    private String username;

    /**
     * 密码
     */
    @Column(name = "password")
    @NotNull
    private String password;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 账户状态 1锁定 0正常
     */
    @Column(name="state")
    private Integer state;

}