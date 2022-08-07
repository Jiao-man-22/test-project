package com.jiao.testproject.testproject.controller;
import com.jiao.testproject.testproject.dao.ICustomerRepository;
import com.jiao.testproject.testproject.dao.impl.CustomerCrudRepository;
import com.jiao.testproject.testproject.dto.AjaxResult;
import com.jiao.testproject.testproject.dto.FileDto;
import com.jiao.testproject.testproject.dto.UserDto;
import com.jiao.testproject.testproject.dto.UserDtoVo;
import com.jiao.testproject.testproject.entity.*;
import com.jiao.testproject.testproject.services.IFileService;
import com.jiao.testproject.testproject.services.IUserService;
import com.jiao.testproject.testproject.utils.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import netscape.javascript.JSObject;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;

import java.util.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/common")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private  IFileService fileService;

    @Resource
    private  CustomerCrudRepository customerCrudRepository;

    @Resource
    private ICustomerRepository customerRepositoryImpl;

    private static final HashMap<String,Object> dataStore=new HashMap<>();

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/doLogin")
    public AjaxResult doLogin(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) Integer userid){
        if (username == null || password ==null){
            return AjaxResult.error("用户名 | 密码 | userid 为 null");
        }else{
            UserDto userDto = new UserDto();
            userDto.setUserName(username);
            userDto.setPassword(password);
            userDto.setUuid(userid);
            //UserEntity userEntity = userService.selectUserById(userDto);
            int overloadFlag=1;
            UserEntity userEntity = userService.selectUserById(userDto, overloadFlag);
            if (userEntity!=null
               && userDto.getUserName().equals(userEntity.getUser_name())
               && userDto.getPassword().equals(userEntity.getUser_password())){
                if (userEntity.getRole().equals(1) ){
                    dataStore.put("userId",userEntity.getUser_id());
                    UserDto userDto_1=new UserDto();
                    userDto_1.setStatus(Integer.valueOf(0));
                    userService.logOutUser(userDto_1);
                    return AjaxResult.success("1",userEntity);
                }else if (!userEntity.getRole().equals(1)) {
                    //0 :common
                    UserDto userDto_1=new UserDto();
                    userDto_1.setStatus(Integer.valueOf(0));
                    userService.logOutUser(userDto_1);
                    return AjaxResult.success("0",userEntity);
                }
                return AjaxResult.success("登陆失败");
            }
        }
    return AjaxResult.error("NG");
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file , UserDtoVo userdata) throws Exception
    {
        try
        {
            // 存放的文件路径
            String filePath = FileUtils.getImportPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = filePath + fileName;
            AjaxResult ajax = AjaxResult.success();
            FileDto fileDto = new FileDto();
            fileDto.setUserId(Integer.valueOf(userdata.getUserId()));
            fileDto.setFilePath(url);
            fileDto.setFileName(fileName);
            fileDto.setFileSize(String.valueOf(file.getSize()));
            fileDto.setUploadTime(DateUtils.getSysTime());
            Object userId_1 = dataStore.get("userId");
            if (userId_1 instanceof Integer){
                fileDto.setUserId((Integer) userId_1);
            }
            int i = fileService.insertFileById(fileDto);
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return AjaxResult.success();
        }
        catch (Exception e)
        {
            log.error("通用上传请求  error");

            return AjaxResult.error();
        }
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response)
    {
        try
        {
            String realFileName=null;
            String filePath=null;
            List<FileEntity> fileEntities = fileService.selectFileByFileName(fileName);
            if (fileEntities == null || fileEntities.size() < 1){
                log.error("fileEntities null | size=0 ");
            }
            for (FileEntity f:fileEntities) {
                realFileName= f.getFileName();
                filePath = f.getFilePath() + File.separator+ f.getFileName();
                String s = StringUtils.excludeString(filePath);
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                FileUtils.setAttachmentResponseHeader(response, realFileName);
                FileUtils.writeBytes(s, response.getOutputStream());
            }
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    @GetMapping("/test")
    @Transactional
    public void testJpaCrud() throws ParseException {
//        String sql="SELECT *  FROM filelist";
//         List list = customerCrudRepository.sqlObjectList(sql, null, FileEntity.class);
//
//        for (int i = 0; i < list.size(); i++) {
//            Object o = list.get(i);
//        }

        //customerRepositoryImpl

        TestTableEntity testTableEntity = new TestTableEntity();
        //发生这个原因是因为我们已经在实体类用JPA注解指定了主键的生成策略主键就不能设置了，一旦不为空或者0就被认为是已经保存到了数据库中，一旦调用persist()方法就会抛出上面的异常
        //testTableEntity.setId(3);
        testTableEntity.setName("rong");

        testTableEntity.setSalary(new BigDecimal(22.20));

        testTableEntity.setTime_1(new Date(System.currentTimeMillis()));

        //

        Date date = new Date();
        //SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //String format = dateFormat.format(date);
        //Date parse = dateFormat.parse(format);
        testTableEntity.setTime_2(new Date());
        //SQL查询从表中选择。JPQL从应用程序域模型的实体中选择。
//        List<UserEntity> l = em.createQuery("SELECT e FROM UserEntity e").getResultList();
//        for(UserEntity p:l){
//            System.out.println(p);
//        }
        //Example<TestTableEntity> example = Example.of(testTableEntity);
        // customerRepositoryImpl.findOne(example);
        // em.persist(testTableEntity);
        //通过entityManager将一个存在的实体“同步到”persistenceContext中。
        //em.merge(testTableEntity);
        //em.refresh(testTableEntity);
        //TestTableEntity testTableEntity1 = em.find(TestTableEntity.class, 1);

//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
//        QFileEntity qfileEntity = QFileEntity.fileEntity;
//        QUserEntity quserEntity = QUserEntity.userEntity;
//        jpaQueryFactory.select();
        StringBuilder content = new StringBuilder(16);
        final MutableBoolean mutableBoolean = new MutableBoolean(true);
        Map<Integer,Integer> map = new HashMap<>(16);
        for (int i = 0; i <10 ; i++) {
            map.put(i+1,i+1);
        }
        map.forEach((k,v)->{
            if (v == 5) {
                System.out.println("55555555555");
                return;
              }
            System.out.println("vvvvvvvv" +v);
            if (v == 8) {
             System.out.println("ssssssssssss");
            }
        });
        System.out.println("way outer");
    }

    public void test(){
    }






}








