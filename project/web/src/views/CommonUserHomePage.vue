<template>
<el-container>
  <!--头部 -->
  <el-header direction="horizontal ">
    <!-- 用户信息 -->
    <el-row class="avatar">
    <el-col :span="12">
        <div class="block_avatar" >
          <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar></div>
    </el-col>
    <div class="sub-title">
     <span>用户名:{{this.userdata.userName}} | UserID:{{this.userdata.userId}} </span>
      <span v-if="userdata.role === 1">| 管理员 : {{userdata.role}}</span>
    </div>  
    </el-row>
  </el-header>

  <el-container>
    <!--侧边栏  -->
<el-aside width="300px" class="sideContainer">
<!-- 菜单控件 -->
<el-input class="filter_input"
  placeholder="输入关键字进行过滤"
  v-model="filterText">
</el-input>
<el-tree
  :data="sideData"
  :props="defaultProps"
  node-key="id"
  class="filter-tree"
  default-expand-all
  @node-drag-start="handleDragStart"
  @node-drag-enter="handleDragEnter"
  @node-drag-leave="handleDragLeave"
  @node-drag-over="handleDragOver"
  @node-drag-end="handleDragEnd"
  @node-drop="handleDrop"
  @node-click="nodeClick"
  draggable
  :allow-drop="allowDrop"
  :allow-drag="allowDrag"
  :filter-node-method="filterNode"
  ref="tree"
  >
<!-- 内容区 -->
  <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            type="text"
            size="mini"
            v-if="data.label === '我的回收站' "
            @click="() => remove(node, data)">
            清空
          </el-button>
        </span>
      </span>
</el-tree>
  <el-upload
  id="tigger"
  class="upload-demo"
  action="http://192.168.1.77:8080/fileController/handleFileUpload"
  multiple
  :file-list="fileList"
  :on-success="onSuccess"
  ref="myload">
  <el-button size="small" type="primary"><i class="el-icon-upload el-icon--right"></i>点击上传</el-button>
</el-upload>
</el-aside>

<!-- 主内容 -->
 <el-main>
  <el-row class="operator_button">
  <el-button type="danger" icon="el-icon-edit" @click="updatePsd()">修改密码</el-button>
  <el-button type="success" @click="upload()"><i class="el-icon-upload el-icon--right"></i>点击上传</el-button>
  <el-button type="primary" icon="el-icon-search" @click="refreshPage()">刷新页面</el-button>
  <el-button type="warning" icon="el-icon-error" @click="loginOut()">注销登录</el-button>
  <el-button type="info"    icon="el-icon-folder-add" @click="tiggerChildEvent()">新建文件夹</el-button>
  
  <!-- 弹窗 -->
  <el-popover
  placement="right"
  width="470"
  trigger="click"
  class="mypopover">
  <span>共享文件夹</span>
<el-tree
  :data="shareFolderData"
  node-key="id"
  border
  :props="defaultProps"
  default-expand-all
  @node-drag-start="handleDragStart"
  @node-drag-enter="handleDragEnter"
  @node-drag-leave="handleDragLeave"
  @node-drag-over="handleDragOver"
  @node-drag-end="handleDragEnd"
  @node-drop="handleDrop"
  draggable
  :allow-drop="allowDrop"
  :allow-drag="allowDrag">
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="data.role === 0"
            type="text"
            size="mini"
            @click="() => open(data)">
            打开文件夹
          </el-button>
          <el-button
            v-if="data.role === 1"
            type="text"
            size="mini"
            @click="() => cancelFileShare(node, data)">
            取消共享
          </el-button>
        </span>
      </span>
</el-tree>
  <el-button slot="reference" @click="lookUpShareFile()">查看共享文件</el-button>
  </el-popover>
</el-row>


  <!-- 导航栏 -->  
      <!-- 面包屑 -->
<el-breadcrumb>
  <el-breadcrumb-item separator="/">导航栏:</el-breadcrumb-item>
      <el-breadcrumb-item v-for=" item in naviList"  :key="item.index"  @click.native="toView(item)">{{item.name}}</el-breadcrumb-item>
</el-breadcrumb>
<!-- 文件视图  二级路由显示 -->
<router-view ref="child" @navigationbar="navigationbar" @getSideData="getSideData"/>

</el-main>
</el-container>

<!-- footer  -->
<el-footer>
</el-footer>
</el-container>
</template>



<script>
import axios from 'axios'
  export default {
    data() {
      return {
        sideData: [{
          id: 1,
          label: '我的文件',
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
        }, {
          id: 2,
          label: '我的分享',
          children: [{
            id: 5,
            label: '二级 2-1'
          }, {
            id: 6,
            label: '二级 2-2'
          }]
        }, {
          id: 3,
          label: '回收站',
          children: [{
            id: 7,
            label: '二级 3-1'
          }, {
            id: 8,
            label: '二级 3-2',
            children: [{
             id: 11,
              label: '三级 3-2-1'
            }, {
              id: 12,
              label: '三级 3-2-2'
            }, {
              id: 13,
              label: '三级 3-2-3'
            }]
          }]
        },],
        
        defaultProps: {
          children: 'childrenList',
          label: 'name'
        },
        // 自定义
        userdata:{
            userId:0
        },
        circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        sizeList: ["large", "medium", "small"],
        filterText: '',
        //上传文件
        fileList:[],
        //存放点击路径
        naviList:[],
        shareFolderData:[]
      };
    },
    // 生命周期函数 
    created(){
    // 直接二级跳转 
        this.getUserInfo()
        //侧边栏视图
        this.getSideData()
        //获取功能按钮
        
    },
    //观察变量的函数
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
    //方法
    methods: {
      handleDragStart(node, ev) {
        console.log('drag start', node,ev);
      },
      handleDragEnter(draggingNode, dropNode, ev) {
        console.log('tree drag enter: ', dropNode.label,ev);
      },
      handleDragLeave(draggingNode, dropNode, ev) {
        console.log('tree drag leave: ', dropNode.label,ev);
      },
      handleDragOver(draggingNode, dropNode, ev) {
        console.log('tree drag over: ', dropNode.label,ev);
      },
      handleDragEnd(draggingNode, dropNode, dropType, ev) {
        console.log('tree drag end: ', dropNode && dropNode.label, dropType,ev);
      },
      handleDrop(draggingNode, dropNode, dropType, ev) {
        console.log('tree drop: ', dropNode.label, dropType,ev);
      },
      allowDrop(draggingNode, dropNode, type) {
        if(dropNode.data.role === 0 && type === 'inner'){
          return true
        }
        if(dropNode.data.role === 1 && draggingNode.data.role === null){
          this.$message("文件节点不允许拖拽")
          return false
        }
          
      },
      allowDrag(draggingNode) {
        if(draggingNode.data.role === 1 || draggingNode.data.role === null){
          return true
        }
        if(draggingNode.data.role === 0){
          this.$message("文件夹不允许拖拽")
          return false
        }
          
      },
      toFileLookView(){
        this.$router.push({
            name:'filelookview',
            query:{
              userId:this.$store.state.userdata.userId
                    }
          })
        },

       
      //得到用户信息
      getUserInfo(){
        console.log(this.$store.state.userdata.userId)
        this.userdata= this.$store.state.userdata
         this.toFileLookView()
      },
      //得到侧边栏数据
      getSideData(){
        console.log('子路由正在调用')
                axios({
                //修改接口  /fileController/getSideViewVo 
                url:'/fileController/getSideViewModify',
                method: 'get',
                params:{
                  username:this.$store.state.userdata.userName,
                  userId:this.$store.state.userdata.userId
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
              .then( res=>{
                console.log("打印侧边栏数据")
                console.log(res)
                if(res.data.code===200){
               this.sideData = res.data.data
                }
            }
            )  
      },
      //修改密码
      updatePsd(){
        this.$router.push({
        path:"/updatepsd",
        query:{
          username:this.userdata.userName,
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
                  username:this.userdata.userName
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
                console.log(res)
                if(res.data.msg==='1'){
                  this.$router.push("/loginpage")
                this.$message(res.data.msg);
                }else{
                this.$message("注销错误");  
                    console.log('error'+res.data);
                }}
            ) 
      },
      //过滤节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      //触发upload
      upload(){
        console.log("触发upload")
           this.$refs['myload'].$refs['upload-inner'].handleClick()
            this.$refs.myload.submit();
            this.$refs['myload'].clearFiles()
            this.$refs['child'].initView()
            this.refreshPage()
      },
      //文件上传成功时的钩子	function(response, file, fileList)	
      onSuccess(response, file, fileList){
        console.log("文件上传成功时的钩子")
        console.log(response, file, fileList)
        if(response.msg ==="repeat"){
                this.$message("文件重复提交,请重新选择")
        }
            this.$refs['myload'].clearFiles()
            this.$refs['child'].initView()
            this.refreshPage()
        
      },

      //触发子路由的方法
      tiggerChildEvent(){
        this.$refs['child'].createFolder()
      },
      //调用子路由的刷新方法
      refreshPage(){
        this.$refs['child'].initView()
        this.getSideData()
      },  
      //写个子路由渲染导航栏方法
      async navigationbar(row,expanded){
        console.log('navigationBar 方法正在被子组件调用')
        console.log(row,expanded)
        if(this.naviList.length === 0 && expanded === true){
          this.naviList.unshift(row)
        }else if(this.naviList.length >= 1 && expanded === true){
          let last=this.naviList[this.naviList.length-1];
          //上下级关系
        if(last.name === row.parentName && expanded === true){
          this.naviList.push(row)
        }
        //同级关系
        if(last.parentName === row.parentName && expanded === true){
         console.log(this.naviList.pop())
          this.naviList.push(row)
          console.log(this.naviList)
        }
        }//做删除处理
        else if(this.naviList.length >0 && expanded === false){
              //遍历数组
              this.naviList.forEach((element,index) => {
                if(element.name === row.name){
                  console.log(index)
                  this.naviList.splice(index)
                }
              });
        }
      },

      //清空
      deletedFileByUserId(){
            axios({
                url:'/fileController/deletedFileByUserId',
                method: 'post',
                params:{
                  userId:this.userdata.userId,
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
                console.log(res)
                this.getSideData()
            }
            )
      },

      //查看共享文件 
      lookUpShareFile(){
            axios({
                url:'/fileController/getShareFileView',
                method: 'get',
                xhrFields: {
                withCredentials: true                   
                },
            })
            .then( res=>{
                //alert("axios请求完成，正在执行页面跳转")
                console.log(res)
                this.shareFolderData=res.data.data
            }
            )
      },

      //设置点击事件 nodeClick
      nodeClick(data,node,vc){
        console.log(data,node,vc)
        console.log('触发点击事件')
        this.navigationbar(data,true)
        //调用子路由的方法
        this.$nextTick(() =>{
         this.$refs['child'].initViewByModify(data.absolutePath)
        })
      },

      //open 打开文件
      open(data){
        axios({
                //打开文件内
                url:'/fileController/openPath',
                method: 'post',
                params:{
                  openPath:data.absolutePath
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
              .then( res=>{
                console.log("打开文件方法")
                console.log(res)

            }
            )  
      },

      //取消文件共享
      cancelFileShare(node , data ){
       console.log(node , data)
          axios({
            url:'/fileController/cancelShareFile',
            method: 'post',
            data:data,
            contentType: "application/json;charset-utf-8",
            xhrFields: {
            withCredentials: true  
           }, 
             })
          .then(res=>{
           console.log(res)     
            //再查询变
          this.lookUpShareFile();
          
          this.$refs['child'].initView()
            })
      },

      //toView 
      toView(data){
      this.$refs['child'].initViewByModify(data.path)
      console.log(data,777777777)
    }
    }
  };
</script>

<style>

.sub-title{
  line-height: 30px;
   position: absolute;
   left: 50px;
   top: 15px;
}

.block_avatar{
  padding: 10px;
  /* overflow:hidden; */
}
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: left;
    line-height: 30px;
    position: relative;
  }
  
  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    /* line-height: 200px; */
  }
  
  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
    height: 700px;
  }
  
  body > .el-container {
    margin-bottom: 40px;
  }
  
  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }
  
  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
  .operator_button{
    margin: 0;
    margin-bottom: 10px;
    padding: 0;
    line-height: 0px;
     text-align: left;
  }
.filter_input{
  display:flex;
  border: 0px !important;
  
}
.upload-demo{
  display: none;
}

.mypopover{
  margin-left: 10px;
  
}
</style>