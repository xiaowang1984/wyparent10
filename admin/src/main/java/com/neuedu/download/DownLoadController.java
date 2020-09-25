package com.neuedu.download;

import com.neuedu.pojo.UmsAdmin;
import com.neuedu.service.IUmsAdminService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/ums-admin")
public class DownLoadController {
    @Resource
    IUmsAdminService umsAdminService;
    @GetMapping("/download")
    void download(HttpServletResponse response) throws IOException {
        int index = 0;
        List<UmsAdmin> list = umsAdminService.list();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "用户列表";
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet");
        HSSFRow head = sheet.createRow(index++);
        head.createCell(0).setCellValue("编号");
        head.createCell(1).setCellValue("昵称");
        head.createCell(2).setCellValue("邮箱");
        head.createCell(3).setCellValue("手机号");
        head.createCell(4).setCellValue("是否失效");
        head.createCell(5).setCellValue("最后一次登录时间");
        for(UmsAdmin admin : list) {
            HSSFRow row = sheet.createRow(index++);
            row.createCell(0).setCellValue(admin.getId());
            row.createCell(1).setCellValue(admin.getName());
            row.createCell(2).setCellValue(admin.getMail());
            row.createCell(3).setCellValue(admin.getPhone());
            row.createCell(4).setCellValue(admin.getActive()==1?"有效":"失效");
            row.createCell(5).setCellValue(admin.getLastlogin()==null ? "" : admin.getLastlogin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        workbook.write(response.getOutputStream());
    }


}
