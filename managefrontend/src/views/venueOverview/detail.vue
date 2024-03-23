<template>
  <div class="detail-page">

    <!-- 表格1 -->
    <el-card shadow="hover" class="table-card">
      <div slot="header" class="slot-header">
        <span class="important-font">参观场馆</span>
        <el-button type="primary" round class="return-button" @click="goBack">返回</el-button>
      </div>
      <el-table :data="venueVisitInfoList" stripe border height="450px" style="width: 100%">
        <!-- 表格列配置 -->
        <el-table-column prop="orderNumber" label="预约编号">
          <template slot-scope="scope">{{ scope.row.orderNumber}}</template>
        </el-table-column>
        <el-table-column prop="appointmentDate" label="预约日期">
          <template slot-scope="scope">
            {{ new Date(scope.row.bookTime).toISOString().split('T')[0] }}
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱地址">
          <template slot-scope="scope">{{ scope.row.userMail}}</template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证号">
          <template slot-scope="scope">{{ scope.row.idCardNum}}</template>
        </el-table-column>

      </el-table>
      <!-- 翻页组件，注意替换为实际的翻页组件 -->
      <el-pagination
        @current-change="fetchVenueDetailData"
        :current-page.sync="venueCurrentPage"
        :page-size="venuePageSize"
        layout="total, prev, pager, next"
        :total="venueTotalItems">
      </el-pagination>
    </el-card>

    <!-- 表格2 -->
    <el-card shadow="hover" class="table-card">
      <div slot="header" class="slot-header">
        <span class="important-font">参与活动</span>
        <el-button type="primary" round class="return-button" @click="goBack">返回</el-button>
      </div>
      <el-table :data="activityInfoList" stripe border height="450px" style="width: 100%">
        <!-- 表格列配置 -->
        <el-table-column prop="orderNumber" label="预约编号">
          <template slot-scope="scope">{{ scope.row.orderNumber}}</template>
        </el-table-column>
        <el-table-column prop="appointmentDate" label="预约日期">
          <template slot-scope="scope">
            {{ new Date(scope.row.bookingTime).toISOString().split('T')[0] }}
          </template>
        </el-table-column>
        <el-table-column prop="activityName" label="活动名称">
          <template slot-scope="scope">{{ scope.row.actName }}</template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱地址">
          <template slot-scope="scope">{{ scope.row.userMail}}</template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证号">
          <template slot-scope="scope">{{ scope.row.idCardNumber }}</template>
        </el-table-column>
      </el-table>
      <!-- 翻页组件，注意替换为实际的翻页组件 -->
      <el-pagination
        @current-change="fetchActivityDetailData"
        :current-page.sync="activityCurrentPage"
        :page-size="activityPageSize"
        layout="total, prev, pager, next"
        :total="activityTotalItems">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      venueVisitInfoList: [], // 替换为实际的数据
      activityInfoList: [], // 替换为实际的数据
      venueDetailTotal: 0, // 替换为实际的总数
      activityDetailTotal: 0, // 替换为实际的总数
      venueCurrentPage: 1,
      venuePageSize: 10,
      venueTotalItems:0,
      activityCurrentPage: 1,
      activityPageSize: 10,
      activityTotalItems:0,
      venueId:localStorage.getItem("venueId"),
    };
  },
  created() {
    this.fetchVenueDetailData();
    this.fetchActivityDetailData();
  },
  methods: {
    fetchVenueDetailData() {
      axios.get(`http://localhost:8888/sys/venueReservation/get-venue-reservations?page=${this.venueCurrentPage}&pageSize=${this.venuePageSize}&venueId=${this.venueId}`)
        .then(response => {
          // 更新列表数据
          this.venueVisitInfoList = response.data.rows.map((venueDetails) => ({ ...venueDetails}));
          // 更新总条目数量
          this.venueTotalItems = response.data.total;
        })
        .catch(error => {
          console.error('获取场馆详细信息失败', error);
        })
        .finally(() => {
          this.listLoading = false;
        });
    },
    fetchActivityDetailData(){
      axios.get(`http://localhost:8888/sys/activityReservation/get-activity-details?page=${this.activityCurrentPage}&pageSize=${this.activityPageSize}&venueId=${this.venueId}`)
        .then(response => {
          // 更新列表数据
          this.activityInfoList = response.data.rows.map((activityDetails) => ({ ...activityDetails}));
          // 更新总条目数量
          this.activityTotalItems = response.data.total;
        })
        .catch(error => {
          console.error('获取场馆详细信息失败', error);
        })
        .finally(() => {
          this.listLoading = false;
        });
    },
    // printForm() {
    //   // 此处为打印表单的逻辑，根据实际需求实现
    //   console.log('打印表单');
    // },
    goBack() {
      this.$router.go(-1); // 返回到上一页
    },
  },

};
</script>

<style scoped>
/* 样式部分，请根据实际情况调整 */
.detail-page {
  display: flex;
  justify-content: space-between;
}

.table-card {
  margin-top: 20px;
  width: 48%;
}

.slot-header{
  display: flex;
  justify-content: space-between;
  align-items: center; /* 垂直居中对齐 */
}

.return-button {
  float: right;
  //margin-right: 10px;
}
</style>

