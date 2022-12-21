import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//在main.js中引入 axios
import axios from 'axios'
// import Sortable from "sortablejs";



import store from './store/store'
// import storage from './store/storage'

axios.defaults.baseURL='http://192.168.137.1:8083'
import VueContextMenu from 'vue-contextmenu'
Vue.use(VueContextMenu)

//Vue 原型挂载
Vue.prototype.axios=axios
//使用qs

Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
    router,
    store,
    //storage,
    render: h => h(App)
}).$mount('#app')

