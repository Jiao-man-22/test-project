package com.jiao.testproject.testproject.controller;


import com.jiao.testproject.testproject.utils.ExeclUtil;
import com.jiao.testproject.testproject.utils.WordUtils;
import com.sun.deploy.net.URLEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/export")
public class ExportFileController {

   @RequestMapping("exportExcel")
   public void doExport(HttpServletResponse response){

        // 操作导出excel
        //excel标题
        String title = "挂号信息表";
        //excel列头信息
        String[] rowsName = new String[] { "门诊编号", "主治医师", "挂号时间", "挂号科室", "状态" };

        List<Object[]> dataList = new ArrayList<Object[]>();

        String[] testObj={"123456","jiao","13234","笑话内科","daa"};
        dataList.add(testObj);

        Object[] objs = null;

        //给文件命名。随机命名
        String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
        //告诉浏览器数据格式，将头和数据传到前台
        String headStr = "attachment; filename=\"" + fileName + "\"";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", headStr);
        try {
            OutputStream out = response.getOutputStream();
            //调用poi的工具类
            ExeclUtil ex = new ExeclUtil(title, rowsName, dataList);
            try {
                ex.export(out);
            } catch (Exception e) {
                e.printStackTrace();
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("doExportWord")
   public void doExportWord(HttpServletResponse response){
        // 导出到response输出流中
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            String fileName = "模板" + ".docx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            WordUtils wordUtil = new WordUtils(os);
        } catch (IOException e) {
            try {
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

   }

}
