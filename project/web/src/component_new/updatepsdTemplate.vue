<template>
<!-- 修改密码显示视图 -->
  <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <span>{{ruleForm.username}}|{{ruleForm.userId}}</span>
  <el-form-item label="密码" prop="password">
    <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
  </el-form-item>
  <el-form-item label="确认密码" prop="checkPass">
    <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
  </el-form-item>
  <!-- <el-form-item label="年龄" prop="age">
    <el-input v-model.number="ruleForm.age"></el-input>
  </el-form-item> -->
  <el-form-item>
    <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
    <el-button @click="resetForm('ruleForm')">重置</el-button>
  </el-form-item>
</el-form>
</template>

<script>
import axios from 'axios'
  export default {
    data() {
    //   var checkAge = (rule, value, callback) => {
        // if (!value) {
        //   return callback(new Error('年龄不能为空'));
        // }
    //     setTimeout(() => {
    //       if (!Number.isInteger(value)) {
    //         callback(new Error('请输入数字值'));
    //       } else {
    //         if (value < 18) {
    //           callback(new Error('必须年满18岁'));
    //         } else {
    //           callback();
    //         }
    //       }
    //     }, 1000);
    //   };
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        ruleForm: {
          password: '',
          checkPass: '',
          userId:'',
          username:''
        //   age: ''
        },
        rules: {
          pass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ]
        //   age: [
        //     { validator: checkAge, trigger: 'blur' }
        //   ]
        }
      };
    },
    created(){
       this.ruleForm.userId= this.$route.query.userId;
       this.ruleForm.username=this.$route.query.username;
    },
    methods: {
       AxiosRequest(){
            console.log("AxiosRequest" + this.ruleForm.password);
            console.log("AxiosRequest" + this.ruleForm.username);
        axios({
                method:"post",
                url:"http://localhost:8080/user/updateUserPassword",
                params:{
                   username:this.ruleForm.username,
                   password:this.ruleForm.password,
                   userid:this.ruleForm.userId
                },
            }).then((response)=>{
                console.log(response);
            });
            console.log("axios end")
        },
      submitForm(ruleForm) {
          this.AxiosRequest(ruleForm);
        this.$refs[ruleForm].validate((valid) => {
          if (valid) {
            alert('submit!');
            // this.AxiosRequest(ruleForm);
            console.log(this.ruleForm.password);
            console.log(this.ruleForm.username);
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }

    }
  }
</script>
<style>

</style>