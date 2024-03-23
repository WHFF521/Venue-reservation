<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">场馆预约后台管理系统</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="Username"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.userPassword"
          :type="passwordType"
          placeholder="Password"
          name="password"
          tabindex="2"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click="login">登录</el-button>

<!--      <div class="tips">-->
<!--        <span style="margin-right:20px;">username: admin</span>-->
<!--        <span> password: any</span>-->
<!--      </div>-->

    </el-form>
  </div>
</template>

<script>
import request from "@/utils/request";
import axios from "axios";
import router, {generateRoutes} from '@/router/index';
import { resetRouter } from '@/router/index';

export default {
  name: 'login',
  data() {
    return {
      loginForm: {
        username: 'lca',
        userPassword: '12345678'
      },
      loginRules: {
        username: [{required: true, trigger: 'blur', },
          {min:1,max:10,trigger: 'blur'}],
        userPassword: [{required: true, trigger: 'blur', },
          {min:1,max:30,trigger: 'blur'}],
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    login() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          axios.get("http://localhost:8888/sys/admin/login", {
            params: {
              username: this.loginForm.username,
              userPassword: this.loginForm.userPassword,
            }
          }).then(res => {
                console.log(res);
                if (res.data.status != true) {
                  console.log('error submit!!');
                  this.$message.error(res.data.msg);
                } else {
                  const adminData = res.data;
                  localStorage.setItem("token", adminData.token);
                  localStorage.setItem("venueId", JSON.stringify(adminData.venueId));
                  console.log(adminData.permission);
                  if (adminData.permission === 0) {
                    console.log(this.redirect);
                    this.$router.push({path:  '/venueOverview'});
                  }else {
                    this.$router.push({path:  '/venueOverview'});
                  }
                }
              }
          )
        }
      });
    },
    // login() {
    //   this.$refs.loginForm.validate(valid => {
    //     if (valid) {
    //       axios.get("http://localhost:8888/sys/admin/login", {
    //         params: {
    //           username: this.loginForm.username,
    //           userPassword: this.loginForm.userPassword,
    //         }
    //       }).then(res => {
    //         console.log(res);
    //         if (res.data.status != true) {
    //           console.log('error submit!!');
    //           this.$message.error(res.data.msg);
    //         } else {
    //           const adminData = res.data;
    //           localStorage.setItem("token", adminData.token);
    //           localStorage.setItem("venueId", JSON.stringify(adminData.venueId));
    //           console.log(adminData.permission);
    //
    //           // 调用生成动态路由的方法，并传入权限信息
    //           resetRouter(adminData.permission);
    //
    //
    //           // 根据权限信息跳转到相应页面
    //           switch (adminData.permission) {
    //             case 0:
    //               router.push({ path: '/venueOverview' });
    //               break;
    //             case 1:
    //               // 跳转到其他页面，例如 '/blacklistManagement' 或 '/venueManagement'
    //               router.push({ path: '/blacklistManagement/banlogOverview' });
    //               break;
    //             case 2:
    //               // 可以不跳转，或者跳转到一个默认页面
    //               break;
    //             default:
    //               break;
    //           }
    //         }
    //       });
    //     }
    //   });
    // },
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
