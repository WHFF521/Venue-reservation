<template>
  <div class="app-container">
    <el-form ref="form" :model="adminForm" label-width="120px">
      <!--用户名输入-->
      <el-form-item label="用户名:">
        <el-input v-model="adminForm.username" />
      </el-form-item>

      <!--密码设置-->
      <el-form-item label="密码:">
        <el-input v-model="adminForm.userPassword" />
      </el-form-item>

      <!--下拉单选框-->
      <el-form-item label="所属场馆:">
        <el-select v-model="adminForm.venueId" placeholder="请选择管理员所属场馆">
          <el-option v-for="venue in venueOptions" :key="venue.value" :label="venue.label" :value="venue.value" />
        </el-select>
      </el-form-item>

      <!--提交/取消按钮-->
      <el-form-item>
        <el-button type="primary" @click="showPasswordDialog">提交</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>

    <!-- 密码输入弹窗 -->
    <el-dialog
      title="请输入管理员密码"
      :visible.sync="passwordDialogVisible"
      width="30%"
      @close="resetPasswordDialog"
    >
      <el-input
        v-model="password"
        type="password"
        placeholder="请输入密码"
        style="margin-bottom: 10px;"/>
      <el-button type="primary" @click="verifyPassword">确定</el-button>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      adminForm: {
        userPassword: '',
        username: '',
        venueId: '',
      },
      venueOptions: [], // 新增数组用于存储场馆选项
      passwordDialogVisible: false, // 控制密码输入弹窗的显示状态
      password: '' // 保存用户输入的密码
    }
  },
  created() {
    // 在组件创建时调用接口获取场馆信息
    this.getVenues();
  },
  methods: {
    getVenues() {
      // 调用接口的逻辑
      axios.get('http://localhost:8888/sys/venue/getBasicVenue')
        .then(response => {
          // 处理成功响应
          this.venueOptions = response.data.rows.map(venue => ({
            label: `${venue.venueId}.${venue.venueName}`,
            value: venue.venueId
          }));
        })
        .catch(error => {
          // 处理错误响应
          console.error('Error getting venues:', error);
          this.$message.error('获取场馆信息失败，请重试');
        });
    },
    showPasswordDialog() {
      this.passwordDialogVisible = true;
    },
    resetPasswordDialog() {
      // 关闭密码输入弹窗时，重置密码相关数据
      this.passwordDialogVisible = false;
      this.password = '';
    },
    verifyPassword() {
      // 验证密码的逻辑
      if (this.password === '123456') {
        // 密码正确，执行提交操作
        this.onSubmit();
        this.resetPasswordDialog(); // 关闭密码输入弹窗
      } else {
        // 密码错误，提示用户重新输入
        this.$message.error('密码错误，请重新输入');
      }
    },
    onSubmit() {
      // 调用接口的逻辑
      axios.post('http://localhost:8888/sys/admin/addAdmin', this.adminForm)
        .then(response => {
          // 处理成功响应
          this.$message('创建成功!');
          this.resetPasswordDialog(); // 关闭密码输入弹窗
        })
        .catch(error => {
          // 处理错误响应
          console.error('Error creating admin:', error);
          this.$message.error('创建管理员失败，请重试');
        });
    },
    onCancel() {
      this.$message({
        message: '已取消!',
        type: 'warning'
      })
    }
  }
}
</script>

<style scoped>
</style>

