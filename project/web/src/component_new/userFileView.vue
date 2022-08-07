<template>
  <el-table
    :data="tableData"
    style="width: 100%">
    <el-table-column
      label="上传时间"
      width="180">
      <template slot-scope="scope">
        <i class="el-icon-time"></i>
        <span style="margin-left: 10px">{{ scope.row.uploadTime }}</span>
      </template>
    </el-table-column>
    <el-table-column
      label="文件名"
      width="180">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
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
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium">{{ scope.row.fileSize }}</el-tag>
          </div>
        </el-popover>
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
        <!-- <el-button
          size="mini"
          @click="handleEdit(scope.$index, scope.row)">刷新</el-button> -->
        <el-button
          size="mini"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import axios from 'axios'
  export default {
    data() {
      return {
        tableData: [
        ]
      }
    },
    async created(){
      this.initView()
    },
    methods: {
      async handleEdit(index, row) {
        console.log(index, row);
        //this.initView()
      },
     async handleDelete(index, row) {
        console.log(index, row);
        await axios({
                url:'/fileController/deleteFileById',
                method: 'post',
                params:{
                  fileId:row.fileId,
                },
            })
            .then( res=>{
              console.log("handleDelete" + res)
              this.$message("删除成功")
                this.initView();
            })
      },
            // 目录结构视图
      initView(){
              axios({
                url:'/fileController/getFilesByUserId',
                method: 'get',
                params:{
                  userId :'3'
                        },
                xhrFields: {
                withCredentials: true                   
                },
            })
              .then( res=>{
                console.log(res.data.data)
                if(res.data.code===200){
               this.tableData = res.data.data
                }else {
                  this.tableData = []
                }
            }
            )  
      },
    }
  }
</script>