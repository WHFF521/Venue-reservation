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
          <template slot-scope="scope">{{ scope.row.banlogId }}</template>
        </el-table-column>
        <!--第二列-->
        <el-table-column label="用户姓名" min-width="100" max-width="200" align="center">
          <template slot-scope="scope">{{ scope.row.nickName }}</template>
        </el-table-column>
        <!--第三列-->
        <el-table-column label="拉黑时间" min-width="100" max-width="200" align="center" >
          <template slot-scope="scope">{{ new Date(scope.row.banTime).toISOString().split('T')[0] }}</template>
        </el-table-column>
        <!--第四列-->
        <el-table-column label="拉黑原因" min-width="150" max-width="250" align="center">
          <template slot-scope="scope">
            恶意取消预约
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!--分页-->
    <el-pagination
      @current-change="fetchBanlogData()"
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
      listLoading: true,
      currentPage: 1,
      pageSize: 2, // 根据需要调整每页显示的数量
      totalItems: 0,
    }
  },
  created() {
    this.fetchBanlogData();
  },
  methods: {
    fetchBanlogData() {
      axios
        .get(`http://localhost:8888/sys/banlog/getBanlog?page=${this.currentPage}&pageSize=${this.pageSize}`)
        .then((response) => {
          // 将后端返回的数据赋值给list
          this.list = response.data.rows.map((banlog) => ({ ...banlog }));
          this.totalItems = response.data.total;
        })
        .catch((error) => {
          console.error("获取黑名单数据失败", error);
        })
        .finally(() => {
          this.listLoading = false;
        });
    },
  }
}
</script>
