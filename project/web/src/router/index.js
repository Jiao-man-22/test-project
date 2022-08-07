import Vue from 'vue'
import VueRouter from 'vue-router'
import userhomepage from '../views/UserHomePage.vue'
import loginpage from '../views/LoginPage.vue'
import updatepsd from '../component_new/updatepsdTemplate.vue'
import diskpathview from '../component_new/diskpathView.vue'
import userallview from '../component_new/userallview.vue'
import fileall from '../component_new/fileall.vue'
import CommonUserHomePage from '../views/CommonUserHomePage.vue'
import userFileView from '../component_new/userFileView.vue'
import modifyuserpage from '../views/modifyUserpage.vue'
import filelookview from '../component_new/FileLookView.vue'

Vue.use(VueRouter)

const routes = [
  // prod
  {
    path: '/',
    redirect: '/loginpage'
  },

 // {
//    path: '/adminPage',
//    name: 'AdminPage',
//    redirect: "/userTablepage",
//    component: adminPage,
//    children: [{
//      path: "/userTablePage",
//     component: userTablePage
//    },
//    {
//      path: '/postsPage',
//      //name: 'PostsPage',
//      component: postsPage
//    }]
 // },
  // {
  //     path:"/adminPage",
  //     name:"AdminPage",
  //     component:()=>import('../components/adminPage'),
  //     children:[
  //     {path:'/userTablePage',userTablePage},
  //     {path:'/postsPage',postsPage}
  //     ]
  // },
  {
    path:'/userhomepage',
    component:userhomepage,
        children:[
        {name:'diskpathview',path:'/diskpathview',component:diskpathview},
        {name:'userallview',path:'/userallview',component:userallview},
        {name:'fileall',path:'/fileall',component:fileall},
       ]
  },
  {
    path:'/CommonUserHomePage',
    name:'CommonUserHomePage',
    component:CommonUserHomePage,
    children:[
       {name:'userFileView',path:'/userFileView',component:userFileView},
       {name:'filelookview',path:'/filelookview',component:filelookview},

     ]
  },


  {
    path:'/loginpage',
    component:loginpage,
  },
  {
    path:'/updatepsd',
    component:updatepsd,
  },

  {
    path:'/modifyuserpage',
    component:modifyuserpage,
  }


]

// 解决重复点击相同路由报错
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
