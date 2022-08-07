<template>
  <el-container style="height: 500px; border: 1px solid #eee">
  <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
    <el-menu :default-openeds="['1', '3']">
      <el-submenu index="1">
        <template slot="title"><i class="el-icon-user-solid"></i>普通用户</template>
        <el-menu-item-group>
          <template slot="title">用户管理</template>
          <el-menu-item index="1-1" @click="getUserFile()">{{userPermission[0]}}</el-menu-item>
          <el-menu-item index="1-2" @click="updatePsd()">{{userPermission[2]}}</el-menu-item>
          <el-menu-item index="1-2">
              <el-upload
              class="upload"
              action="http://192.168.1.77:8080/fileController/handleFileUpload"
              multiple
              :limit="3"
              :file-list="fileList"
              :on-success="uploadSuccess">
              {{userPermission[1]}}
              </el-upload>
          </el-menu-item>
          <el-menu-item index="1-2" @click="loginOut()">{{userPermission[3]}}</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </el-aside>
  
  <el-container>
  <el-header style="text-align: right; font-size: 12px">
       <!--<el-dropdown>
        <i class="el-icon-setting" style="margin-right: 15px"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>查看</el-dropdown-item>
          <el-dropdown-item>新增</el-dropdown-item>
          <el-dropdown-item>删除</el-dropdown-item>
        </el-dropdown-menu> 
      </el-dropdown> -->
      <span>用户名:{{userdata.username}} | UserID:{{userdata.userId}} </span>
      <span v-if="userdata.role === 1">| 管理员 : {{userdata.role}}</span>
      <!-- 面包屑 -->
      <el-breadcrumb>
      <el-breadcrumb-item v-for=" item in narList"  :key="item.index"  >{{item.name}}</el-breadcrumb-item>
</el-breadcrumb>
</el-header>
<el-main>
  <!-- 树结构渲染 -->
<el-input
  placeholder="输入关键字进行过滤"
  v-model="filterText">
</el-input>

<el-tree
  class="filter-tree"
  :data="data"
  show-checkbox
  draggable:true
  :props="defaultProps"
  default-expand-all:false
  :filter-node-method="filterNode"
  @node-contextmenu="nodeContextmenu"
  @node-expand="nodeExpand"
  @node-collapse="nodeCollapse"
  @node-drag-end="handleDragEnd"
  @check="onCheck"
  draggable
  :allow-drop="allowDrop"
  :allow-drag="allowDrag"
  ref="tree">

  <span class="custom-tree-node" slot-scope="{ node, data }">
        <span v-if="data.role===1"  @dbclick="dbClick(data)"><i class="el-icon-document"></i> {{ node.label }}</span>
        <span v-if="data.role===0"  @dbclick="dbClick(data)"><i class="el-icon-folder"></i> {{ node.label }}</span>
        <span>
          <el-button
            v-if="data.role === 0 && data.name !== 'shareFolder'"
            type="text"
            size="medium"
            @click="() => append(data,node)">
            <i class="el-icon-folder-add"></i>
            <span>新建文件夹</span>
          </el-button> 
          <el-button
            v-if="data.name !== 'shareFolder'"
            type="text"
            size="medium"
            @click="() => remove(node, data)">
            <i class="el-icon-folder-delete"></i>
            <span v-if="data.parent_name === 'shareFolder'">取消共享</span>
            <span v-if="data.parent_name !== 'shareFolder'">删除</span>
          </el-button>
         <!--追加一个下载  -->
            <el-button
            v-if="data.role === 1"
            type="text"
            size="medium"
            @click="() => download(data)">
            <i class="el-icon-bottom"></i>
            <span>下载文件</span>
          </el-button>
        </span>
      </span>
</el-tree>
      <router-view/> 
    </el-main>
  </el-container>
  <!-- 共享文件对话框 -->
  <div>
  <el-dialog
  title="提示"
  :visible.sync="centerDialogVisible"
  width="30%"
  center>
  <span>确定共享该文件夹 | 文件 </span>
  <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
  </span>
</el-dialog>
  </div>
</el-container>
</template>

<script>
import axios from 'axios'
let id=1;
  export default {
    data() {
      return {
        filterText: '',
        data: [{
          id: 1,
          label: '一级 1',
          children: [{
            id: 4,
            label: '二级 1-1',
            children: [{
              id: 9,
              label: '三级 1-1-1'
            }, {
              id: 10,
              label: '三级 1-1-2'
            }]
          }]
        }
        ],
        defaultProps: {
          children: 'chlid_list',
          label: 'name'
        },
        narList:[    
        ],
        nardata:{
          name:'',
          parent_name:''
        },
        visible:false,
        newFileList:[],
        copyDragData:{
        sourcePath:'',
        fileName:'',
        targetPath:''
      },
      fileList:[],
      pathObj:{
        content:'',
        next:'',
        pre:''
      },
      userPermission:[],
      userdata:{
          username:'',
          userId:'',
          status:0,    
          role:0
        },
      centerDialogVisible:false,
      shareQuery:{
         name:'',
         absolutePath:''
       },
      }
  
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      },
      visible(value) {
      if (value) {
        document.body.addEventListener('click', this.closeMenu)
      } else {
        document.body.removeEventListener('click', this.closeMenu)
      }
    },
      watchUser(){
       if(this.userdata.username === '' || this.userdata.username === null){
         this.$message('请重新登录')
       }
      }
    },
    created(){
      this.getUserdataByLogin()
      this.getPermission()
    },
    methods: {
      filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
       append(data ,node) {
         console.log("do append")
         console.log("parent")
         console.log(data)
         console.log(node.parent.data)
         console.log(node.parent.data.absolutePath)
         console.log("获取当前节点")
         id++
         const newChild = {
          name:'新建文件夹'+'-'+id, 
          label: "新建文件夹"+'-'+id, 
          chlid_list: [] ,
          role:0 , 
          absolutePath:node.data.absolutePath + '\\'+'新建文件夹'+'-'+id
          };
        if (!data.chlid_list) {
          console.log("set 方法执行中")
          this.$set(data, 'chlid_list', []);
        }
        var name=newChild.name
        var path=newChild.absolutePath
        data.chlid_list.push(newChild);
        this.createFileOrDirc(name,path)
        console.log(data)
        this.narList.splice(0,this.narList.length)
        
      },
       remove(node, data) {
        const parent = node.parent;
        const children = parent.data.chlid_list || parent.data;
        const index = children.findIndex(d => d.name === data.name);
        children.splice(index, 1);
        var fullAbsolutName=data.absolutePath
        this.removefileOrFolder(fullAbsolutName)
        this.narList.splice(0,this.narList.length)
      },
      upload(node,data){
        console(node)
        console(data)
      },
      // 点击事件 回调
      nodeContextmenu(ev,data){
        console.log(ev);
         console.log(data.absolutePath);
         this.visible = true
         this.openFilePath(data)
  
         //  this.openFilePath(data.absolutePath)
      },
      // 目录结构视图
     async initTreeView(){

       await axios({
                url:'/fileController/initCommonUserView',
                method: 'get',
                params:{
                  username:this.userdata.username,
                  userid:this.userdata.userId
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
              .then( res=>{
                if(res.data.code===200){
               this.data = res.data.data
                console.log("面包屑");
              // console.log( this.data[0].name);
              //  this.nardata.name=this.data[0].name
              //  this.nardata.parent_name=this.data[0].parent_name
           
                }
            }
            )  
      },
    //打开文件路径
      openFilePath(data){
              axios({
                url:'/fileController/openPath',
                method: 'post',
                params:{
                  openPath:data.absolutePath,
                  filename:data.name
                },
                xhrFields: {
                withCredentials: true                   
                },
            })              
            .then( res=>{
                console.log(res)
            }
            ) 
      },
      nodeExpand(data){
        if(this.narList.length === 0){
          this.narList.push(data)
        }else if(this.narList.length === 1){
          var obj=this.narList.shift()
          //分同一级 同一个
          if(obj.name === data.name && obj.parent_name===data.parent_name){
            console.log("obj");
            console.log(obj);
          }//同一级 不是同一个
          if(obj.name !== data.name && obj.parent_name===data.parent_name) {
            this.narList.push(data)  
          }
           //子级
          if(obj.name === data.parent_name){
            this.narList.push(obj) 
            this.narList.push(data) 
          }
  
        }else if(this.narList.length > 1){
           obj=this.narList.pop()
          //分同一级 同一个
          if(obj.name === data.name && obj.parent_name===data.parent_name){
            console.log("obj");
            console.log(obj);
          }//同一级 不是同一个
          if(obj.name !== data.name && obj.parent_name===data.parent_name) {
            this.narList.push(data)  
          }
          //子级
          if(obj.name === data.parent_name){
            this.narList.push(obj) 
            this.narList.push(data) 
          }
        }
  
      },
      //
      nodeCollapse(data){
        console.log(data)
        if(this.narList.length>=1){
          console.log(this.narList.length)
          this.narList.pop()
          console.log(this.narList.length)
        }
      },
      handleDragEnd(draggingNode, dropNode, dropType, ev) {
        console.log('tree drag end: ', dropNode && dropNode.label,ev +"handleDragEnd方法" +dropType +dropNode.data.role);
        if(dropNode.data.role=== 0){
          console.log('进入node targetPath==') 
          draggingNode.data.parent_name=dropNode.data.name
          // draggingNode.data.absolutePath=dropNode.data.absolutePath +'\\' + draggingNode.data.name
          this.copyDragData.fileName=draggingNode.data.name
          this.copyDragData.sourcePath=draggingNode.data.absolutePath
          this.copyDragData.targetPath=dropNode.data.absolutePath
          console.log(this.copyDragData)
          this.dragCopy(this.copyDragData)
        }else if(dropNode.data.role=== 1){
          this.$message("node 节点是文件 不可以拖入")
        }
      },
      allowDrop(draggingNode, dropNode, type) {
        if (dropNode.data.label === '二级 3-1') {
          return type !== 'inner';
        } else {
          return true;
        }
      },
      // drag 节点
      allowDrag(draggingNode) {
        if(draggingNode.data.role === 1){
          return true;
        }else{
          return false;
        }
      },
      uploadSuccess(){
        this.initTreeView();
      },

    // axios操作
      //实现拖拉复制
    dragCopy(copyDragData){
             axios({
                url:'/fileController/copyFile',
                method: 'post',
                params:{
                  sourcePath:copyDragData.sourcePath,
                  fileName:copyDragData.fileName,
                  targetPath:copyDragData.targetPath
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
            .then(res=>{
              console.log(res)
            })},

    //点击下载
    download(data){
      console.log(data.name)
       axios({
                url:'/fileController/fileDownLoad',
                method: 'post',
                responseType: 'blob',
                params:{
                  filename:data.name,
                  path:data.absolutePath
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
    dbClick(data){
      this.openFilePath(data)
    },
    createFileOrDirc(name,absolutePath){
           axios({
                url:'/fileController/createFileList',
                method: 'post',
                params:{
                  fileName:name,
                  folderPath:absolutePath
                },
                xhrFields: {
            withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
              console.log(res)
              this.initTreeView()
            }
            ) 
    },
    removefileOrFolder(path){
                 axios({
                url:'/fileController/delFileByPath',
                method: 'post',
                params:{
                  path:path
                },
                xhrFields: {
            withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
              console.log(res)
              this.initTreeView()
            }
            )
    },
        //获取权限
      getPermission(){
              axios({
                url:'/user/getUserPermission',
                method: 'get',
                params:{
                  role:0
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
              .then( res=>{
                if(res.data.code===200){
              console.log(" res.data.data")    
              console.log( res.data)   
               this.userPermission = res.data.data

                }
            }
            )  
      },
      //注销登录
      loginOut(){
                 //发送axios请求
            axios({
                url:'/user/loginOutUser',
                method: 'post',
                params:{
                  userid:this.userdata.userId,
                  username:this.userdata.username
                },
                xhrFields: {
            withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
                console.log(res)
                if(res.data.msg==='1'){
                  this.userdata.status=res.data.msg,
                  this.$router.push("/loginpage")
                  console.log("赋值之后")
                this.$message(res.data.msg);
                }else{
                    console.log('error'+res.data);
                }
            }
            ) 
      },
      updatePsd(){
        this.$router.push({
        path:"/updatepsd",
        query:{
          username:this.userdata.username,
          userId:this.userdata.userId
        }
        }
        )
      },
            // 得到登录用户的信息
      getUserdataByLogin(){
        this.$nextTick(() => {
        // 在此处执行你要执行的函数 
        this.userdata.username = this.$route.query.username,
        this.userdata.userId = this.$route.query.userId,
        this.userdata.role=this.$route.query.role,
        console.log("role====" + this.userdata.role)
        this.userdata.status=this.$route.query.status
        this.initTreeView()
      });
      },
       getUserFile(){
          this.$router.push({
            name:'userFileView',
          query:{
              userId:this.userdata.userId
                    }
          })
      },
       onCheck( currentdata,checkedData){
        console.log("进入onCheck 方法")
         if(currentdata.role === 0){
           this.$message("文件夹不允许共享")
           return
         }
        this.centerDialogVisible = true
        for(var index in checkedData.checkedNodes){
          console.log("item=====")
          console.log(checkedData.checkedNodes[index])
          this.shareQuery.name=checkedData.checkedNodes[index].name
          this.shareQuery.absolutePath=checkedData.checkedNodes[index].absolutePath
        }
         console.log("开始调用centerDialogVisibleByme 方法")
        this.centerDialogVisibleByme(this.shareQuery)
         console.log("调用OK  ===centerDialogVisibleByme 方法")
      },
    // 设置共享
    centerDialogVisibleByme(shareQuery){
       console.log("在 centerDialogVisibleByme 方法 inner ")
       console.log(shareQuery)
               axios({
                url:'/fileController/fileShareByModify',
                method: 'post',
                data:shareQuery,
                contentType: "application/json;charset-utf-8",
                xhrFields: {
                withCredentials: true                   
                },
                
            })
            .then(res=>{
              console.log(res)
              this.initTreeView()
            })
          }


    },
  };


</script>

<style>

 


</style>