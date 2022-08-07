<template>
<div>
<el-input
  placeholder="输入关键字进行过滤"
  v-model="filterText">
</el-input>
<el-breadcrumb separator="/">
   <el-breadcrumb-item  v-for="item in  pathNameList"  :key="item.name" :to="{ path: '/' }">{{item.name}}</el-breadcrumb-item>
</el-breadcrumb>
<el-tree
  class="filter-tree"
  :data="fileStructData"
  show-checkbox
  node-key="name"
  default-expand-all
  @node-drag-start="handleDragStart"
  @node-drag-enter="handleDragEnter"
  @node-drag-leave="handleDragLeave"
  @node-drag-over="handleDragOver"
  @node-drag-end="handleDragEnd"
  @node-drop="handleDrop"
  :allow-drop="allowDrop"
  :allow-drag="allowDrag"
  :props="defaultProps"
  :filter-node-method="filterNode"
  ref="tree">
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
        { name: 'VirtualDisk'},
      ],
      filterText:'' 
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
        if (dropNode.data.label === '二级 3-1') {
          return type !== 'inner';
        } else {
          return true;
        }
      },
      // getNameKey(obj,Node,fileStructData){
          
      // },
      allowDrag(draggingNode) {
        return draggingNode.data.label.indexOf('三级 3-2-2') === -1;
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
      initNar(){


      }
    }
  };
</script>


<style>

</style>