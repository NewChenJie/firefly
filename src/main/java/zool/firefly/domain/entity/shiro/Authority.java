package zool.firefly.domain.entity.shiro;

import javax.persistence.*;

@Table(name = "t_authority")
public class Authority {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 权限名称
     */
    @Column(name = "authority_name")
    private String authorityName;

    /**
     * 图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 请求uri
     */
    @Column(name = "uri")
    private String uri;

    @Column(name = "permission")
    private String permission;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取权限名称
     *
     * @return authority_name - 权限名称
     */
    public String getAuthorityName() {
        return authorityName;
    }

    /**
     * 设置权限名称
     *
     * @param authorityName 权限名称
     */
    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    /**
     * 获取图标
     *
     * @return icon - 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取请求uri
     *
     * @return uri - 请求uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置请求uri
     *
     * @param uri 请求uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * @param permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }
}