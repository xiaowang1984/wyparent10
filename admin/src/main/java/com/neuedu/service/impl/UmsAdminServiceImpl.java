package com.neuedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neuedu.pojo.MailVo;
import com.neuedu.pojo.UmsAdmin;
import com.neuedu.mapper.UmsAdminMapper;
import com.neuedu.service.IUmsAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.service.MailService;
import com.neuedu.util.DefaultExceptionBean;
import com.neuedu.util.JwtUtil;
import com.neuedu.util.ResultCode;
import com.neuedu.util.ResultData;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2020-09-12
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements IUmsAdminService {
    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    MailService mailService;
    @Override
    public UmsAdmin getAdminByMail(String mail) {
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq("mail",mail);
        return this.getOne(wrapper);
    }

    @Override
    public UmsAdmin getAdminByPhone(String phone) {
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        return this.getOne(wrapper);
    }

    @Override
    public UmsAdmin login(String username,String password) throws DefaultExceptionBean {
        Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        Matcher matcher = pattern.matcher(username);
        UmsAdmin admin = null;
        admin = matcher.matches()?getAdminByMail(username):getAdminByPhone(username);
        if(null == admin) {
            throw new DefaultExceptionBean(ResultCode.FAILED,"用户名或密码不存在");
        } else {
            if(!bCryptPasswordEncoder.matches(password,admin.getPassword())) {
                throw new DefaultExceptionBean(ResultCode.FAILED,"用户名或密码不存在");
            } else if(admin.getActive() == 0){
                throw new DefaultExceptionBean(ResultCode.FAILED,"该用户已经失效无法登录");
            }else {
                admin.setLastlogin(LocalDateTime.now());
                edit(admin);
                return admin;
            }
        }
    }
    @Override
    public boolean edit(UmsAdmin umsAdmin) {
        return this.updateById(umsAdmin);
    }

    @Override
    public IPage<UmsAdmin> page(String name, Integer active, Integer pageNo, Integer pageSize) {
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(name))
            wrapper.like("name",name);
        if(null != active)
            wrapper.eq("active",active);
        return this.page(new Page<UmsAdmin>(pageNo,pageSize), wrapper);
    }

    @Override
    public List<UmsAdmin> list(String name, Integer active) {
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(name))
            wrapper.like("name",name);
        if(null != active)
            wrapper.eq("active",active);
        return list(wrapper);
    }
    @Override
    public boolean add(UmsAdmin umsAdmin) {
        String password =  RandomStringUtils.random(8,true,true);
        UmsAdmin clone = ObjectUtils.clone(umsAdmin);
        umsAdmin.setPassword(password);
        clone.setPassword(bCryptPasswordEncoder.encode(password));

        return this.save(clone);
    }
}
