package com.neuedu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neuedu.pojo.UmsAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neuedu.util.DefaultExceptionBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2020-09-12
 */
public interface IUmsAdminService extends IService<UmsAdmin> {
    UmsAdmin getAdminByMail(String mail);
    UmsAdmin getAdminByPhone(String phone);
    UmsAdmin login(String username,String password) throws DefaultExceptionBean;
    boolean edit(UmsAdmin umsAdmin);
    IPage<UmsAdmin> page(String name,Integer active, Integer pageNo, Integer pageSize);
    List<UmsAdmin> list(String name,Integer active);
    boolean add(UmsAdmin umsAdmin);
}
