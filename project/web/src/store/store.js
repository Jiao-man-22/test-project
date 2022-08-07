import Vue from 'vue'
import Vuex from 'vuex'
// 2. 在 Vuex 的 store 、getters 和 mutations 中编写保存方法
//import storage from './storage'

Vue.use(Vuex)
 // 数据保存在state内，在任何组件内通过this.$store.state.XX来读取
export default new Vuex.Store({
    state:{
        userdata : {
            userName:'',
            userId:0,
            userStatus:0,
            user_role:0
    },
    
    },

    mutations: {
    // 用来直接修改state内的数据；在组件内，通过this.$store.commit(方法名)来执行mutations
    // mutations: 本质上是一个对象
    // mutations中主要是改变state初始值的方法, 用法: 通过传入state或额外参数
        createUser(state, userdata) {
            state.userdata = userdata;
        },

    
    },

    // 提交的是mutation, 并且可以异步操作
    // 异步触发mutations中的方法
    // action中的自定义函数: 一个context(上下文)以及要变化的 “形参”
    // context与store实例具有相同的方法和属性, 故context.commit('')有效！
    // 作用：存在业务逻辑
    // action在组件内通过this.$store.dipatch(方法名)来触发
    actions: {
        // this.$store.dispatch('createUser');
        commitToken(context) {
            context.commit('createUser');
        },
    },

    // 将各个组件中的computed中的方法提取出来
    // 实时监测state中变量值的变化
    // 可以通过this.$store.state.xxx获取变量值, 但getters更专业(类似于Bean的getter方法)
    getters:{
        getUser(state) {
            //state.userdata = storage.get('userdata');
            return state.userdata;
        },
    }

  })