package com.jiao.testproject.testproject.services;
import com.jiao.testproject.testproject.dto.FileDto;
import com.jiao.testproject.testproject.dto.FileViewVo;
import com.jiao.testproject.testproject.dto.FolderDto;
import com.jiao.testproject.testproject.dto.SideDtoVo;
import com.jiao.testproject.testproject.entity.FileEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/*操作文件表*/
public interface IFileService {

    /* 上传文件 插入一条数据*/
    int insertFileById(FileDto fileDto);

    /*检索文件*/
    List<FileDto> selectFiles();

    List<FileEntity> selectFileByUserId(Integer uesrId);

    List<FileEntity> selectFileByFileName(String fileName);

    /*删除文件*/
    int deleteFile(Integer fileId);

    /*根据用户ID删除*/
    int deleteFileByUserId(Integer userid);

    /*根据file_id删除*/
    int delFileByFileId(Integer fileid);

    /*删除file By name */
    int delFileByFileName(String filename);

    /*创建文件夹*/
    List<String> createFolders(String path,String filename,@Nullable String flag);

    /*检索文件*/
    File retrievalFileName(String fileName, String path);

    /*文件的复制*/
    void copyFile(String sourcePath,String filename,String target) throws IOException;

    /*
    *设置文件共享
    * 防止多线程操作error
    *
    * @prama  File 文件类型
    * void    无返回值*/
    List<FolderDto> fileStructrue();

    /*
     *文件共享
     * @param MultipartFile fie
     * */
    String fileShare(String fileName, String absolutPath);

    //删除服务器文件
    boolean delFileByPath(String path);

    //重写磁盘初始化方法
    List<FolderDto>initUserVirtualDisk(String username,String userid);

    //处理文件上传
    void handleFileUpload(MultipartFile file,@Nullable String specificPath);

    //得到sharefolder的集合
    List<FolderDto> getShareFolder(String parentDir);

    List<FolderDto>appendFolder(List<FolderDto> listA , @Nullable List<FolderDto> listB);

    //重写 返回视图
    List<FileViewVo>netDiskViewInit(String userName,String userId , @Nullable String absolutePath);

    Map<String,List<SideDtoVo>> getSideDto(String userName, String userId);

    //删除 重新修改

    /**
     * <pre>
     *     拿到前端传来的path
     *     去映射本地文件
     *     再去更改数据库
     * </pre>
     *
     * @param path
     * @return int 做删除flag
     */
    int delFileByMapping(String path);

    /**
     *
     * @param fvv
     * @return 更新成功flag 0 : OK  1 : NG
     */
    int updateFileShareByFileName(FileViewVo fvv);

    /**
     * <pre>
     *     删除 字段 delete  == 1 的
     * </pre>
     * @param userId
     * @param markStatus
     * @return
     */
    int deleteByMarkFile(Integer userId, Integer markStatus);

    List<FileViewVo> getSideViewModify();

    /**
     * <pre>
     *     设置一个目录专门方法 共享文件
     * </pre>
     * @return List<FileViewVo> 一个共享文件的集合
     */
    List<FileViewVo> setShareFileView();

    int cancelShare(FileViewVo fvv);

    void copyFile(File source, File target) throws IOException;

    /**
     * 将结果打成map
     * @return map
     */
    Map getMapCollection();

}
