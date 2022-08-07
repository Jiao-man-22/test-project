package com.jiao.testproject.testproject.utils;



import com.deepoove.poi.XWPFTemplate;

import com.deepoove.poi.data.PictureRenderData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WordUtils {

    /** 日志对象 **/
    private static final Logger LOGGER = LoggerFactory.getLogger(WordUtils.class);

    /** 临时目录 **/
    private static final String TEMP_PATH = "temp/";

    /** 临时docx文件 **/
    private static final String TEMP_FILE_DOCX = "temp.docx";

    /** utf-8字符编码 **/
    private static final String CHARSET_UTF8 = "UTF-8";

    /** 缓冲区大小1M **/
    private static final int BUFFER_SIZE = 1024 * 1024;


    /** 私有构造器 **/
    public WordUtils(OutputStream os) throws IOException {
        this.exportWord(os);
    }

    public void exportWord(OutputStream os) throws IOException {

        /*
         * 行间距没有找到怎么设置的，用的换行？？？？
         * */

        //String path = this.getClass().getResource("/file-template/template.docx").getPath();
        //File templateFile = new File(path);
        //Word07Writer writer = WordUtil.getWriter(templateFile);
        //writer.addText(new Font("宋体", Font.PLAIN, 22), "真特么是sb");
        //writer.addText(new Font("宋体", Font.PLAIN, 22), "我是正文第一部分", "我是正文第二部分");
        //InputStream inputStream = this.getClass().getResourceAsStream("/file-template/template_older.doc");
        //InputStream inputStream =this.getClass().getResourceAsStream("template");
//        HWPFDocument document = null;
//        try {
//            document = new HWPFDocument(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 读取文本内容
        //Range bodyRange = document.getRange();

        //document.write(os);
        // 替换内容
//        for (Map.Entry<String, String> entry : contentMap.entrySet()) {
//            bodyRange.replaceText("${" + entry.getKey() + "}", entry.getValue());
//
//        }
        //writer.flush(os);
        //writer.flush(FileUtil.file("D:/wordWrite.docx"));
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("test00", "这是本文标题");
        //有样式的文本
//        map.put("test01", Texts.of("作者：刘七八").color("000000").create());
//        //超链接和锚点文本
        //map.put("link", Texts.of("本文链接").link("https://blog.csdn.net/my_batis/article/details/120410885").create());
        map.put("test02",dateFormat.format(date));
        map.put("test01","IT部测试是否会自动----------------------------------"
                + "------------------------------------------------------"
                + "----------------------------------------------------换行" );
        map.put("test04","另一自然段" );
        map.put("test05","姓名" );
        map.put("test06","张三");
        map.put("test07","李四" );
        map.put("test08","职业" );
        map.put("test09","散仙" );
        map.put("test10","半仙" );
        map.put("img", new PictureRenderData(100, 100, "D://weChat.jpg"));
        // svg图片
        //map.put("img", "D://weChat.jpg");
        String path = this.getClass().getResource("/file-template/template.docx").getPath();

        XWPFTemplate template = XWPFTemplate.compile(path).render(map);
        FileOutputStream out = new FileOutputStream(new File("D:\\输出自我介绍.docx"));
        template.write(out);
        template.write(os);

    }


}

















