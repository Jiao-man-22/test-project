<template>
  <el-table
    :data="tableData"
    style="width: 100%">
    <el-table-column
      label="创建时间"
      width="180">
      <template slot-scope="scope">
        <i class="el-icon-time"></i>
        <span style="margin-left: 10px">{{ scope.row.create_time }}</span>
      </template>
    </el-table-column>
    <el-table-column
      label="姓名"
      width="180">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium">{{ scope.row.user_name }}</el-tag>
          </div>
        </el-popover>
      </template>
    </el-table-column>

  <el-table-column
      label="身份"
      width="180">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium" v-if="scope.row.role ===1"> 管理员 </el-tag>
            <el-tag size="medium" v-if="scope.row.role ===0">普通用户 </el-tag>
          </div>
        </el-popover>
      </template>
    </el-table-column>
      <el-table-column
      label="状态"
      width="180">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium" v-if="scope.row.status ===0">已逻辑删除</el-tag>
            <el-tag size="medium" v-if="scope.row.status ===1">未逻辑删除</el-tag>
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
        tableData: [
          {user_id:'',
          user_name:'',
          role:0,
          create_time:''}
        ]
      }
    },
    created(){
      this.initUserView()
    },
    methods: {
      handleEdit(index, row) {
        console.log(index, row);
      },
      handleDelete(index, row) {
        console.log(index, row);
        let userid=row.user_id
        axios({
                url:'user/delUserAndFileById',
                method: 'post',
                params:{
                  userid:userid
                },
                xhrFields: {
                withCredentials: true                   
                },
            })
            .then(res=>{
              console.log(res)
              this.initUserView()
            })
      },
      initUserView(){
                axios({
                url:'/user/getUserAll',
                method: 'get',
                xhrFields: {
                withCredentials: true                   
                },
            })
            .then(res =>{
              this.tableData = res.data.data
            })
      },
      
    }
  }
</script>