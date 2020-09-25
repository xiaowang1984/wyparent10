package com.neuedu.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangyu
 * @since 2020-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UmsAdmin implements Serializable,Cloneable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 0失效 1有效
     */
    private Integer active;
    private LocalDateTime lastlogin;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
