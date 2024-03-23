<template>
  <div class="app-container">
    <el-card shadow="hover">
      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
      >
        <!--第一列-->
        <el-table-column  label="序号" min-width="50" max-width="100" align="center">
          <template slot-scope="scope">{{ scope.row.id}}</template>
        </el-table-column>
        <!--第二列-->
        <el-table-column label="用户邮箱" min-width="100" max-width="200" align="center">
          <template slot-scope="scope">{{ maskUserMail(scope.row.userMail)}}</template>
        </el-table-column>
        <!--第三列-->
        <el-table-column label="联系电话" min-width="100" max-width="200" align="center" >
          <template slot-scope="scope">{{ maskPhoneNumber(scope.row.phoneNumber)}}</template>
        </el-table-column>
        <!--第四列-->
        <el-table-column label="用户名" min-width="150" max-width="200" align="center">
          <template slot-scope="scope">{{ scope.row.nickName}}</template>
        </el-table-column>

        <el-table-column label="持续时间" min-width="100" max-width="200" align="center" >
          <template slot-scope="scope">{{ scope.row.banDays}}</template>
        </el-table-column>

        <!--第五列-->
        <el-table-column align="center" label="操作" min-width="100" max-width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="showPasswordDialog(scope.row)">解除</el-button>
          </template>
        </el-table-column>

      </el-table>
    </el-card>
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
      <el-button type="primary" @click="confirmPassword">确定</el-button>
    </el-dialog>

    <!--分页-->
    <el-pagination
      @current-change="fetchBanedUserData()"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="totalItems">
    </el-pagination>
  </div>
</template>

<script>
import axios from "axios";

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: [],
      banedUserMail:"",
      listLoading: true,
      passwordDialogVisible: false, // 控制密码输入弹窗的显示状态
      password: '', // 保存用户输入的密码

      currentPage: 1,
      pageSize: 2, // 根据需要调整每页显示的数量
      totalItems: 0,
    }
  },
  created() {
    this.fetchBanedUserData();
  },
  methods: {
    fetchBanedUserData() {
      axios
        .get(`http://localhost:8888/sys/user/getBanedUser?page=${this.currentPage}&pageSize=${this.pageSize}`)
        .then((response) => {
          // 将后端返回的数据赋值给list
          this.list = response.data.rows.map((banedUser, index) => ({...banedUser,id: index + 1,}));
          this.totalItems = response.data.total;
        })
        .catch((error) => {
          console.error("获取黑名单数据失败", error);
        })
        .finally(() => {
          this.listLoading = false;
        });
    },
    maskPhoneNumber(phoneNumber) {
      if (phoneNumber && phoneNumber.length >= 3) {
        return phoneNumber.substring(0, 3) + '*'.repeat(phoneNumber.length - 3);
      }
      return phoneNumber;
    },
    // 脱敏处理邮箱地址
    maskUserMail(userMail) {
      if (userMail && userMail.includes('@')) {
        const parts = userMail.split('@');
        if (parts[0].length >= 3) {
          return parts[0].substring(0, 3) + '*'.repeat(parts[0].length - 3) + '@' + parts[1];
        }
      }
      return userMail;
    },

    showPasswordDialog(row) {
      this.banedUserMail = row.userMail;
      // 点击提交按钮，显示密码输入弹窗
      this.passwordDialogVisible = true;
    },
    resetPasswordDialog() {
      // 关闭密码输入弹窗时，重置密码相关数据
      this.passwordDialogVisible = false;
      this.password = '';
    },
    confirmPassword() {
      // 在点击确定后，验证密码的逻辑
      if (this.password === '123456') { // 假设密码为 '123456'，您应该根据实际情况修改
        // 密码正确，执行 updatePermission 接口的功能
        this.updateUserStatus();
        this.resetPasswordDialog(); // 关闭密码输入弹窗
      } else {
        // 密码错误，提示用户重新输入
        this.$message.error('密码错误，请重新输入');
      }
    },
    updateUserStatus() {
      const userMail = this.banedUserMail;
      console.log(userMail);
      axios
        .put('http://localhost:8888/sys/user/updateUserStatus',{userMail})
        .then( response => {
          // 根据接口返回的数据进行处理
          this.$message('解除成功');
          // 可以刷新管理员数据或执行其他逻辑
          this.fetchBanedUserData();
        })
        .catch(error => {
          console.error('解除失败', error);
          this.$message.error('解除失败，请重试');
        });
    },

  }
}
</script>
