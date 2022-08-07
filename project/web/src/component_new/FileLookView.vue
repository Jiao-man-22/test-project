<template>
  <el-table
    ref="singleTable"
    id="dragTable"
    :data="tableData"
    style="width: 100%;
    margin-bottom: 20px;"
    row-key="id"
    :stripe="isTrue"
    :row-class-name="changeRowClass"
    highlight-current-row
    @select="selectCheck"
    @expand-change="expandChange"
    @row-dblclick="dblClick"
    border
    :tree-props="{children: 'childrenList', hasChildren: true}">

    <el-table-column
      type="selection"
      width="55">
    </el-table-column>
  
    <el-table-column
      prop="upload"
      label="上传时间"
      sortable
      width="250">
    </el-table-column>
    <el-table-column
      prop="name"
      label="名称"
      sortable
      width="400">
      <template slot-scope="scope">
      <el-avatar :src="folderAddress" shape="square" :size="medium " v-if="scope.row.extensionFlag === 0"></el-avatar>&nbsp;&nbsp;
       <el-avatar :src="fileImgAddress" shape="square" :size="medium " v-if="scope.row.extensionFlag === 1"></el-avatar>&nbsp;&nbsp;
       <el-avatar :src="undefinedImgAdress" shape="square" :size="medium " v-if="scope.row.extensionFlag === 2"></el-avatar>&nbsp;&nbsp;
      <span>{{scope.row.name}}</span>
      </template>
    </el-table-column>
    <el-table-column
      prop="fileSize"
      label="文件大小">
    </el-table-column>
    <el-table-column
      prop="operationList"
      label="操作">
    <template slot-scope="scope">
    <el-button v-if="scope.row.role === 1" type="info" icon="el-icon-download" circle @click="download(scope.row)"></el-button>
    <el-button type="info" v-if="scope.row.role === 0" icon="el-icon-upload2" circle @click="upload(scope.row)"></el-button>
    <!-- <el-button v-if="scope.row.role === 1" type="warning" icon="el-icon-share" circle></el-button> -->
    <el-button  :disabled="scope.row.notAllowOperator" type="danger" icon="el-icon-delete" circle @click="sendAxiosRemovefileOrFolder(scope.row.path)"></el-button>
      </template> 
    </el-table-column>
    <!-- 共享按钮 -->
    <el-table-column
    label="共享按钮">
    <template slot-scope="scope">
         <el-switch
              :disabled="scope.row.role===0?true:false"
              v-model="scope.row.share"
              :active-value="1"
              :nactive-value="0"
              active-color="#02538C"
              inactive-color="#B9B9B9"
              @change="changeSwitch(scope.row)"/>
    </template>
    </el-table-column>
<!-- 做文件上传 -->
<el-upload
  class="upload-demo"
  action=""
  multiple
  :http-request="httpRequest"
  :before-upload="beforeUpload"
  :on-change="onChange"
  :on-progress="onProgress"
  :on-success="onSuccess"
  :on-error="onError"
  ref="myload">
  <el-button size="small" type="primary"><i class="el-icon-upload el-icon--right" id="uploadBbutton"></i>点击上传</el-button>
</el-upload>     
  </el-table>

  

</template>

<script src="https://cdn.jsdelivr.net/npm/sortablejs@latest/Sortable.min.js"></script> 
<script>
//导入axios
import axios from 'axios'
import folderImg from '../assets/folder.png'
import fileImg from '../assets/file.png'
import undefinedImg from '../assets/undefined.png'

import Sortable from 'sortablejs'



export default {
    // data函数 数据是返回值的形式给出的  无法修改 
     data() {
      return {
        tableData: [{
            "id": 1,
            "pid": null,
            "name": "ww_3-VirtaulDisk",
            "parentName": "VirtaulDisk",
            "upload": "2022-05-23:10:05",
            "fileSize": "4KB",
            "operatorList": null,
            "path": "E:\\VirtaulDisk\\ww_3-VirtaulDisk",
            "userId": null,
            "role": 0,
            "share": null,
            "childrenList":[
                    {
                    "id": 2,
                    "pid": 0,
                    "name": "defaultUpload",
                    "parentName": "ww_3-VirtaulDisk",
                    "upload": "2022-05-23:10:05",
                    "fileSize": "4KB",
                    "operatorList": null,
                    "path": "E:\\VirtaulDisk\\ww_3-VirtaulDisk\\defaultUpload",
                    "userId": null,
                    "role": 0,
                    "share": null,
                    "childrenList":[
                    ]
                    },
            ]
        },
        {
            "id": 3,
            "pid": null,
            "name": "defaultUpload",
            "parentName": "ww_3-VirtaulDisk",
            "upload": "2022-05-23:10:05",
            "fileSize": "4KB",
            "operatorList": null,
            "path": "E:\\VirtaulDisk\\ww_3-VirtaulDisk\\defaultUpload",
            "userId": null,
            "role": 0,
            "share": null,
            "childrenList":[]
        }
      ],
        fileList:[],
        isTrue:true,
        isExpand:false,
        expandRows:[],
        selectList:[],
        //共享Flag
        shareFlag:0,
        folderAddress:folderImg,
        fileImgAddress:fileImg,
        undefinedImgAdress:undefinedImg,
        medium:'medium',
        //要上传的额外对象
        fileViewVo:{
          path:'123456//'
        },
        //做拖拉功能 
        sortable : {},//做拖拉对象 
        isShowTable: true,
        isEditOrder: false,
        sortData: [] // 拖拽数据
        
      }
    },
    created(){
      //初始化视图
      this.initView()
    },
    mounted () {
      //  this.rowDrop()
     },
    // 方法
    methods:{
      //初始话
      initView(){
        axios({
           url:'/fileController/initFileViewModify',
                method: 'get',
                params:{
                  username:this.$store.state.userdata.userName,
                  userid:this.$store.state.userdata.userId,
                  flag:1
                },
        }).then(res =>{
          if(res.data.code === 200){
            this.tableData=res.data.data
            console.log( this.tableData)
            this.$nextTick(()=>{
              //this.rowDrop
            })
          }
        }
          
        )
      },
      //
      httpRequest(fileRequestParam){
      console.log("自定义上传"+fileRequestParam)
      let fd = new FormData()
      fd.append('file', fileRequestParam.file)// 传文件
      fd.append('path', this.fileViewVo.path)
      this.sendUploadAxios(fd)
      },
      //触发upload
      upload(row){
        console.log("触发upload"+row)
         this.$refs['myload'].clearFiles()
        this.selectList=[];
        this.$refs['myload'].$refs['upload-inner'].handleClick()
        this.fileViewVo=row
        this.$refs['myload'].submit()
        // 使用form表单的数据格式
        // row.share=0
        // let data={
        //   file:this.fileList[0],
        //   }
        // this.sendUploadAxios(data)    
     },
     //文件上传时的钩子 function(event, file, fileList)	
     onProgress(event , file ,fileList){
       console.log("文件上传时的钩子")
       console.log(event , file ,fileList)
     },
    //上传之前的钩子 
     beforeUpload(file){
       console.log("上传之前的钩子 ")
       console.log(file)
     },
    //文件状态改变时触发
     onChange(file,fileList){
        console.log("文件状态改变时触发 ")
       console.log(file,fileList)
     },
     //文件上传成功时的钩子	function(response, file, fileList)
     onSuccess(response, file, fileList){
        console.log("文件上传成功时的钩子 ")
       console.log(response,file,fileList)
       document.getElementById("")
       this.$refs['myload'].clearFiles()
     },
    //文件上传失败时的钩子	function(err, file, fileList)
    onError(err, file, fileList){
        console.log("文件上传失败时的钩子 ")
       console.log(err,file,fileList)
    },
     
    //点击下载
    download(data){
      console.log(data.name)
      this.sendDownloadFileAxios(data)
    },
    //选中当前行
    setCurrent(row) {
        this.$refs.singleTable.setCurrentRow(row);
      },
    //处理当前行
    handleCurrentChange(val) {
        this.currentRow = val;
      },
    //
    //勾选check框时触发
    selectCheck(selection, row){
      console.log("手动勾选时触发")
      console.log(selection,row)
      if(row.role === 1){
        this.$message("文件节点不允许创建文件夹 上传点击右侧")
         this.$refs.singleTable.clearSelection();
      }

      //勾选时 放到 暂存区
      //只能放一个
      if(selection.length === 1 && this.selectList == 0 ){
        this.selectList=selection
      }else if(selection.length > 1){
         this.$message("不允许多个勾选 请重新勾选")
         this.$refs.singleTable.clearSelection();}
       else if(selection.length === 1 && this.selectList != 0 ){
        this.$message("请重新勾选")
        selection = []
        this.$refs.singleTable.clearSelection();
        this.selectList = []
      }
    },
    //做新建文件夹
    async createFolder(){
      console.log(this.selectList);
      if(this.selectList.length === 0){
        this.$message("请选择你要生成文件夹的目录")
      }else if(this.selectList.length === 1){
        console.log(this.selectList[0])
        let data=this.selectList.shift()
        this.sendAxiosCreateFolder(data.name,data.path)
        await this.$message("创建成功")
        this.$refs.singleTable.clearSelection();
      }else{
        this.$message("请重新勾选")
        this.selectList = []
        this.$refs.singleTable.clearSelection();
      }
    },
    //创建文件夹axios
    sendAxiosCreateFolder(fileName,filePath){
        axios({
          url:'/fileController/createFileList',
          method: 'post',
          params:{
            fileName:fileName,
            folderPath:filePath,
            flag:'1'
            },
            xhrFields: {
            withCredentials: true                   
                },
            })
        .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
              console.log(res)
              this.initView()
              this.$emit('getSideData')
              
            }
            ) 
    },
    //删除文件axios    
    sendAxiosRemovefileOrFolder(path){
        axios({
                url:'/fileController/delFileByPath',
                method: 'post',
                params:{
                  path:path,
                  flag:'1'
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
              console.log(res)
              this.$emit('getSideData')
              this.initView()
            }
            )
            
    },
    //下载文件axios
    sendDownloadFileAxios(data){
      console.log(data.name)
       axios({
                url:'/fileController/fileDownLoad',
                method: 'post',
                responseType: 'blob',
                params:{
                  filename:data.name,
                  path:data.path
                },
                xhrFields: {
            withCredentials: true                   
                },
            })            
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
              console.log(res);
              let eleLink = document.createElement("a")
              let blob = res.data
              let url = window.URL.createObjectURL(blob)
              eleLink.href = url
              //规避 转义字符
              console.log(decodeURI(res.headers['download-filename']));
              eleLink.download = decodeURI(res.headers['download-filename'])
              document.body.appendChild(eleLink)
              eleLink.click()
              window.URL.revokeObjectURL(url)
              this.$message("下载成功")
            }
            )  
    },
    //共享文件
    changeSwitch(data){
      console.log("into changeSwitch")
      console.log(data)
      this.shareFileDialog(data)
    },
    //共享文件对话框
    shareFileDialog(data){
      if(data.share === 0 || data.share === false ){
         this.$confirm('此操作将取消该文件共享, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            console.log('分享对话框 then块')
            //更新数据库 修改share状态 并且回显
            data.share = 0
            this.setFileShare(data);
            console.log('以改变数值')
            console.log(data)
            this.$message({
            type: 'success',
            message: '取消该文件共享成功!'
          });
        }).catch(()=>{
          console.log("进入catch")
          this.initView()
          this.$message({
            type: 'info',
            message: '已取消此操作'
          });  
        })
      }else if(data.share === 1){
         this.$confirm('此操作将共享该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //业务代码
          console.log("打印共享对象");
          this.setFileShare(data);
          console.log(data);
          this.$message({
            type: 'success',
            message: '设置共享成功!'
          });
        }).catch(() => {
          console.log("进入catch")
          this.initView()
          this.$message({
            type: 'info',
            message: '已取消此操作'
          });          
        });
      }
    },

    //设置文件共享
    setFileShare(data){
      let fvv = data
           axios({
                url:'/fileController/setShareFile',
                method: 'post',
                data:fvv,
                contentType: "application/json;charset-utf-8",
                xhrFields: {
                withCredentials: true                   
                },
                
            })
            .then(res=>{
              console.log(res)
              this.initView()
              //在父组件中要配置监听
              //this.$emit('getSideData')
              //this.$parent.getSideData
            })
    },
    //展开时触发
    //设置时的路径 用于导航栏（d调用父方法 传递参数）
    expandChange(row,expanded){
      console.log('展开时触发')
      console.log(row,expanded)
      this.$emit('navigationbar',row,expanded);
    },
    //自定义upload
    async sendUploadAxios(fd){
      console.log("自定义upload")
      console.log(fd.get('file'))
      console.log(fd.get('path'))
            axios({
                url:'/fileController/handleFileUpload',
                method: 'post',
                headers: {
                credentials: 'same-origin'
                // 'Content-Type': 'multipart/form-data',不用写这个，写了报错
                },
                data:fd,//不为data为body否则报错
                contentType: "application/json;charset-utf-8",
                xhrFields: {
                withCredentials: true                   
                },   
            })
            .then(res=>{
              if(res.data.msg ==="repeat"){
                this.$message("文件重复提交,请重新选择")
              }
              //刷新视图
              this.initView()
              //刷新父组件
              this.$emit('getSideData')
              console.log(res)
            })
    },

    //重写一个渲染table
    initViewByModify(path){
      console.log('重新切换table 会切换 table 类型 从 tree table -> 欧通')
        axios({
                url:'/fileController/initFileViewModify',
                method: 'get',
                params:{
                  username:this.$store.state.userdata.userName,
                  userid:this.$store.state.userdata.userId,
                  flag:1,
                  absolutPath:path
                },
        }).then(res =>{
          if(res.data.code === 200){
            this.tableData=res.data.data
            console.log( this.tableData)
            this.$nextTick(()=>{
              this.rowDrop()
            })
          }
        })
    },

  //绑定一个双击事件 调用父组件方法
  dblClick(row, column, event){
      console.log('绑定一个双击事件')
      console.log(row, column, event)
      this.$emit('navigationbar',row,true);
      this.initViewByModify(row.path)
    },
    
// 完成前端拖拉动画
      // 行拖拽
  rowDrop () {
    console.log("行拖拽启动")
    const tbody = document.querySelector('.el-table__body-wrapper tbody')
    let that=this
 
 //创建 sortable实例 
    Sortable.create(tbody, 
    //配置项
    {
    group:{ name: "goodNiceNB", pull: 'clone', put: true },
    direction: 'vertical',
    sort: true ,//禁止排序
    //forceFallback: true,
    //draggable:'.allowDrag',
    setData:function(transfer){
    transfer.setData("Text",'')
 },
 // 元素被选中
  onChoose: function (/**Event*/evt) {
    console.log(evt,'onChoose方法')

    },
    
  // 开始拖拽的时候
  onStart: function (/**Event*/evt) {
    console.log(evt,'onStart')
      // element index within parent
    },  

  onChange: function(/**Event*/evt) {
     console.log(evt.newIndex,'onChange---------------------'); 
  },

  onEnd: evt=>{
      //获取拖拉的行对象
      //console.log(that.tableData.splice(evt.oldIndex,1))
      //let a =that.tableData.splice(evt.oldIndex,1);
        //console.log(evt)
        //console.log(that.tableData)
        debugger
        //console.log(that.tableData.splice(evt.oldIndex,1),'1111111')
        let sourceRow=this.tableData.splice(evt.oldIndex, 1)[0]
        //const targetRow = that.tableData.splice(evt.oldIndex,1)[0];
        console.log("END 之中打印",evt)
        //console.log(that)
        //console.log(targetRow)
        //that.tableData.splice(evt.newIndex,0,targetRow)
        let targetRow = that.tableData[evt.newIndex-1];
        //console.log('source =' ,sourceRow , 'target' , targetRow)
        if(targetRow.role === 0){
          //that.tableData.splice(evt.oldIndex,0,sourceRow)
          this.$nextTick(()=>{
          this.sendDragTableAxios(sourceRow,targetRow)
        }) 
        }else if(targetRow.role === 1){
          that.tableData.splice(evt.oldIndex,0,sourceRow)
          this.$message('文件不可以拖入')
        }

  },
        })
      },

  sendDragTableAxios(sourceRow ,targetRow){
    console.log(sourceRow ,targetRow)
    sourceRow.share = sourceRow.share === false?1:0;
    targetRow.share = targetRow.share === false?1:0
    let arrayList=[]
    arrayList.unshift(sourceRow)
    arrayList.push(targetRow)

                axios({
                url:'/fileController/dragFile',
                method: 'post',
                headers: {
                credentials: 'same-origin'
                // 'Content-Type': 'multipart/form-data',不用写这个，写了报错
                },
                data:arrayList,//不为data为body否则报错
                contentType: "application/json;charset-utf-8",
                xhrFields: {
                withCredentials: true                   
                },   
            })
            .then(res =>{

            })
  },

  //设置是否拖拉
  changeRowClass(data){
    if(data.row.role === 1){
      return 'allowDrag';
    }else{
      return 'notAllowDrag';
    }
  }

    },

    

}
</script>

<style>

</style>