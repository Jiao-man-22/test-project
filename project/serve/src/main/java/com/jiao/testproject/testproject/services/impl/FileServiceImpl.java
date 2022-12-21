package com.jiao.testproject.testproject.services.impl;

import com.jiao.testproject.testproject.dao.FileEntityMapper;
import com.jiao.testproject.testproject.dao.FileEntityRepository;
import com.jiao.testproject.testproject.dao.UserRepository;
import com.jiao.testproject.testproject.dto.FileDto;
import com.jiao.testproject.testproject.dto.FileViewVo;
import com.jiao.testproject.testproject.dto.FolderDto;
import com.jiao.testproject.testproject.dto.SideDtoVo;
import com.jiao.testproject.testproject.entity.*;
import com.jiao.testproject.testproject.entity.QFileEntity;
import com.jiao.testproject.testproject.services.IFileService;
import com.jiao.testproject.testproject.services.IUserService;
import com.jiao.testproject.testproject.utils.DateUtils;
import com.jiao.testproject.testproject.utils.FieldTranfUtils;
import com.jiao.testproject.testproject.utils.StringUtils;
import com.jiao.testproject.testproject.utils.TreeDataUtils;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class  FileServiceImpl implements IFileService {


    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

     //存放文件的根目录
    private static final String base_path = "E://VirtaulDisk";

    //共享文件目录
    private static final String share_stroen="E://VirtaulDisk//shareArea";

    //模拟网盘的根目录
    private static final String virture_base_path="E://VirtaulDisk";

    private static final String suffix_name="-VirtaulDisk";

    //默认上传文件的存放路径
    private static final String defaultUpload="E://VirtaulDisk//";

    //文件存放List
    private ArrayList<File> fileList = new ArrayList<>();

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    //这些文件夹不允许操作

    private static final String DEFAULTGEN = "-VirtaulDisk";

    private static final String DEFAULTSHAREFLODER = "shareFolder";

    private static final String DEFAULTUPLOAD = "defaultUpload";

    //文件size 单位
    private final static short BASE_AMOUNT= 10;

    private final static byte FILE_SIZE_B = 1;

    private final static int FILE_SIZE_KB = FILE_SIZE_B << BASE_AMOUNT;
    //用大了 没有影响
    private final static long FILE_SIZE_MB = FILE_SIZE_B << BASE_AMOUNT << BASE_AMOUNT;

    //计算 文件 的大小
    private final static Integer FILE_SIZE=0;

    @PersistenceContext
    private EntityManager entityManager;

    //查询工厂实体
    private JPAQueryFactory queryFactory;


    //实例化控制器完成后执行该方法实例化JPAQueryFactory
    @PostConstruct
    public void initFactory()
    {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Resource
    FileEntityMapper FileMapper;
    //集成jpa
    @Resource
    UserRepository userRepository;

    @Resource
    FileEntityRepository fileEntityRepository;

    @Autowired
    private IUserService userService;

    @Transactional
    @Override
    public int insertFileById(FileDto fileDto) {
        FileEntity fileEntity = new FileEntity();
        int inert_flag=0;
        // 项目赋值
        try{
            StringBuffer subFileName=new StringBuffer(fileDto.getFileName());
            int subIndex = subFileName.lastIndexOf("/");
            String fileName = StringUtils.substring(subFileName.toString(), subIndex+1);
            fileDto.setFileName(fileName);
            //处理filelist 存放文件的path
            // 截取文件名和路径
            //保证多线程处理
            StringBuffer subPathName=new StringBuffer(fileDto.getFilePath());
            int pathSubIndex = subPathName.lastIndexOf("/");
            String subPath = StringUtils.substring(subPathName.toString(), 0, pathSubIndex);
            fileDto.setFilePath(subPath);
            FieldTranfUtils.reflectField(fileDto,fileEntity);
            //inert_flag = FileMapper.insert(fileEntity);
            FileEntity save_result = fileEntityRepository.save(fileEntity);
            inert_flag=save_result.getFileId();
        }catch (Exception e){
            log.error("FileServiceImpl 赋值方法有error | inser方法出错 =====");
        }
        return inert_flag;
    }

    @Override
    public List<FileDto> selectFiles() {
        ArrayList<FileDto> fileDtos = new ArrayList<>();
        QFileEntity fileEntity = QFileEntity.fileEntity;
       // List<Tuple> fetch = queryFactory.select().from(fileEntity).fetch();

        List<FileEntity> fileEntities = FileMapper.selectByExample(new FileEntityExample());
        if (fileEntities == null || fileEntities.size()< 1 ){
            return null;
        }
        for (FileEntity fe:fileEntities) {
            this.setSuffix(fe.getFileSize(),fe);
            FileDto fileDto = new FileDto();
            try{
                FieldTranfUtils.reflectField(fe,fileDto);
            }catch (Exception e){
                log.error("FieldTranfUtils 失败"+e);
            }
            fileDtos.add(fileDto);
        }
        return fileDtos;
    }

    @Override
    public List<FileEntity> selectFileByUserId(Integer userId) {
        FileEntityExample fileEntityExample = new FileEntityExample();
        //定义where语句
        FileEntityExample.Criteria criteria = fileEntityExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        fileEntityExample.setOrderByClause("upload_time asc");
        List<FileEntity> fileEntities = FileMapper.selectByExample(fileEntityExample);
        for (FileEntity file:fileEntities) {
            String fileSize = file.getFileSize();
            this.setSuffix(fileSize,file);
        }
        return fileEntities;
    }

    @Override
    public List<FileEntity> selectFileByFileName(String fileName) {
        FileEntityExample fileEntityExample = new FileEntityExample();
        FileEntityExample.Criteria criteria = fileEntityExample.createCriteria();
        criteria.andFileNameEqualTo(fileName);
        List<FileEntity> fileEntities = FileMapper.selectByExample(fileEntityExample);
        return fileEntities;
    }

    @Override
    public int deleteFile(Integer fileId) {
        return 0;
    }

    @Override
    public int deleteFileByUserId(Integer userid) {
        return 0;
    }

    @Override
    public int delFileByFileId(Integer fileid) {

        FileEntity fileEntity = FileMapper.selectByPrimaryKey(fileid);

        File file = new File(virture_base_path);

        if (!fileList.isEmpty()){
            fileList.clear();
        }
        ArrayList<File> postFileList = IteraFolder(file);
        for (File f: postFileList) {
            if (f.getName() !=null && f.getName().contains(fileEntity.getFileName())){
               f.delete();
            }
        }
        int i = FileMapper.deleteByPrimaryKey(fileid);
        return i;
    }

    @Transactional
    @Override
    public int delFileByFileName(String filename) {
        FileEntityExample fileEntityExample = new FileEntityExample();
        FileEntityExample.Criteria criteria = fileEntityExample.createCriteria();
        criteria.andFileNameEqualTo(filename);
        //SqlSession sqlSession = MybatisUtils.getSqlSession();
        int i = FileMapper.deleteByExample(fileEntityExample);
        //sqlSession.commit();
        if (i==0){
            log.info("delete 数据影响行数" + i);
        }
        return i;
    }

    /*
    * 创建文件夹
    * */
    @Override
    public ArrayList<String> createFolders(String path ,String filename,@Nullable String flag) {
        ArrayList<String> folderList = new ArrayList<>();
        File file=null;
        StringBuilder mappingPath=null;
        if(!path.contains(filename)){
             mappingPath = new StringBuilder(path + File.separator + filename + new Random().nextInt(1000) );
        }else{
            mappingPath = new StringBuilder(path + new Random().nextInt(1000) );
        }
        //查看是否是全地址
        if (mappingPath.toString().contains("E:") && flag == null){
           file = new File(mappingPath.toString());
        }else if(mappingPath.toString().contains("E:") && flag != null){
            String childFileName="新建文件夹_"+DateUtils.getSysTimeDoId();
            file = new File(mappingPath.substring(0,mappingPath.length()-3),childFileName);
        }else{
            file=new File(virture_base_path+File.separator+mappingPath);
        }
        String fullName = file.getName();
        if (file.exists()){
            folderList.add(file.getAbsolutePath());
            return folderList;
        }else if (!file.exists()){
            // -1
            int indexOf = fullName.lastIndexOf(".");
            if (indexOf != -1){
                folderList.add(file.getAbsolutePath());
            }else{
               try{
                   boolean flag_temp=file.mkdirs();
                   if(flag_temp){
                       folderList.add(file.getAbsolutePath());
                   }
               }catch (Exception e){
                   log.error("创建文件夹异常" +e);
               }
            }
        }
        return folderList;
    }

    /*查询文件名*/
    @Override
    public File retrievalFileName(String fileName,@Nullable String path){
        String filepath="E://VirtaulDisk//ww_3-VirtaulDisk";
        try{
            File file =new File(filepath);
            ArrayList<File> files = IteraFolder(file);
            for (File f: files) {
                if(f.getName().equals(fileName)){
                    return f;
                }
            }
        }catch (Exception e){
            log.error(e+"====查询文件名出错=====");
            log.error(e.toString());
        }
        return null;
    }

    //文件的复制
    @Override
    public void copyFile(String sourcePath,@Nullable String targetPath,String filename)  {
        //获取多个参数
        String absolutPath=sourcePath;
        String targetPathName;
        if (targetPath == null || "".equals(targetPath)){
            targetPathName=share_stroen;
        }else {
             targetPathName=targetPath;
        }
        if (targetPath!=null && !"".equals(targetPath) && !targetPath.contains(filename)){
            targetPathName =targetPath +File.separator+ filename;
        }else {
            targetPathName += File.separator + filename;
        }
        FileInputStream fis=null;
        FileOutputStream fos=null;
        try{
            File file = new File(absolutPath);
            if (file.isFile()){
                fis = new FileInputStream(file);
                File target =new File(targetPathName);
                fos = new FileOutputStream(target);
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buf)) > 0) {
                    fos.write(buf, 0, bytesRead);
                }
            }else {
                log.info("文件不存在" );
            }
        }catch (Exception e){
            log.error("文件 复制NG " + e );
        }finally {
           try{
               fis.close();
               fos.close();
           } catch (IOException e){
               log.error("数据流 关闭异常 " +  e);
           }
        }
    }
    @Override
    public void copyFile(File source, File target) throws IOException {
        FileInputStream fis=null;
        FileOutputStream fos=null;
        if (!source.exists()){
            log.error("file not exist" + source);
        }
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buf)) > 0) {
                fos.write(buf, 0, bytesRead);
            }

        } catch (Exception e) {
            log.error("重载方法异常" +e);
        }finally {
            try{
                fos.flush();
                fis.close();
                fos.close();
                source.delete();
            } catch (IOException e){
                log.error("数据流 关闭异常 " +  e);
            }
        }

    }

    /*
    *设置文件共享
    * 防止多线程操作error
    *
    * @prama  File 文件类型
    * void    无返回值*/
    @Transactional
    @Override
    public synchronized String fileShare(String fileName, String absolutPath){
        try{
            File share_folder = new File(share_stroen);
            File file=null;
            if (share_folder.exists() && share_folder.isDirectory()){
                if (!absolutPath.contains(fileName)){
                     file=new File(absolutPath + File.separator +fileName);
                }else{
                     file=new File(absolutPath);
                }
                file.setReadable(true);
                int indexOf = absolutPath.lastIndexOf(":");
                String pathString = absolutPath.substring(indexOf + 1);
                //File file_target=new File(share_stroen +pathString);
                File file_target = new File(share_stroen+File.separator+ fileName);

                copyFile(absolutPath,fileName,file_target.getAbsolutePath());
                file_target.setReadable(true);
                file_target.setWritable(true);
                //this.copyFile(file,file_target);
                file_target.setWritable(false);
                return file_target.getAbsolutePath();
            }else if(!share_folder.exists()){
                share_folder.mkdirs();
                this.fileShare(fileName,absolutPath);
            }
        }catch (Exception e){
            log.error("文件共享 err"+e);
        }
        return null;
    }

    /*删除文件夹和文件*/
    @Override
    public boolean delFileByPath(String path) {
        File file = new File(path);
        log.info(" jpa 删除 "+file.getName());
        QFileEntity qfileEntity = QFileEntity.fileEntity;
        FileEntity fileEntity_result = queryFactory.select(qfileEntity)
                .from(qfileEntity)
                .where(qfileEntity.fileName.eq(file.getName()))
                .fetchOne();
        FileEntity fileEntity_param = new FileEntity();
        if (fileEntity_result == null || fileEntity_result.getFileId() == null || fileEntity_result.getFileName() == null){
            log.info("fileEntity_result : null " );
            return false;
        }
        fileEntity_param.setFileName(fileEntity_result.getFileName());
        fileEntity_param.setFileId(fileEntity_result.getFileId());
        fileEntityRepository.delete(fileEntity_param);
        if (file.exists() && file.isFile()){
            file.delete();
            return true;
        }else if (file.exists() && file.isDirectory()){
            delFolder(file);
            file.delete();
            return true;
        }
        return false;
    }

    /*初始化普通用户虚拟磁盘*/
    @Override
    public List<FolderDto> initUserVirtualDisk(String username, String userid) {
        List<FolderDto> folderDtos=null;
        FolderDto folderDto = new FolderDto();
        //存file 遍历赋值用
        List<File> files = new ArrayList<>();
        try{
            //在网盘根路径下面存放用户的文件夹
            File file = new File(virture_base_path ,username + "_" + userid+suffix_name);
            File uploadFolder = new File(file.getAbsolutePath(),"defaultUpload");
            File shareFolder = new File(file.getAbsolutePath(),"shareFolder");
            //文件存在 则 遍历文件结构
            if (file.exists() && uploadFolder.exists()){
                folderDtos = fileStructrue(file);
                return folderDtos;
            }else if(!file.exists()){
                file.mkdirs();
                uploadFolder.mkdir();
                shareFolder.mkdir();
                shareFolder.setReadOnly();
                files.add(file);
                files.add(uploadFolder);
                files.add(shareFolder);
                folderDtos = this.setFolder(files);
                return folderDtos;
            }
        }catch (Exception e){
            log.error("initUserVirtualDisk 方法 出现异常 " + e );
        }
        return null;
    }

    /*删除子文件夹*/
    private void delFolder(File folder){
        File[] files = folder.listFiles();
        try{
            for (File f:files) {
                if (f.isFile()){
                    f.delete();
                }else if (f.isDirectory()){
                    delFolder(f);
                    f.delete();
                }
            }
        }catch (Exception e){
            log.error("遍历删除异常"+ e);
        }
    }

    /*
     * 初始化磁盘模拟空间
     * */
    @Override
    public List<FolderDto> fileStructrue(){
        ArrayList<FolderDto> files_folders = new ArrayList<>();
        File file = new File(virture_base_path);
        if (file.exists()){
            IterafileStructrue(file,files_folders);
            //追加一个根目录
/*            FolderDto folderDto = new FolderDto();
            folderDto.setName(file.getName());
            folderDto.setParent_name(subFileName(file.getParent()));
            folderDto.setAbsolutePath(subFileName(file.getPath()));
            folderDto.setRole(0);*/
            //files_folders.add(folderDto);
        }else{
            File initFileStructrue = fileStructrueInit(virture_base_path);
            return files_folders;
        };
        return files_folders;
    }

    /*
    * 初始化磁盘模拟空间*/
    public File fileStructrueInit(String virture_base_path){
        File file = new File(virture_base_path);
        boolean mkdirs = file.mkdirs();
        if (mkdirs){
            return file;
        }
        return null;
    }

    //重载 生成文件目录方法
    public List<FolderDto> fileStructrue(File file){
        ArrayList<FolderDto> files_folders = new ArrayList<>();
            IterafileStructrue(file,files_folders);
            //追加一个根目录
            FolderDto folderDto = new FolderDto();
            folderDto.setName(file.getName());
            folderDto.setParent_name(subFileName(file.getParent()));
            folderDto.setAbsolutePath(file.getPath());
            folderDto.setRole(0);
            files_folders.add(folderDto);
        return files_folders;
    }

    /*遍历文件结构*/
    private void IterafileStructrue(File file,ArrayList<FolderDto> files_folders){
        try{
            File[] files=  file.listFiles();
            for (File f: files) {
                if (f.isFile()) {
                    FolderDto folderDto = new FolderDto();
                    folderDto.setName(f.getName());
                    folderDto.setParent_name(subFileName(f.getParent()));
                    folderDto.setAbsolutePath(f.getAbsolutePath());
                    folderDto.setRole(1);
                    files_folders.add(folderDto);
                } else if (f.isDirectory() && f.list() == null) {
                    FolderDto folderDto = new FolderDto();
                    folderDto.setName(f.getName());
                    folderDto.setParent_name(subFileName(f.getParent()));
                    folderDto.setAbsolutePath(f.getAbsolutePath());
                    folderDto.setRole(0);
                    files_folders.add(folderDto);
                } else if (f.isDirectory() && f.list() != null) {
                    FolderDto folderDto = new FolderDto();
                    folderDto.setName(f.getName());
                    folderDto.setParent_name(subFileName(f.getParent()));
                    files_folders.add(folderDto);
                    folderDto.setAbsolutePath(f.getAbsolutePath());
                    folderDto.setRole(0);
                    IterafileStructrue(f, files_folders);
                }
            }
        }catch (Exception e){
            log.error("遍历文件结构 error" + e );
        }

    }

    //遍历文件
     ArrayList<File> IteraFolder(File file){
        try{
           if (file.isFile()){
               fileList.add(file);
           }else if(file.isDirectory()){
               File[] files = file.listFiles();
               if (files !=null && files.length>0){
                   for ( File f:files) {
                       IteraFolder(f);
                   }
               }
           }
       }catch (Exception e){
            log.error("遍历出 error=====");
            e.printStackTrace();
       }
        return fileList;
    }

    /**
     *
     * @param f File
     * @return List<File> 一个 list文件的集合 包含递归后的所有文件
     */
       private List<File> modifyIterator(File f){
           ArrayList<File> returnArrayList = new ArrayList<>();
           if (f == null || !f.exists()){
               log.info("遍历文件对象为null 或者不存在"+f);
               return returnArrayList;
           }
           if (f.exists() && f.isFile()){
               //只要求目录 returnArrayList.add(f);
               return returnArrayList;
           }else if (f.exists() && f.isDirectory()){
               File[] tempFileList = f.listFiles();
                   returnArrayList.add(f);
               for (File ff: tempFileList) {
                   returnArrayList.addAll(modifyIterator(ff));
               }
           }
           return returnArrayList;
       }

    //处理文件上传
    @Transactional
    public void handleFileUpload(MultipartFile file, @Nullable String specificPath){
        String originalFilename = file.getOriginalFilename();
       // String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //UserDto user = SessionLocal.getUser();
        //存放输出文件
        File outFile=null;
        Map<String, UserEntity> userMap = userService.getUserInfoMap();
        UserEntity userinfo = userMap.get("userinfo");
        if (specificPath !=null && !specificPath.equals("''")){
             outFile = new File(specificPath,file.getOriginalFilename());
        }else {
             outFile = new File(defaultUpload +userinfo.getUser_name() +"_"+userinfo.getUser_id().toString()+ "-VirtaulDisk"+File.separator+"defaultUpload" + File.separator +file.getOriginalFilename());
        }
        FileEntity insertFileEntity = new FileEntity();
        insertFileEntity.setUserId(userinfo.getUser_id());
        insertFileEntity.setFileName(file.getOriginalFilename());
        insertFileEntity.setFilePath(outFile.getPath());
        insertFileEntity.setCreaterId(String.valueOf(userinfo.getUser_id()));
        insertFileEntity.setFileSize(String.valueOf(file.getSize()));
        insertFileEntity.setUploadTime(DateUtils.getSysTime());
        insertFileEntity.setIsDelete(0);
        insertFileEntity.setIsShare(0);
        //同名文件不可插入
        FileMapper.avoidRepeatInsert(insertFileEntity);
        try {
            file.transferTo(outFile);
        } catch (IOException e) {
            log.error("处理文件上传"+e);
        }
    }

    /*截取名字*/
    private String subFileName(String s){
       // int i = s.lastIndexOf("\\");OK
        int i = s.lastIndexOf(File.separator);
        return s.substring(i+1);
    }

    /*判断单位*/
    private  void setSuffix(String fileSize,Object file){
        if (fileSize == null || fileSize.equals("") || !isNumeric(fileSize)){
            log.info("fileSize === null | 含有非数字");
            return;
        }
        //小于 1k 显示 字节
        //大于 1k 小于 1m  显示 kb
        //大于 1m 显示 xxxMB
        Integer integer=null;
        // 取出单位的 二进制权重 刚好最高位为 1 其余为 0
        int powerNumberKb = Integer.toBinaryString(FILE_SIZE_KB).length() - 1 ;
        int powerNumberMb = Long.toBinaryString(FILE_SIZE_MB).length() - 1 ;
        if (file instanceof  FileEntity){
            FileEntity convertObj=(FileEntity) file;
            try{
                integer = Integer.valueOf(fileSize);
                if (integer < FILE_SIZE_KB){
                    convertObj.setFileSize(fileSize + "字节");
                }else if (integer > FILE_SIZE_KB && integer >> powerNumberMb <= 1){
                    int size_KB = integer >> powerNumberKb;
                    convertObj.setFileSize(size_KB + "KB");
                }else if(integer >> powerNumberMb > 1){
                    int size_MB = integer >> powerNumberMb;
                    convertObj.setFileSize(size_MB + "MB");
                }
            }catch(NumberFormatException e){
                log.error("setSuffix 方法 integer = " + integer + e);
            }
            return;
        }
        if (file instanceof  FileViewVo){
            FileViewVo convertObj=(FileViewVo) file;
            try{
                integer = Integer.valueOf(fileSize);
                if (integer < FILE_SIZE_KB){
                    convertObj.setFileSize(fileSize + "字节");
                }else if (integer > FILE_SIZE_KB && integer >> powerNumberMb <= 1){
                    int size_KB = integer >> powerNumberKb;
                    convertObj.setFileSize(size_KB + "KB");
                }else if(integer >> powerNumberMb > 1){
                    int size_MB = integer >> powerNumberMb;
                    convertObj.setFileSize(size_MB + "MB");
                }
            }catch(NumberFormatException e){
                log.error("setSuffix 方法 integer = " + integer + e);
            }
            return;

        }


    }

    //set FoderDto
    private List<FolderDto> setFolder(List<File> list){
        List<FolderDto> folderDtoList = new ArrayList<>();
        for (File file : list) {
            FolderDto folderDto = new FolderDto();
            folderDto.setName(file.getName());
            folderDto.setParent_name(subFileName(file.getParent()));
            folderDto.setAbsolutePath(file.getAbsolutePath());
            folderDto.setRole(file.isFile()?1:0);
            folderDtoList.add(folderDto);
        }
        return folderDtoList;
    }

    //追加一个share的文件内容
    public List<FolderDto>appendFolder(List<FolderDto> listA , @Nullable List<FolderDto> listB){
        if (listB == null || listB.size()<1){
            for (FolderDto fd: listA) {
                String fileName = fd.getName();
                if (fileName.contains("shareFolder")){
                    List<FolderDto> shareFolder = this.getShareFolder(fileName);
                    listB = shareFolder;
                    fileList.clear();
                    break;
                }
            }
        }
        for (FolderDto fd:listB) {
            //有一个放一个
            listA.add(fd);
        }
        return listA;
    }

    @Override
    public List<FileViewVo> netDiskViewInit(String userName, String userId , @Nullable String absolutePath) {
        List<FileViewVo> resultFileViewList = new ArrayList<>();

        if (absolutePath == null || absolutePath.equals("''")){
            //拿到磁盘文件夹路径
            String fileName=userName+"_"+userId+"-VirtaulDisk";
            String filePath=base_path+File.separator+fileName;
            File userGenFolder = new File(filePath);
            if (!userGenFolder.exists() || userGenFolder.isFile()){
                log.info("用户根文件夹不存在");
                return null;
            }
            fileList.clear();
            List<File> tempList = iteratorFolder(userGenFolder);
            //追加gen目录
            tempList.add(userGenFolder);
            //转成特定的 dto封装类
            resultFileViewList = dtoToResultDto(tempList);
            fileList.clear();
            return resultFileViewList;
        }else if (absolutePath!=null && !absolutePath.equals("''")){
            File file = new File(absolutePath);
            if (file.exists()){
                File[] fileArray = file.listFiles();
                //Arrays的内部类ArrayList没有重写 AbstractList的add和remove方法
                ArrayList<File> fileList = new ArrayList<>(Arrays.asList(fileArray));
                //？？
                fileList.add(file);
                resultFileViewList = dtoToResultDto(fileList);
                //filter 不会改变原数据
                List<FileViewVo> collect = resultFileViewList.stream().filter(x -> !x.getName().equals(file.getName())).collect(Collectors.toList());
                return collect;
            }
        }else {
            log.info("----netDiskViewInit----" + "存在异常");
        }
        return resultFileViewList;
    }

    /**
     * <pre>
     *  组装side边栏 显示内容
     *
     * </pre>
     * @param userName
     * @param userId
     * @return List<SideDtoVo>
     */
    @Override
    public Map<String,List<SideDtoVo>> getSideDto(String userName, String userId) {
        //todo
        //db返回结果集
        QFileEntity qfileEntity = QFileEntity.fileEntity;
        List<Tuple> collect = queryFactory.select(
                qfileEntity.fileName,
                qfileEntity.fileId,
                qfileEntity.isDelete,
                qfileEntity.isShare
                )
                .from(qfileEntity)
                .where(qfileEntity.userId.eq(Integer.valueOf(userId))).fetch();
        List<SideDtoVo> SideDtoList = collect.stream().map(tuple ->  {
            SideDtoVo sideDtoVo = new SideDtoVo();
            sideDtoVo.setId(tuple.get(qfileEntity.fileId));
            sideDtoVo.setName(tuple.get(qfileEntity.fileName));
            sideDtoVo.setIsDelete(tuple.get(qfileEntity.isDelete));
            sideDtoVo.setIsShare(tuple.get(qfileEntity.isShare));
            return sideDtoVo;
        }).collect(Collectors.toList());
        //编辑我的文件;
        List<SideDtoVo> myFileList = SideDtoList.stream().filter(x -> x.getIsDelete() != 1 && x.getIsShare() != 1).collect(Collectors.toList());
        //编辑我的共享
        List<SideDtoVo> myShareList = SideDtoList.stream().filter(x -> x.getIsShare() == 1 && x.getIsDelete() != 1).collect(Collectors.toList());
        //编辑回收站
        List<SideDtoVo> recycleBinList = SideDtoList.stream().filter(x -> x.getIsDelete() == 1).collect(Collectors.toList());

        Map<String, List<SideDtoVo>> sideDtoResultMap = new HashMap<>();

        sideDtoResultMap.put("我的文件",myFileList);
        sideDtoResultMap.put("我的分享",myShareList);
        sideDtoResultMap.put("我的回收站",recycleBinList);

        return sideDtoResultMap;
    }

    /**
     * <pre>
     *     拿到前端传来的path
     *     去映射本地文件
     *     再去更改数据库
     * </pre>
     *
     * @param path
     * @return int 0:OK 1:NG
     */
    @Override
    @Transactional
    public int delFileByMapping(String path) {

        try {
            File file = new File(path);
            if (file.exists() && file.isFile()){
                //删除本地
                file.delete();
                //update database record;
                //todo
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(file.getName());
                QFileEntity qfileEntity = QFileEntity.fileEntity;
                // 1 : delete ; 0 :exist
                queryFactory.update(qfileEntity).set(qfileEntity.isDelete,1)
                            .where(qfileEntity.fileName.eq(file.getName())).execute();
                return 0;
            }else if (file.exists() && file.isDirectory()){
                //删除所有的子目录 和 文件 同时 更改表记录
                List<File> delObjectList = delFolderReturnObject(file);
                file.delete();
                List<FileDto> collect = delObjectList.stream().map(x -> {
                    FileDto fileDto = new FileDto();
                    fileDto.setFileName(x.getName());
                    return fileDto;
                }).collect(Collectors.toList());

                if (collect == null || collect.size()<1){
                    log.info("数据库查询结果 为 =    " + collect);
                    return 1;
                }
                //批量删除数据库数据
                int i=0;
                try {
                    i = FileMapper.batchDelFileByFileName(collect);
                } catch (Exception e) {
                    log.info("打印批量删除啊===============" +i +'\t'+ e);
                }
                //fileEntityRepository.save()
                return 0;
            }
        } catch (Exception e) {
            log.error("delFileByMapping inner error"+e);
        }
        return 1;
    }

    private List<File> delFolderReturnObject(File file) {
        List<File> returnFileList=new ArrayList<>();
        File[] files = file.listFiles();
        for (File f:files) {
            if (f.exists() && f.isFile()){
                f.delete();
                returnFileList.add(f);
            }else if (f.exists() && f.isDirectory()){
                f.delete();
                returnFileList.addAll(delFolderReturnObject(f));
            }

        }
        return returnFileList;
    }

    /**
     *
     *
     * @return
     */
    @Override
    @Transactional
    public int updateFileShareByFileName(FileViewVo fvv){
        String sourcePath=fvv.getPath();
        String fileName = sourcePath.substring(sourcePath.lastIndexOf(File.separator) + 1);
        //目标文件放到 共享文件夹
        if (fvv.getShare() != null && fvv.getShare() == 1){
            try {
                this.copyFile(sourcePath,"",fileName);
            } catch (Exception e) {
                log.error("updateFileShareByFileName " +  e);
            }
        }else if (fvv.getShare() != null && fvv.getShare() == 0){
            //delete  local file
            File file = new File(fvv.getPath());
            file.exists();
            //file.delete();
        }else{
            log.info("FileViewVo对象  ---入参不标准--- 重新核实--- ");
        }

        QFileEntity qfileEntity = QFileEntity.fileEntity;
        long execute = queryFactory.update(qfileEntity).set(qfileEntity.isShare, fvv.getShare())
                .where(qfileEntity.fileName.eq(fvv.getName())).execute();
        return (int) execute;
    }

    @Override
    @Transactional
    public int deleteByMarkFile(Integer userId, Integer markStatus) {
        int i=0;
        FileEntityExample fileEntityExample = null;
        try {
            fileEntityExample = new FileEntityExample();
            FileEntityExample.Criteria criteria = fileEntityExample.createCriteria();
            criteria.andUserIdEqualTo(userId);
            criteria.andIsDeleteEqualTo(1);
            i = FileMapper.deleteByExample(fileEntityExample);
        } catch (Exception e) {
            log.error("deleteByMarkFile" + e);
        }
        return i;
    }

    @Override
    public List<FileViewVo> getSideViewModify() {

        UserEntity userinfo = userService.getUserInfoMap().get("userinfo");

        File genPath = new File(defaultUpload
                +userinfo.getUser_name()
                +"_"+userinfo.getUser_id().toString()
                + "-VirtaulDisk");
        List<File> fileListShowSideView = modifyIterator(genPath);
        //对象映射
        List<FileViewVo> returnCollect = fileListShowSideView.stream().map(f -> {
            FileViewVo fileViewVo = new FileViewVo();
            fileViewVo.setName(f.getName());
            fileViewVo.setRole(f.isFile() ? 1 : 0);
            fileViewVo.setParentName(subFileName(f.getParent() ));
            fileViewVo.setAbsolutePath(f.getAbsolutePath());
            return fileViewVo;
        }).collect(Collectors.toList());
        return returnCollect;
    }

    @Override
    public List<FileViewVo> setShareFileView() {
        //todo
        QFileEntity qfileEntity = QFileEntity.fileEntity;
        List<Tuple> collect = queryFactory.select(
                        qfileEntity.fileName,
                        qfileEntity.fileId,
                        qfileEntity.isDelete,
                        qfileEntity.isShare,
                        qfileEntity.userId,
                        qfileEntity.filePath
                )
                .from(qfileEntity)
                .where(qfileEntity.isShare.eq(Integer.valueOf(1)),qfileEntity.isDelete.eq(Integer.valueOf(0))).fetch();
        List<FileViewVo> SideDtoList = collect.stream().map(tuple ->  {
            FileViewVo fvv =new FileViewVo();
            fvv.setId(tuple.get(qfileEntity.fileId));
            fvv.setName(tuple.get(qfileEntity.fileName));
            fvv.setShare(tuple.get(qfileEntity.isShare));
            fvv.setIsDelete(tuple.get(qfileEntity.isDelete));
            fvv.setAbsolutePath(tuple.get(qfileEntity.filePath));
            return fvv;
        }).collect(Collectors.toList());

        //获取服务器的 共享文件夹
        File file = new File(share_stroen);
        //构建一个根共享文件夹视图
        FileViewVo shareFolderFileViewVo = new FileViewVo();
        //element-ui构建视图必备
        shareFolderFileViewVo.setId(999);
        shareFolderFileViewVo.setName(file.getName());
        SideDtoList.stream().forEach(x->{
            x.setParentName(shareFolderFileViewVo.getName());
            x.setRole(1);

        });
        TreeDataUtils.addIdToTree(SideDtoList);
        shareFolderFileViewVo.setChildrenList(SideDtoList);

        ArrayList<FileViewVo> returnData = new ArrayList<>(SideDtoList.size() + 5);
        returnData.add(shareFolderFileViewVo);
        return returnData;
    }

    /**
     *
     * @param source
     * @return
     */
    private List dtoToResultDto(@NotNull Object source) {

        //el-table 做树结构视图 gen id 不能 为 0
        Integer id=1;
        List<FileViewVo> resultList=new ArrayList<FileViewVo>();
        List<File> source1 = (List<File>) source;
        //得到根目录
        File file = source1.get(source1.size() - 1);
        FileViewVo fileViewVo_1 = new FileViewVo();
        fileViewVo_1.setId(id);
        fileViewVo_1.setParentName(subFileName(file.getParent()));
        fileViewVo_1.setName(file.getName());
        if (file.isDirectory()){
            setSuffix(String.valueOf(calculateFolderLength(file)),fileViewVo_1);
        }else if (file.isFile()){
            setSuffix(String.valueOf(file.length()),fileViewVo_1);
        }
        fileViewVo_1.setUpload("2022-05-10:10:05");
        fileViewVo_1.setPath(file.getAbsolutePath());
        fileViewVo_1.setRole(file.isFile()?1:0);
        setExtensionName(fileViewVo_1);
        resultList.add(fileViewVo_1);
        try {
            source1.remove(source1.size()-1);
        } catch (Exception e) {
            log.info("说明");
            return null;
        }
        if (source instanceof List){
            for (File f : source1) {
                FileViewVo fileViewVo = new FileViewVo();
                fileViewVo.setId(++id);
                fileViewVo.setParentName(subFileName(f.getParent()));
                fileViewVo.setName(f.getName());
                if (f.isDirectory()){
                    setSuffix(String.valueOf(calculateFolderLength(f)),fileViewVo);
                }else if (f.isFile()){
                    setSuffix(String.valueOf(f.length()),fileViewVo);
                }
                fileViewVo.setUpload(f.isFile()?DateUtils.formatTime(f.lastModified()):"2022-05-10:10:05");
                fileViewVo.setPath(f.getAbsolutePath());
                fileViewVo.setRole(f.isFile()?1:0);
                setExtensionName(fileViewVo);
                resultList.add(fileViewVo);
            }
        }
        //追加操作禁止
        resultList.stream().forEach(x->{
            if(x.getName().contains(DEFAULTGEN)){
                x.setNotAllowOperator(true);
            }

            if(x.getName().equals(DEFAULTUPLOAD)){
                x.setNotAllowOperator(true);
            }

            if(x.getName().equals(DEFAULTSHAREFLODER)){
                x.setNotAllowOperator(true);
            }
        });
        //todo做分享设置
        List<FileViewVo> fileViewVos = setShareByRecord(resultList);
        return fileViewVos;
    }

    //得到shareFolder文件夹的所有文件
    public List<FolderDto> getShareFolder(String parentDir){
        ArrayList<FolderDto> folderDtoList = new ArrayList<>();
        File file = new File(share_stroen);
        ArrayList<File> fileList_1 = IteraFolder(file);
        for (File f: fileList_1) {
            FolderDto folderDto = new FolderDto();
            folderDto.setParent_name(parentDir);
            folderDto.setName(f.getName());
            folderDto.setRole(1);
            folderDto.setAbsolutePath(f.getAbsolutePath());
            folderDtoList.add(folderDto);
        }
        return folderDtoList;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /*
    *
    * */
    public List iteratorFolder(File file){
        for (File f : file.listFiles()) {
            if(f.exists() && f.isFile()){
                fileList.add(f);
            }else if(f.exists() && file.isDirectory()){
                fileList.add(f);
                iteratorFolder(f);
            }
        };
        List<File>  resultFile= new ArrayList<>();
        resultFile.addAll(fileList);
        return resultFile;
    }

    /**
     * <pre>
     *     根据数据库记录
     *     去判断 根据名字对比
     *     追加 分享状态
     * </pre>
     * @param fvList
     */
    @Transactional
    public List<FileViewVo> setShareByRecord(List<FileViewVo> fvList){

        List<FileViewVo> rebuildCollect=null;

        //获取数据库的文件数据
        FileEntity fileEntity = new FileEntity();
        fileEntity.setUserId(userService.getUserInfoMap().get("userinfo").getUser_id());
        QFileEntity qfileEntity = QFileEntity.fileEntity;
        List<Tuple> fetch = queryFactory.select(qfileEntity.fileName, qfileEntity.fileSize, qfileEntity.isShare ,qfileEntity.uploadTime)
                .from(qfileEntity).where(qfileEntity.userId.eq(fileEntity.getUserId())).fetch();
        List<FileViewVo> collect = fetch.stream().map(tuple -> {
            FileViewVo fileViewVo = new FileViewVo();
            fileViewVo.setName(tuple.get(qfileEntity.fileName));
            fileViewVo.setShare(tuple.get(qfileEntity.isShare));
            fileViewVo.setFileSize(tuple.get(qfileEntity.fileSize));
            fileViewVo.setUpload(tuple.get(qfileEntity.uploadTime));
            return fileViewVo;
        }).collect(Collectors.toList());

        //非空判定
        if (collect == null || collect.size()<1){

            return  fvList;
        }
        for (FileViewVo x : collect) {
            rebuildCollect = fvList.stream().map(y -> {
                if (y.getName().equals(x.getName())) {
                    y.setShare(x.getShare());
                    y.setUpload(x.getUpload());
                }
                return y;
            }).collect(Collectors.toList());
        }
        return rebuildCollect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelShare(FileViewVo fvv){
        long execute=0L;
        QFileEntity qfileEntity = QFileEntity.fileEntity;
        try {
             execute = queryFactory.update(qfileEntity).set(qfileEntity.isShare, fvv.getShare())
                    .where(qfileEntity.fileName.eq(fvv.getName())).execute();

        } catch (Exception e) {
            log.error("cancelShare---影响的行数---" + execute);
            //在try catch 异常被捕获就不会回滚  事务就会是失效
            //如果需要手动捕获异常，并且也希望抛异常的时候能回滚
            TransactionAspectSupport.currentTransactionStatus();
        }
        return (int)execute;

    }

    public Map getMapCollection(){
        QFileEntity qfileEntity = QFileEntity.fileEntity;
        Map<String,String > map = queryFactory.select(qfileEntity.fileName,qfileEntity.userId).from(qfileEntity)
                .fetch().stream().collect(
                        Collectors.toMap(
                                tuple->tuple.get(0, String.class),
                                tuple->tuple.get(1, String.class)
                        )
                );
        return map;

    };


    //文件夹的 计算
    //遍历文件夹累加 length
    private <T> Long calculateFolderLength(T folder){
        Long fileSizeTotal=0L;
        if (folder instanceof  File){
            //listFiles 如果目录为空，则数组将为空。如果此抽象路径名不表示目录，或者发生 I/O 错误，则返回 null。故 做非空判定
            File[] files = ((File) folder).listFiles();
            if (files == null || files.length <1){
                return fileSizeTotal;
            }
            for (File f: files) {
                if (f.isFile()){
                    fileSizeTotal += f.length();
                }
                if (f.isDirectory()){
                    fileSizeTotal += calculateFolderLength(f);
                }
            }
            return fileSizeTotal;
        }
        return null;
    }

    private void setExtensionName(FileViewVo fvv){
        String[] extensionArray={"txt","doc","png","jpg","zip","docx"};
        List<String>  extensionList = Arrays.asList(extensionArray);

        String fullName = fvv.getName();
        if (fullName != null && !fullName.equals("''")){
            int indexOf = fullName.lastIndexOf(".");
            String extensionName = fullName.substring(indexOf + 1);
            if (extensionList.contains(extensionName)){
                fvv.setExtensionFlag(1);
            }else  if (fvv.getRole() == 0){
                fvv.setExtensionFlag(0);
            }else {
                fvv.setExtensionFlag(2);
            }
        }
    }


    private void editRedis(){
        //redisTemplate的对象的序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        String s = redisTemplate.opsForValue().get("");
    }



}
