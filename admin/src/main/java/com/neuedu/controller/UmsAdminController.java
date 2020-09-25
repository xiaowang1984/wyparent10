package com.neuedu.controller;


import com.neuedu.pojo.UmsAdmin;
import com.neuedu.service.IUmsAdminService;
import com.neuedu.util.DefaultExceptionBean;
import com.neuedu.util.JwtUtil;
import com.neuedu.util.ResultCode;
import com.neuedu.util.ResultData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2020-09-12
 */
@RestController
@RequestMapping("/ums-admin")
public class UmsAdminController {
    @Resource
    IUmsAdminService umsAdminService;
    @PostMapping("/login")
    ResultData login(String username,String password) throws DefaultExceptionBean {
        UmsAdmin admin = umsAdminService.login(username,password);
        ResultData result = ResultData.success(admin.getId());
        result.setToken(JwtUtil.createTokenBySession(admin.getId(),admin.getPassword()));
        return result ;
    }
    @GetMapping("/checkmail")
    ResultData checkmail(String mail) {
        UmsAdmin umsAdmin = umsAdminService.getAdminByMail(mail);
        if(null == umsAdmin) {
            return ResultData.success(null,"email可以使用");
        } else {
            return ResultData.failed(ResultCode.FAILED, "email已经存在不可使用");
        }
    }
    @GetMapping("/checkphone")
    ResultData checkphone(String phone) {
        UmsAdmin umsAdmin = umsAdminService.getAdminByPhone(phone);
        if(null == umsAdmin) {
            return ResultData.success(null,"手机号可以使用");
        } else {
            return ResultData.failed(ResultCode.FAILED, "手机号已经存在,不可使用");
        }
    }

    @GetMapping("/list")
    ResultData list(String name, Integer active, Integer pageNo, Integer pageSize) {
        if(null == pageNo || null == pageSize) {
            return ResultData.success(umsAdminService.list(name, active));
        } else {
            return ResultData.success(umsAdminService.page(name, active, pageNo, pageSize));
        }
    }
    @PostMapping("/save")
    ResultData save(UmsAdmin umsAdmin) {
        if(null == umsAdmin.getId())
            return ResultData.success(umsAdminService.add(umsAdmin));
        else
            return ResultData.success(umsAdminService.edit(umsAdmin));
    }
    @GetMapping("/getbyid")
    ResultData getById(Integer id) {
        return ResultData.success(umsAdminService.getById(id));
    }
    @GetMapping("/del")
    ResultData del(UmsAdmin umsAdmin) {
        return ResultData.success(umsAdminService.updateById(umsAdmin));
    }


}
