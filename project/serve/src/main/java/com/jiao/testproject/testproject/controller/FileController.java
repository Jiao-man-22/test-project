package com.jiao.testproject.testproject.controller;
import com.jiao.testproject.testproject.dto.*;
import com.jiao.testproject.testproject.entity.FileEntity;
import com.jiao.testproject.testproject.services.IFileService;
import com.jiao.testproject.testproject.utils.FileUtils;
import com.jiao.testproject.testproject.utils.StringUtils;
import com.jiao.testproject.testproject.utils.TreeDataUtils;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/fileController")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    IFileService fileService;

    @GetMapping("/getFilesByUserId")
    public AjaxResult getFilesbyUserid(@RequestParam("userId") Integer userId){
        List<FileEntity> fileEntities = fileService.selectFileByUserId(userId);
        try {
            if (fileEntities ==null || fileEntities.size() < 1){
                return AjaxResult.error("文件数据为 null");
            }else{
                for (FileEntity fe:fileEntities) {
                    int indexOf = fe.getFileName().lastIndexOf("/");
                    int time_indexOf = fe.getUploadTime().lastIndexOf(":");
                    String substring = StringUtils.substring(fe.getFileName(), indexOf + 1);
                    String substring1 = StringUtils.substring(fe.getUploadTime(), 0, time_indexOf - 1);
                    fe.setFileName(substring);
                    fe.setUploadTime(substring1);
                }
            }
        }catch (Exception e){
            log.error("C 层 getFilesbyUserid  有 error");
        }
        return AjaxResult.success("OK",fileEntities);
}
    @PostMapping("/delFileByFileName")
    public AjaxResult delFileByName (@RequestParam("fileName") String fileName) {
        if (fileName ==null || fileName.equals("''") || fileName.equals("")){
            return AjaxResult.error("入参错误");
        }
        int i = fileService.delFileByFileName(fileName);
        if (i>0){
            return AjaxResult.success();
        }
        return AjaxResult.error("delete error");
    }
    /*create 文件夹*/
    @PostMapping("/createFileList")
    public AjaxResult createFolerList(@RequestParam(value = "folderPath",required = false)String folderPath
            , @RequestParam(value = "fileName",required = false)String fileName,@RequestParam(value = "flag",required = false) String flag){
        List<FolderDto> folderDtos=null;
        try{
            fileService.createFolders(folderPath,fileName,flag);
            folderDtos = fileService.fileStructrue();
            return AjaxResult.success("OK",folderDtos);
        }catch (Exception e){
            log.error("创建文件 "+ e);
        }
    return AjaxResult.error();
    }

    /*检索文件*/
    @GetMapping("retrievalFileName")
    public AjaxResult retrievalFileName(String fileName, @RequestParam(value = "path" ,required=false) String path){
        if (paramIsEmpty(fileName)){
            return AjaxResult.error("fileName 不能为null");
        }
        File file = fileService.retrievalFileName(fileName, "");
        if (file == null){
            return AjaxResult.error("NG","file 文件 不存在 ");
        }
        return AjaxResult.success("OK",file);
    }

    /*copyfile */
    @PostMapping("/copyFile")
    public AjaxResult copyFile(@RequestParam("sourcePath") String sourcePath
            ,@RequestParam("fileName") String filename
            ,@RequestParam("targetPath") String targetPath){
        if (paramIsEmpty(sourcePath) || paramIsEmpty(filename) || paramIsEmpty((targetPath))){
            return AjaxResult.error("入参错误");
        }
        try {
            fileService.copyFile(sourcePath,filename,targetPath);
        } catch (IOException e) {
           log.error("copyFile " +e);
        }
        return AjaxResult.success();
    }
    /*设置文件共享区*/
    @PostMapping("/fileShare")
    public AjaxResult fileShare(@RequestBody List<FolderDto> queryArray) {
        if (queryArray == null || queryArray.size()<1){
            return AjaxResult.error("数组为 null ");
        }
        ArrayList<String> pathList = new ArrayList<>();
        try{
            for (FolderDto fd:queryArray) {
                String fileName = fd.getName();
                String absolutePath = fd.getAbsolutePath();
                if (!paramIsEmpty(fileName) && !paramIsEmpty(absolutePath)){
                    String s = fileService.fileShare(fileName, absolutePath);
                    pathList.add(s);
                }
            }
        }catch (Exception e){
            log.info("设置文件共享区"+e);
        }
        return AjaxResult.success("OK",pathList);
    }
    /*设置文件共享区*/
    @PostMapping("/fileShareByModify")
    public AjaxResult fileShareByModify(@RequestBody FolderDto shareQuery) {
        if (shareQuery == null){
            return AjaxResult.error("shareQuery 为 null");
        }
        try{
            String name = shareQuery.getName();
            String absolutePath = shareQuery.getAbsolutePath();
            fileService.fileShare(name,absolutePath);
        }catch (Exception e){
            log.info("设置文件共享区"+e);
        }
        return AjaxResult.success("OK");
    }
    /*返回文件目录结构 */
    @GetMapping("/flieDiskStructrue")
    public AjaxResult flieDiskStructrue(@RequestParam(required = false) String userName ,@RequestParam(required = false)String userId){
        List<FolderDto> folderDtos=null;
        //根传入的参数判断是否是admin
        if ( userName== null || userName.equals("''") || userId == null || userId.equals("''") ){
            folderDtos= fileService.fileStructrue();
        }else {
            folderDtos = fileService.initUserVirtualDisk(userName, userId);
        }
        List<FolderDto> treeDataByIterator = TreeDataUtils.getTreeDataByIterator(folderDtos);
        return AjaxResult.success("200",treeDataByIterator);
    }

    /*查询所有文件*/
    @GetMapping("/getAllFilesByAdmin")
    public AjaxResult getAllFilesByAdmin(@RequestParam(required = false) String role){

            List<FileDto> fileDtos = fileService.selectFiles();
            if (fileDtos == null || fileDtos.size() < 1) {
                return AjaxResult.success("集合为空", fileDtos);
            }
            return AjaxResult.success("OK", fileDtos);
        }
    /*根据id 删除*/
    @PostMapping("/deleteFileById")
    public AjaxResult deleteFileById(String fileId){
        int i=0;
        if (fileId == null || fileId.equals("")  || fileId.equals("''")){
            return AjaxResult.error("fileId 未输入");
        }
        try{
             i = fileService.delFileByFileId(Integer.valueOf(fileId));
        }catch (ClassCastException e){
            log.error("对象 转换类型冲突" + e);
        }
       return AjaxResult.success("OK",i);
    }
    /**/
    @PostMapping("/fileDownLoad")
    public AjaxResult fileDownLoad(String filename, String path, HttpServletResponse response){

        if (filename == null || filename.equals("")  || filename.equals("''") ||path == null || path.equals("") || path.equals("''")){
           log.info("入参有误  "+filename+"---"+path);
        }
        try{
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, filename);
            //文件下载
            if (path.contains(filename)){
                FileUtils.writeBytes(path,response.getOutputStream());
            }else{
                String fullName=path+File.separator+filename;
                FileUtils.writeBytes(fullName,response.getOutputStream());
            }
        }catch (IOException e){
           log.error("io " +e);
        }
        return null;
    }
    @PostMapping("/openPath")
    public void openPath(String openPath,@RequestParam(required=false)String filename) {
        try {
            File file = new File(openPath);
            try {
                Desktop.getDesktop().open(file); // 启动已在本机桌面上注册的关联应用程序，打开文件对象file。
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) { // 异常处理
            System.err.println(e);
        }
    }

    @PostMapping("/delFileByPath")
    public AjaxResult delFileByPath(String path ,@RequestParam(required = false) String flag){
        if (paramIsEmpty(path)){
            return AjaxResult.error("path 不能为 null");
        }
        if (flag != null){
            int i = fileService.delFileByMapping(path);
            return i==0?AjaxResult.success("OK"):AjaxResult.error("NG");
        }
        boolean b = fileService.delFileByPath(path);
        if (b){
            return AjaxResult.success("Ok",b);
        }
        return AjaxResult.error("NG",b);
    }

    @GetMapping("/initCommonUserView")
    public AjaxResult initCommonUserView(String username,String userid){
        if (paramIsEmpty(username) || paramIsEmpty(userid)){
            return AjaxResult.error("username && userid 都不能为 空");
        }
        List<FolderDto> folderDto_1 = fileService.initUserVirtualDisk(username, userid);
        List<FolderDto> shareFolder = fileService.appendFolder(folderDto_1, new ArrayList<>());
        List<FolderDto> treeDataByIterator = TreeDataUtils.getTreeDataByIterator(shareFolder);
        return AjaxResult.success("登录成功", treeDataByIterator);
    }

    //重新封装页面视图dto
    @GetMapping("/initFileViewModify")
    public AjaxResult initFileViewModify(String username,String userid,String flag,@RequestParam(required = false) String absolutPath){
        if (paramIsEmpty(username) || paramIsEmpty(userid) || paramIsEmpty(flag)){
            return AjaxResult.error("username && userid 都不能为 空");
        }
        List<FileViewVo> fileViewVos;
        List<FileViewVo> treeDataByIterator=null;
        if (absolutPath == null || absolutPath.equals("''")){
            fileViewVos = fileService.netDiskViewInit(username, userid, absolutPath);
            treeDataByIterator = TreeDataUtils.getTreeDataByIterator(fileViewVos);
        }
        else if (absolutPath!=null && !absolutPath.equals("''") ){
            fileViewVos = fileService.netDiskViewInit(username, userid, absolutPath);
           return AjaxResult.success("查询一层 成功",fileViewVos);
        }

        return AjaxResult.success("登录成功", treeDataByIterator);
    }


    @PostMapping("/handleFileUpload")
    public AjaxResult handleFileUpload(MultipartFile file,@RequestParam(value = "path", required = false) String  path){
        if (file == null){
            return AjaxResult.error("file 不能 为 空");
        }
        //判断文件是否存在
        File fileIsRepeatFlag = fileService.retrievalFileName(file.getOriginalFilename(), "");
        if (fileIsRepeatFlag !=null && fileIsRepeatFlag.exists() ){
            return AjaxResult.success("repeat");
        }
        String specificPath=path;
        try{
            fileService.handleFileUpload(file,specificPath);
        }catch (Exception e){
            log.error("fileService 调用 :" + e);
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @PostMapping("/handleFileUploadWithParma")
    public AjaxResult handleFileUploadWithParma(@RequestBody FileWithParam fileWithParam){
        MultipartFile file = fileWithParam.getFile();
        FileViewVo fileViewVo = fileWithParam.getFileViewVo();
        if (file == null){
            return AjaxResult.error("file 不能 为 空");
        }
        String specificPath=fileViewVo.getPath();
        try{

            fileService.handleFileUpload(file,specificPath);
        }catch (Exception e){
            log.error("fileService 调用 :" + e);
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @GetMapping("/getSideViewVo")
    public AjaxResult getSideViewVo(String userName,String userId){
        //todo 非空判定
        Map<String, List<SideDtoVo>> sideDto = fileService.getSideDto(userName, userId);
        List<SideDtoVo> returnSideData = TreeDataUtils.mapToTreeData(sideDto);
        return AjaxResult.success("OK ",returnSideData);
    }

    @PostMapping("/setShareFile")
    public AjaxResult setShareFile(@RequestBody FileViewVo fvv){
        if (fvv == null){
            return AjaxResult.error();
        }
        int i = fileService.updateFileShareByFileName(fvv);
        return AjaxResult.success("OK",i);
    }

    @PostMapping("deletedFileByUserId")
    public AjaxResult deletedFileByUserId(String userId){
        int i = fileService.deleteByMarkFile(Integer.valueOf(userId), 1);
        if(i==0){
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }
    @GetMapping("getSideViewModify")
    public AjaxResult getSideViewModify(){
        List<FileViewVo> sideViewModify = fileService.getSideViewModify();
        TreeDataUtils.addIdToTree(sideViewModify);
        List<FileViewVo> treeDataByIterator = TreeDataUtils.getTreeDataByIterator(sideViewModify);
        return AjaxResult.success(treeDataByIterator);
    }
    @GetMapping("getShareFileView")
    public AjaxResult getShareFileView(){
        List<FileViewVo> toWebData = fileService.setShareFileView();
        return AjaxResult.success(toWebData);
    }

    @PostMapping("/cancelShareFile")
    public AjaxResult cancelShareFile(@RequestBody FileViewVo fvv){
        fvv.setShare(Integer.valueOf(0));
        int i = fileService.cancelShare(fvv);
        if (i == 0){
            return AjaxResult.error("更新失败 受影响行数",i);
        }
        return AjaxResult.success("更新成功 受影响行数");
    }

    /**
     * <pre>
     *     将文件移到共享目录
     *
     * </pre>
     * @param
     * @return
     */

    @PostMapping("/dragFile")
    public AjaxResult dragFile(@RequestBody List<FileViewVo> arrayList){
        arrayList.get(0);
        Map<String, FileViewVo> paramHashMap = new HashMap<>();
        for (int i = 0; i <arrayList.size() ; i++) {
            paramHashMap.put("param_"+i,arrayList.get(i));
        }

        FileViewVo fileViewVo = paramHashMap.get("param_0");
        FileViewVo fileViewVo_2 = paramHashMap.get("param_1");
        //判断是否为 0: source 或者 1 :target
        File  source=null;
        File  output=null;
        Optional<FileViewVo> first = arrayList.stream().filter(x -> x.getRole() > 0).findFirst();
        FileViewVo fileViewVo1 = first.get();
        source = new File(fileViewVo1.getPath());
        Optional<FileViewVo> first1 = arrayList.stream().filter(x -> x.getRole() == 0).findFirst();
        output = new File(first1.get().getPath(),source.getName());

        try {
            fileService.copyFile(source,output);
        } catch (IOException e) {
           log.error("copyFile function error" + e );
        }
        return AjaxResult.success();
    }

    public AjaxResult returnMapToWeb(){
        Map mapCollection = fileService.getMapCollection();
        return AjaxResult.success(mapCollection);
    }


    public boolean paramIsEmpty(String param){
        if (param== null || param.equals("''") || param.equals("")){
            return true;
        }
        return false;
    }












}

