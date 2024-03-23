import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
// 配置路由信息
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  // 场馆概览（主页）
  {
    // 结构框架
    path: '/',
    component: Layout,
    // name:'dashboard',
    // 重定向到二级路由
    redirect: '/login',
    children: [{
      path: '/venueOverview',
      name: 'venueOverview',
      component: () => import('@/views/venueOverview/index'),
      // title设置侧边栏文字，icon设置图标
      meta: { title: '首页', icon: 'el-icon-s-help' },
    }
    ]
  },

  // 在 constantRoutes 数组中添加如下配置
  {
    path: '/detail',
    name: 'detail',
    component: ()=> import('@/views/venueOverview/detail.vue'),
    meta: { title: '详情页面'},  // 根据实际需求设置 title 和 icon
    hidden: true  // 如果不希望在侧边栏显示该路由，设置 hidden 为 true
  },

  // 参观管理
  {
    path: '/visitManagement',
    component:Layout,
    name:'visitManagement',
    meta:{title:'参观管理', icon:'el-icon-s-ticket'},
    children:[
      {
        path:'venueIntroduction',
        name:'venueIntroduction',
        component:()=>import('@/views/visitManagement/venueIntroduction'),
        meta:{title:'场馆简介'}
      },
      {
        path:'bookingManagement',
        name:'bookingManagement',
        component:()=>import('@/views/visitManagement/bookingManagement'),
        meta:{title:'预约管理'}
      },
    ]
  },

  // 活动管理
  {
    path: '/activityManagement',
    component:Layout,
    name:'activityManagement',
    meta:{title:'活动管理', icon:'el-icon-s-flag' },
    children:[
      {
        path:'activityOverview',
        name:'activityOverview',
        component:()=>import('@/views/activityManagement/activityOverview'),
        meta:{title:'活动概览'}
      },
      {
        path:'newActivity',
        name:'newActivity',
        component:()=>import('@/views/activityManagement/newActivity'),
        meta:{title:'新建活动'}
      },
    ]
  },

  {
    path: '/activityDetail/:actId',
    name: 'activityDetail',
    component: ()=> import('@/views/activityManagement/activityOverview/activityDetail.vue'),
    props:true,
    meta: { title: '活动详情'},  // 根据实际需求设置 title 和 icon
    hidden: true  // 如果不希望在侧边栏显示该路由，设置 hidden 为 true
  },

  {
    path: '/activityDetailWithoutUpdate/:actId',
    name: 'activityDetailWithoutUpdate',
    component: ()=> import('@/views/activityManagement/activityOverview/activityDetailWithoutUpdate.vue'),
    props:true,
    meta: { title: '活动详情'},  // 根据实际需求设置 title 和 icon
    hidden: true  // 如果不希望在侧边栏显示该路由，设置 hidden 为 true
  },


  // 黑名单
  {
    path: '/blacklistManagement',
    component:Layout,
    name:'blacklistManagement',
    meta:{title:'黑名单管理', icon:'el-icon-s-release'},
    children: [
      {
      path: 'banlogOverview',
      name: 'banlogOverview',
      component: () => import('@/views/blacklistManagement/banlogOverview/index.vue'),
      // title设置侧边栏文字，icon设置图标
      meta:{title:'黑名单日志'},
      },
      {
        path: 'banlogManagement',
        name: 'banlogManagement',
        component: () => import('@/views/blacklistManagement/banlogManagement/index.vue'),
        // title设置侧边栏文字，icon设置图标
        meta:{title:'黑名单管理'},
      }
    ]
  },

  // 场馆管理（系统）
  {
    path: '/venueManagement',
    component:Layout,
    name:'venueManagement',
    meta:{title:'场馆管理',icon: 'el-icon-school'},
    children:[
      {
        path:'venueList',
        name:'venueList',
        component:()=>import('@/views/venueManagement/venueList'),
        meta:{title:'场馆列表'}
      },
      {
        path:'newVenue',
        name:'newVenue',
        component:()=>import('@/views/venueManagement/newVenue'),
        meta:{title:'新建场馆'}
      },
    ]
  },

  // 账号管理（系统）
  {
    path: '/adminManagement',
    component:Layout,
    name:'adminManagement',
    meta:{title:'账号管理',icon: 'el-icon-s-custom'},
    children:[
      {
        path:'adminList',
        name:'adminList',
        component:()=>import('@/views/adminManagement/adminList'),
        meta:{title:'账号列表'}
      },
      {
        path:'newAdmin',
        name:'newAdmin',
        component:()=>import('@/views/adminManagement/newAdmin'),
        meta:{title:'新建账号'}
      },
    ]
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]
const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
