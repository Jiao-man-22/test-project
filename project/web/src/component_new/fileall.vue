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
        <el-table-column
      label="文件所属者"
      width="180">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium">{{ scope.row.userId }}</el-tag>
          </div>
        </el-popover>
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
        <!-- <el-button
          size="mini"
          @click="handleEdit(scope.$index, scope.row)">编辑</el-button> -->
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
        tableData: [{
          upload: '2016-05-02',
          fliename: '',
          filesize: '',
          owner:0
        }, {
          upload: '2016-05-02',
          fliename: '',
          filesize: '',
          owner:0
        }
        ]
      }
    },
    created(){
      this.initView()
    },
    methods: {
      handleEdit(index, row) {
        console.log(index, row);
      },
      handleDelete(index, row) {
        console.log(index, row);
        axios({
                url:'/fileController/deleteFileById',
                method: 'post',
                params:{
                  fileId:row.fileId,
                },
            })
            .then( res=>{
              this.initView()
              console.log(res)
              this.$message(res.data.msg)
            })
      },
            // 目录结构视图
      initView(){
              axios({
                url:'/fileController/getAllFilesByAdmin',
                method: 'get',
                xhrFields: {
                withCredentials: true                   
                },
            })
              .then( res=>{
                console.log(res.data.data)
                if(res.data.code===200){
               this.tableData = res.data.data
                }
            }
            )  
      },
    }
  }
</script>