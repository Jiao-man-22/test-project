<template>
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
<el-input
  placeholder="输入关键字进行过滤"
  v-model="filterText">
</el-input>
<el-breadcrumb separator="/">
   <el-breadcrumb-item  v-for="item in  narList"  :key="item.name" :to="{ path: '/' }">{{item.name}}</el-breadcrumb-item>
</el-breadcrumb>
<!-- 绑定事件 -->
<el-tree
  class="filter-tree"
  :data="fileStructData"
  draggable:true
  :props="defaultProps"
  default-expand-all:false
  :filter-node-method="filterNode"
  ref="tree">

  <span class="custom-tree-node" slot-scope="{ node, data }">
        <span v-if="data.role===1"  @dbclick="dbClick(data)"><i class="el-icon-document"></i> {{ node.label }}</span>
        <span v-if="data.role===0"  @dbclick="dbClick(data)"><i class="el-icon-folder"></i> {{ node.label }}</span>
        <span>
          <!-- <el-button
            v-if="data.role === 0 && data.name !== 'shareFolder'"
            type="text"
            size="medium"
            @click="() => append(data,node)">
            <i class="el-icon-folder-add"></i>
            <span>新建文件夹</span>
          </el-button>  -->
          <!-- <el-button
            v-if="data.name !== 'shareFolder'"
            type="text"
            size="medium"
            @click="() => remove(node, data)">
            <i class="el-icon-folder-delete"></i>
            <span v-if="data.parent_name === 'shareFolder'">取消共享</span>
            <span v-if="data.parent_name !== 'shareFolder'">删除</span>
          </el-button> -->
         <!--追加一个下载  -->
            <!-- <el-button
            v-if="data.role === 1"
            type="text"
            size="medium"
            @click="() => download(data)">
            <i class="el-icon-bottom"></i>
            <span>下载文件</span>
          </el-button> -->
        </span>
      </span>
</el-tree>
</div>
</template>

<script>
import axios from 'axios'
  export default {
    data() {
      return {
        fileStructData: [
          {
            name:'VirtaulDisk'
          }
        ],
        defaultProps: {
          children: 'chlid_list',
          label: 'name'
        },
      pathNameList:[
        { name: ''},
      ],
      filterText:'' ,
      copyDragData:{
        sourcePath:'',
        fileName:'',
        targetPath:''
      },
      centerDialogVisible:false,
      shareQuery:{
         name:'',
         absolutePath:''
       },
      queryArray:[],
      narList:[]
      };
    },
    created(){
      this.initTreeView()
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
    methods: {
       filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      // handleDragStart(node, ev) {
      //   console.log('drag start', node,ev);
      // },
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
        console.log('tree drag end: ', dropNode && dropNode.label,ev +"handleDragEnd方法" +dropType +dropNode.data.role);
        if(dropNode.data.role=== 0){
          console.log('进入node targetPath==')
          this.copyDragData.fileName=draggingNode.data.name
          this.copyDragData.sourcePath=draggingNode.data.absolutePathArray
          this.copyDragData.targetPath=dropNode.data.absolutePath
          console.log(this.copyDragData)
          this.dragCopy(this.copyDragData)
        }else if(dropNode.data.role=== 1){
          this.$message("node 节点是文件 不可以拖入")
        }
      },
      onCheck( currentdata,checkedData){
        console.log("进入onCheck 方法")
        this.centerDialogVisible = true
        console.log(currentdata)
        console.log(checkedData)
        for(var index in checkedData.checkedNodes){
          console.log("item=====")
          console.log(checkedData.checkedNodes[index])
          this.shareQuery.name=checkedData.checkedNodes[index].name
          this.shareQuery.absolutePath=checkedData.checkedNodes[index].absolutePath
          this.queryArray.push(this.shareQuery)
        }
         console.log("开始调用centerDialogVisibleByme 方法")
        this.centerDialogVisibleByme(this.queryArray)
         console.log("调用OK  ===centerDialogVisibleByme 方法")
      },
      handleDrop(draggingNode, dropNode, dropType, ev) {
      //   this.copyDragData.fileName=draggingNode.data.name
      //   this.copyDragData.sourcePath=draggingNode.data.sourcePath
      //   this.copyDragData.targetPath=dropNode.data.absolutePath
      //   if(dropType==='inner'){
      //   console.log('inner====handleDrop')
      //   console.log(ev)
      //   console.log(dropType)
      //   console.log(this.copyDragData)
      //   console.log(ev)
      //     this.dragCopy(this.copyDragData)
      //   }else{
         console.log(ev)
         console.log(dropType)
         console.log(this.copyDragData)
         console.log(ev)
      //   }
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
      // 导航栏
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
      //回退
      nodeCollapse(data){
        console.log(data)
        if(this.narList.length>=1){
          console.log(this.narList.length)
          this.narList.pop()
          console.log(this.narList.length)
        }
      },
      // 目录结构视图
      initTreeView(){
              axios({
                url:'/fileController/flieDiskStructrue',
                method: 'get',
                xhrFields: {
                withCredentials: true                   
                },
            })
              .then( res=>{
                console.log("创建目录视图")
                console.log(res.data.data)
                if(res.data.code===200){
               this.fileStructData = res.data.data
               console.log("fileStructData")
               console.log(this.fileStructData)
                }
            }
            )  
      },
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
            })
},
// 设置共享
    centerDialogVisibleByme(queryArray){
       console.log("在 centerDialogVisibleByme 方法 inner ")
       console.log(queryArray)
               axios({
                url:'/fileController/fileShare',
                method: 'post',
                data:queryArray,
                contentType: "application/json;charset-utf-8",
                xhrFields: {
                withCredentials: true                   
                },
                
            })
            .then(res=>{
              console.log(res)
            })
          }
    }
  };
</script>


<style>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }

</style>