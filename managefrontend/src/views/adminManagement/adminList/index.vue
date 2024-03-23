<template>
  <div class="app-container">

    <el-card shadow="hover">
      <el-table
        :data="list"
        v-loading="listLoading"
        element-loading-text="加载中"
        border
        fit
        highlight-current-row
      >

        <!--第一列-->
        <el-table-column align="center" label="管理员ID" min-width="50" max-width="100">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <!--第二列-->
        <el-table-column label="管理员账号" min-width="100" max-width="200" align="center">
          <template slot-scope="scope">{{ scope.row.username }}</template>
        </el-table-column>
        <!--第三列-->
        <el-table-column label="管理员密码" min-width="100" max-width="200"  align="center">
          <template slot-scope="scope">{{ scope.row.userPassword }}</template>
        </el-table-column>
        <!--第四列-->
        <el-table-column label="所属场馆" min-width="150" max-width="250" align="center">
          <template slot-scope="scope">{{ scope.row.venueName }}</template>
        </el-table-column>
        <!--第五列-->
        <el-table-column label="管理员权限" min-width="100" max-width="150" align="center">
          <template slot-scope="scope">{{  mapPermission(scope.row.permission)}}</template>
        </el-table-column>

        <!--第六列-->
        <el-table-column align="center"  label="操作" min-width="100" max-width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="showPasswordDialog(scope.row)">删除/解冻</el-button>
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
      <el-button type="primary" @click="confirmPassword" style="margin-top: 20px;">确定</el-button>
    </el-dialog>

    <el-pagination
      @current-change="fetchAdminData"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="totalItems">
    </el-pagination>
    <!--分页-->
<!--    <el-pagination-->
<!--      class-name="el-pagination"-->
<!--      background-->
<!--      layout="prev, pager, next"-->
<!--      :total="totalItems"-->
<!--      :current-page.sync="currentPage"-->
<!--      :page-size="pageSize"-->
<!--      @current-change="fetchAdminData"-->
<!--    >-->
<!--    </el-pagination>-->
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
      adminInfo: {
        username: "",
        userPassword: "",
        venueId: "",
        permission: "",
      },
      list: [],
      listLoading: true,
      passwordDialogVisible: false, // 控制密码输入弹窗的显示状态
      password: '', // 保存用户输入的密码
    //   分页相关
      currentPage: 1,
      pageSize: 2, // 根据需要调整每页显示的数量
      totalItems: 0,
    }
  },
  created() {
    this.fetchAdminData();
  },
  methods: {
    fetchAdminData() {
      // 根据当前页和每页数量计算起始索引
      const startIndex = (this.currentPage - 1) * this.pageSize;

      axios.get(`http://localhost:8888/sys/admin/getAllAdmin?page=${this.currentPage}&pageSize=${this.pageSize}`)
        .then(response => {
          // 更新列表数据
          this.list = response.data.rows.map((admin, index) => ({ ...admin, id: startIndex + index + 1 }));
          // 更新总条目数量
          this.totalItems = response.data.total;
        })
        .catch(error => {
          console.error('获取管理员数据失败', error);
        })
        .finally(() => {
          this.listLoading = false;
        });
    },
    mapPermission(permission) {
      switch (permission) {
        case 0:
          return '场馆管理';
        case 1:
          return '系统管理';
        case 2:
          return '已冻结';
        default:
          return '未知权限';
      }
    },
    showPasswordDialog(row) {
      // 点击删除/解冻按钮，显示密码输入弹窗，并传入当前行的数据
      this.adminInfo = {
        username: row.username,
        userPassword: row.userPassword,
        permission: row.permission,
        venueId: row.venueId,
      };
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
        this.updatePermission();
        this.resetPasswordDialog(); // 关闭密码输入弹窗
      } else {
        // 密码错误，提示用户重新输入
        this.$message.error('密码错误，请重新输入');
      }
    },
    updatePermission() {
      const admin = {
        permission: this.adminInfo.permission,
        userPassword: this.adminInfo.userPassword,
        username: this.adminInfo.username,
        venueId: this.adminInfo.venueId,
      };

      axios
        .put('http://localhost:8888/sys/admin/updatePermission', admin)
        .then( response => {
          // 根据接口返回的数据进行处理
          this.$message('操作成功');
          // 可以刷新管理员数据或执行其他逻辑
          this.fetchAdminData();
        })
        .catch(error => {
          console.error('操作失败', error);
          this.$message.error('操作失败，请重试');
        });
    },
    // onSubmit() {
    //   this.$message('删除成功')
    // //   后端删除数据
    // },

  }
}
</script>
