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
        <el-table-column align="center" label="ID" min-width="50" max-width="100">
          <template slot-scope="scope">{{ scope.row.venueId }}</template>
        </el-table-column>
        <!--第二列-->
        <el-table-column label="场馆名称" min-width="100" max-width="200">
          <template slot-scope="scope">{{ scope.row. venueName}}</template>
        </el-table-column>
        <!--第三列-->
        <el-table-column label="场馆地址" min-width="100" max-width="200" >
          <template slot-scope="scope">{{ scope.row.venueAddress}}</template>
        </el-table-column>
        <!--第四列-->
        <el-table-column label="开放时间" min-width="150" max-width="250" align="center">
          <template slot-scope="scope">{{ scope.row.openTime}}</template>
        </el-table-column>
        <!--第五列-->
        <el-table-column class-name="status-col" label="场馆状态" min-width="100" max-width="150" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.deleteFlag">停用</span>
            <span v-else>正常</span>
          </template>
        </el-table-column>
        <!--第六列-->
        <el-table-column align="center" label="操作" min-width="100" max-width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="showPasswordDialog(scope.row)">删除/启用</el-button>
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
    <!--分页-->
    <el-pagination
      @current-change="fetchVenueData"
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
      venueInfo:{
        venueId:"",
        venueName:"",
        venueAddress:"",
        openTime:"",
        deleteFlag:""
      },
      list: [],
      listLoading: true,
      passwordDialogVisible: false, // 控制密码输入弹窗的显示状态
      password: '' ,// 保存用户输入的密码
      currentPage: 1,
      pageSize: 2, // 根据需要调整每页显示的数量
      totalItems: 0,
    }
  },
  created() {
    this.fetchVenueData()
  },
  methods: {
    fetchVenueData() {
      axios
        .get(`http://localhost:8888/sys/venue/getBasicVenue?page=${this.currentPage}&pageSize=${this.pageSize}`)
        .then((response) => {
          // 将后端返回的数据赋值给list
          this.list = response.data.rows.map((venue) => ({ ...venue }));
          this.totalItems = response.data.total;
        })
        .catch((error) => {
          console.error("获取场馆数据失败", error);
        })
        .finally(() => {
          this.listLoading = false;
        });
    },

    showPasswordDialog(row){
      this.venueInfo = {
        venueId: row.venueId,
        venueName: row.venueName,
        venueAddress: row.venueAddress,
        openTime: row.openTime,
        deleteFlag: row.deleteFlag,
      };
      this.passwordDialogVisible = true;
    },

    confirmPassword() {
      // 在点击确定后，验证密码的逻辑
      if (this.password === '123456') { // 假设密码为 '123456'，您应该根据实际情况修改
        // 密码正确，执行 updatePermission 接口的功能
        this.updateDeleteFlag();
        this.resetPasswordDialog(); // 关闭密码输入弹窗
      } else {
        // 密码错误，提示用户重新输入
        this.$message.error('密码错误，请重新输入');
      }
    },

    updateDeleteFlag() {
      const venueTable = {
        deleteFlag:this.venueInfo.deleteFlag,
        openTime:this.venueInfo.openTime,
        venueAddress:this.venueInfo.venueAddress,
        venueId:this.venueInfo.venueId,
        venueName:this.venueInfo.venueName,
      };
      axios
        .put('http://localhost:8888/sys/venue/updateVenueStatus', venueTable)
        .then(response => {
          // 根据接口返回的数据进行处理
          this.$message('操作成功');
          // 可以刷新管理员数据或执行其他逻辑
          this.fetchVenueData();
        })
        .catch(error => {
          console.error('操作失败', error);
          this.$message.error('操作失败，请重试');
        });
    },
    resetPasswordDialog() {
      // 关闭密码输入弹窗时，重置密码相关数据
      this.passwordDialogVisible = false;
      this.password = '';
    },
  }
}
</script>
