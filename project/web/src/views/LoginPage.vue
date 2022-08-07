<template>
  <div class="Login_1_container">
      <div class="Login_1_box">
          <!-- 用户头像区 -->
          <div class="avatar_box">
              <img src="../assets/logo.png" alt="这是用户头像盒子">
          </div>
          <!-- 表单内容区 -->
          <!-- 动态s为form 表单添加属性 对象 -->
          <!-- 绑定规则对象 -->
          <!--ref 绑定组件的引用对象  -->
          <el-form ref="component_forms" :model="login_form"  :rules="login_form_rules"  label-width="0px" class="login_form">
             <!-- 用户名 -->
            <el-form-item prop="username">
            <el-input v-model="login_form.username" prefix-icon="el-icon-user" ></el-input>
            </el-form-item>
            <!-- 密码 -->
            <el-form-item prop="password">
            <el-input v-model="login_form.password" type="password" prefix-icon="el-icon-lock" ></el-input>
            </el-form-item>
            <!-- userid -->
            <!-- <el-form-item prop="userid">
            <el-input v-model="login_form.userid" type="userid" prefix-icon="el-icon-lock" ></el-input>
            </el-form-item> -->
         <!-- 登陆   重置 -->
            <el-form-item class="btns">
             <el-button type="primary" @click="p_login_form(login_form)">登陆</el-button>
             <el-button type="info" @click="reset_form">重置</el-button>
            </el-form-item>
          </el-form>
      </div>
  </div>
</template>

<script>
import axios from 'axios'
// import storage  from 'storage'
export default {
    //属性绑定,表单的数据绑定对象
    data(){
        return{
            // 创建对象 存放user and password
            login_form:{
                username:'',
                password:'',
                rememberMe: false,
                code: "",
                uuid: "",
                userid:""
            },
            //这是表单验证规则对象

            login_form_rules:{
            username:[
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 1, max: 5, message: '长度在 1 到 5 个字符', trigger: 'blur' }
            ],
            password:[
            { required: true, message: '请输入密码名称', trigger: 'blur' },
            { min: 1, max: 11, message: '长度在 1 到 11 个字符', trigger: 'blur' }
            ],
            // userid:[
            // { required: true, message: '请输入用户标识', trigger: 'blur' },
            // { min: 1, max: 11, message: '长度在 1 到 11 个字符', trigger: 'blur' }
            // ]

            },
            loading: false,
            // 验证码开关
             captchaOnOff: true,
            // 注册开关
            register: false,
            redirect: undefined

        }
    },
    //绑定组件动作
    methods:{
        //重置动作
        reset_form(){
            this.$refs.component_forms.resetFields();
        },
        //登陆预验证
        p_login_form(login_form){
            //发送axios请求
            axios({
                url:'/common/doLogin',
                method: 'get',
                params:{
                   username:login_form.username,
                   password:login_form.password,
                   userid:login_form.userid
                },
                xhrFields: {
            withCredentials: true                   
                },
            })
            .then( res=>{
                console.log(res);
                //alert("axios请求完成，正在执行页面跳转")
                //storage.set("userdata",JSON.stringify(res.data.data))
                // this.$store.commit('createUser', storage.get('userdata'));
                // this.$store.getters.getUser("userdata")
                //this.$store.state.commit("createUser",res.data.data)
                this.$store.state.userdata.userName=res.data.data.user_name
                this.$store.state.userdata.userId=res.data.data.user_id
                this.$store.state.userdata.user_role=res.data.data.role

                //this.$store.getter.getUser()
                if(res.data.code===200 && res.data.data.role === 1){
                this.$message("登陆成功")
                this.$router.push({
                    path :'/userhomepage',
                    query:{
                        username:res.data.data.user_name,
                        userId:res.data.data.user_id,
                        role:res.data.data.role,
                        status:res.data.data.status
                    }
                });//普通用户
                }else if(res.data.code===200 && res.data.data.role === 0 ){
                //做跳转
                   this.$router.push({
                    name :'CommonUserHomePage',
                    query:{
                        username:res.data.data.user_name,
                        userId:res.data.data.user_id,
                        role:res.data.data.role,
                        status:res.data.data.status
                    }
                });
                }else{
                     this.$message("登陆失败")
                }
            }
            )  
        }
    }
}
</script>

<style>

.Login_1_container{
    background-color: #253145;
    height: 100%;
    
}
.Login_1_box{
    width: 450px;
    height: 400px;
    background-color:white;
    border-radius: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform:translate(-50%,-25%);
}
.avatar_box{
    height: 130px;
    width: 130px;
    border: 1px solid #eeeeee;
    border-radius: 50%;
    position: absolute;
    left: 50%;
    transform: translate(-50%,-50%);
    padding: 10px;
    box-shadow: 0 0 10px #dddddd;
    background-color: #ffffff;
}
.avatar_box img{
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: #eeeeee;
}
.login_form{
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
    transform: translate(0,-35%);
}
.btns{
    display: flex;
    justify-content: flex-end;
}

</style>
