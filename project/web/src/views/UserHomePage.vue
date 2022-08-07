<template>
<el-container style="height: 100%; border: 1px solid #eee">
  <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
    <el-menu :default-openeds="['1', '3']">
      <el-submenu index="1">
        <template slot="title"><i class="el-icon-message"></i>用户管理</template>
        <el-menu-item-group>
          <template slot="title">用户</template>
          <el-menu-item @click="updatePsd()">修改密码</el-menu-item>
           <el-menu-item @click="loginOut()">注销登录</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-submenu index="2">
        <template slot="title"><i class="el-icon-menu" ></i><span @click="showVirtureDisk()">空间操作</span></template>
        <el-menu-item-group>
          <el-menu-item @click="showVirtureDisk()">查询磁盘</el-menu-item>
        </el-menu-item-group>
        <!-- <el-menu-item-group>
          <el-popover
          placement="right"
          width="400"
          trigger="click">
        <span>文件名称:</span><el-input v-model="folderArgs.filename" placeholder="必须要的"></el-input>
        <span>文件路径:</span><el-input v-model="folderArgs.filepath" placeholder="可以不传"></el-input>
        <el-button type="primary" plain @click="createFolder()">提交</el-button>
  <el-menu-item slot="reference">
    创建文件夹
    </el-menu-item>
</el-popover>
        <el-menu-item @click="retrievalFolder()">
            检索文件
          </el-menu-item>
        </el-menu-item-group> -->
      </el-submenu>
      <!-- 做一个管理员可用 -->
      <el-submenu index="3" v-if="this.userdata.role==='1'">
        <template slot="title"><i class="el-icon-setting"></i>管理员可用</template>
        <el-menu-item-group>
          <template slot="title">管理所有用户</template>
          <el-menu-item @click="selectAllUser()">查询用户</el-menu-item>
          <el-menu-item @click="selectAllFile()">查询文件</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </el-aside>
  <!-- 主 -->
  <el-container>
    <el-header style="text-align: right; font-size: 12px">
      <!-- <el-dropdown>
        <i class="el-icon-setting" style="margin-right: 15px"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>查看</el-dropdown-item>
          <el-dropdown-item>新增</el-dropdown-item>
          <el-dropdown-item>删除</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown> -->
      <span>用户名:{{userdata.username}} | UserID:{{userdata.userId}} </span>
      <span v-if="userdata.role === 1">| 管理员 : {{userdata.role}}</span>
    </el-header>
    
  <el-main>
   <el-table
    :data="fileData"
    style="width: 100%">
    <el-table-column
      label="上传时间"
      width="180">
      <template slot-scope="scope">
        <i class="el-icon-time"></i>
        <span style="margin-left: 10px">{{ scope.row.uploadTime}}</span>
      </template>
    </el-table-column>
    <el-table-column
      label="文件名"
      width="180">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <!-- <p>姓名: {{ scope.row.name }}</p>
          <p>住址: {{ scope.row.address }}</p> -->
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium">{{ scope.row.fileName }}</el-tag>
          </div>
        </el-popover>
      </template>
    </el-table-column>

    <el-table-column
      label="文件大小"
      width="180">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <!-- <p>姓名: {{ scope.row.name }}</p>
          <p>住址: {{ scope.row.address }}</p> -->
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium">{{ scope.row.fileSize }}</el-tag>
          </div>
        </el-popover>
      </template>
    </el-table-column>

    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button
          size="mini"
          @click="handleEdit(scope.$index, scope.row)">下载</el-button>
        <el-button
          size="mini"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <router-view/> 
</el-main>
  </el-container>
  
<!-- 文件上传 -->
<div>
 <el-upload
  class="upload"
  drag
  ref="upload"
  action="http://192.168.1.77:8080/common/upload"
  multiple
  :data="this.userdata"
  :file-list="fileList"
  >
  <i class="el-icon-upload"></i>
  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
  <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
</el-upload>
<br>
<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">刷新文件列表</el-button>
</div>
<!-- 容器底部 -->
<!-- <el-footer>
<router-view/>
</el-footer> -->
</el-container>

</template>


<script>
import axios from 'axios'
  export default {
    data() {
      return {
        role_flag:false,
        userdata:{
          username:'',
          userId:'',
          status:0,    
          role:0
        },
        userid:'',
        fileData: [],
         fileList: [],
         folderArgs:{
           filename:'',
           filepath:''
         },
         
      }
      
     
    },
  // Vue实例初始化
    created(){
      this.getUserdataByLogin()
      //this.getFilesDataList()
    },

    methods: {
      // 得到登录用户的信息
      getUserdataByLogin(){
        this.$nextTick(() => {
        // 在此处执行你要执行的函数 
        this.userdata.username = this.$route.query.username,
        this.userdata.userId = this.$route.query.userId,
        this.userdata.role=this.$route.query.role,
        console.log("role====" + this.userdata.role)
        this.userdata.status=this.$route.query.status
        this.getFilesDataList();
      });
      },
      handleEdit(index, row) {
        console.log(index, row);
            axios({
                url:'/common/common/download',
                method: 'get',
                responseType: 'blob',
                params:{
                  fileName:row.fileName,
                  delete:false
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
              eleLink.download = decodeURI(res.headers['download-filename'])
              document.body.appendChild(eleLink)
              eleLink.click()
              window.URL.revokeObjectURL(url)
              this.$message("下载成功")
            }
            )  
      },
      handleDelete(index, row) {
        console.log(index, row);
        this.deleteFileByname(row)

      },
      
      //提交上传
      submitUpload() {
        this.$refs.upload.submit();
        console.log("触发查询")
        this.getFilesDataList()
      },
      //得到页面展示数据
      async getFilesDataList(){
            //发送axios请求
            axios({
                url:'/fileController/getFilesByUserId',
                method: 'get',
                params:{
                  userId:this.userdata.userId
                },
                xhrFields: {
            withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
                if(res.data.code===200){
                 // console.log("success")
                  this.fileData=res.data.data
                  console.log(this.fileData)
                }else{
                    console.log("error"+res.data)
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
      showVirtureDisk(){
          this.$router.push(
            {path:"diskpathview",
            date:new Date().getTime()
            })
          
      },

      createFolder(){                        
        //发送axios请求
            axios({
                url:'/fileController/createFileList',
                method: 'post',
                params:{
                  fileName:this.folderArgs.filename,
                  folderPath:this.folderArgs.filepath
                },
                xhrFields: {
            withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
              console.log(res)
              this.showVirtureDisk()
            }
            ) 

      },

      selectAllUser(){
          this.$router.push("userallview")
      },

     selectAllFile(){
          this.$router.push("fileall")
      },
      //删除文件 byname 
      deleteFileByname(row){
           axios({
                url:'/fileController/delFileByFileName',
                method: 'post',
                params:{
                 fileName:row.fileName
                },
                xhrFields: {
                withCredentials: true                   
                },
            })            
            .then( res=>{
              console.log(res)
              this.getFilesDataList();
            }
            ) 
      }

    }
  }
</script>

<style>

</style>