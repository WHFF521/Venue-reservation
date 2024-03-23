<template>
  <div class="dashboard-container">
    <div class="dashboard-text"></div>
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <!-- 管理员信息 -->
          <el-card class="custom-card" shadow="never">
            <div class="userCard">
              <el-avatar :size="150" :src="imgUrl"></el-avatar>
              <div class="userInfo">
                <p class="important-font">管理员</p>
                <p class="secondary-font">场馆管理员</p>
              </div>
            </div>
            <div class="login-info">
              <p>所属场馆Id：{{ venueId }}</p>
            </div>
          </el-card>
          <!-- 预约表格 -->
          <el-card class="custom-card" shadow="never" >
            <div class="tableInfo">
              <div slot="header">
              <span class="important-font">预约信息</span>
            </div>
              <el-button type="text" class="detail-button" @click="goToDetailPage">
                查看详情
              </el-button>
              <el-table
                  :data="tableData"
                  stripe
                  border
                  height="450px"
                  style="width: 100%"
                >
                  <el-table-column prop="date" label="日期" width="120">
                    <template slot-scope="scope">
                      {{ new Date(scope.row.bookTime).toISOString().split('T')[0] }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="name" label="邮箱" width="80">
                    <template slot-scope="scope">{{ scope.row.userMail}}</template>
                  </el-table-column>
                  <el-table-column prop="address" label="身份证">
                    <template slot-scope="scope">{{ scope.row.idCardNumber}}</template>
                  </el-table-column>
                </el-table>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="16">
        <!-- 第一个图表容器 -->
        <el-card class="custom-card" shadow="never" >
          <div class="graph-container">
            <div ref="venueChart" class="chart"></div>
          </div>
        </el-card>
        <!-- 第二个图表容器 -->
        <el-card class="custom-card" shadow="never" >
          <div class="graph-container">
            <div ref="activityChart" class="chart" ></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import * as echarts from 'echarts';
import axios from "axios";
export default {
  // id:localStorage.getItem("venueId"),
  name: 'Dashboard',
  computed: {
    ...mapGetters(['name']),
  },
  data() {
    return {
      userName:"",
      permission:"",
      venueId:"",
      imgUrl: '',
      value: new Date(),
      tableData: [],
    };
  },
  mounted() {
    this.venueId = localStorage.getItem("venueId") || "无";
    this.getVenueProfile(this.venueId);
    // 初始化第一个图表
    this.initVenueChart();
    // 初始化第二个图表
    this.initActivityChart();
  },
  methods: {
    getCountByWeek(venueId) {
      // 调用/count-by-week接口
      axios.get(`http://localhost:8888/sys/venueReservation/count-by-week?venueId=${venueId}`)
        .then(response => {
          // 将返回的数据传递给更新图表的方法
          this.updateVenueChart(response.data);
        })
        .catch(error => {
          console.error("Error fetching data:", error);
        });
    },
    updateVenueChart(data) {
      // 获取第一个图表容器
      const chartContainer = this.$refs.venueChart;

      // 使用 ECharts 初始化第一个图表
      const myChart = echarts.init(chartContainer);

      // 图表配置
      const option = {
        title: {
          text: '场馆参观情况'
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            // 使用从接口获取的数据替换原始的硬编码数据
            data: data,
            type: 'line'
          }
        ]
      };

      // 使用配置项设置第一个图表
      myChart.setOption(option);
    },
    initVenueChart() {
      this.getCountByWeek(localStorage.getItem("venueId"));
    },
    getActivityInfo(venueId) {
      // 调用/get-activity-info接口
      axios.get(`http://localhost:8888/sys/activityReservation/get-activity-info?venueId=${venueId}`)
        .then(response => {
          // 将返回的数据传递给更新图表的方法
          this.updateActivityChart(response.data);
        })
        .catch(error => {
          console.error("Error fetching activity data:", error);
        });
    },
    updateActivityChart(data) {
      // 获取第二个图表容器
      const chartContainer = this.$refs.activityChart;

      // 使用 ECharts 初始化第二个图表
      const myChart = echarts.init(chartContainer);

      // 图表配置
      const option = {
        title: {
          text: '活动参与情况'
        },
        xAxis: {
          type: 'category',
          // 使用从接口获取的 x 轴数据
          data: data[0]
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            // 使用从接口获取的 y 轴数据
            data: data[1],
            type: 'bar'
          }
        ]
      };

      // 使用配置项设置第二个图表
      myChart.setOption(option);
    },
    initActivityChart() {
      this.getActivityInfo(localStorage.getItem("venueId"));
    },
    getVenueProfile(venueId){
      axios.get(`http://localhost:8888/sys/venueReservation/getVenueProfile?venueId=${venueId}`)
        .then(response => {
          // 更新列表数据
          this.tableData = response.data.map((admin) => ({ ...admin}));
        })
        .catch(error => {
          console.error('获取管理员数据失败', error);
        })
        .finally(() => {
          this.listLoading = false;
        });
    },
    goToDetailPage() {
      this.$router.push({name: 'detail'});
    }
  },
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.custom-card {
  transition: box-shadow 0.3s; /* 添加阴影过渡效果 */
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1); /* 初始阴影值，根据需要调整 */
}

.custom-card:hover {
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.3); /* 鼠标悬浮时的阴影值，根据需要调整 */
}

.graph-container{
  margin-bottom: 20px;
  height: calc(50vh - 50px); /* 50% of the viewport height minus margins */
}

.chart{
  width: 100%;
  height: 100%;
}

.el-card__body {
  padding: 10px;
}
.userCard{
  height: calc(29vh - 50px);
  display: flex;
  border-bottom: 2px solid #DCDFE6;
  border-radius: 2px;
}
.userInfo{
  width: auto;
  padding: 6% 0 0 6%;
}
.important-font{
  font-weight: 900;
  font-size: 25px;
}
.secondary-font{
  color: #909399;
}
.login-info{
  height: 40px;
  text-align: left;
  color: #909399;
}
.tableInfo{
  margin: 20px 0 0 0;
  height: calc(65vh - 30px);
}
.OrderCard{
  margin: 0 0 30px 30px;
  border: #DCDFE6 1px solid;
  border-radius: 10px;
  i{
    width: 30%;
    line-height: 120px;
    font-size: 30px;
    color:#fff
  }
  div{
    width: 300px;
  }
}
.el-card{
  border: none;
  margin: 20px 0 0 20px;
}
.num{
  display: flex;
  flex-wrap: wrap;
}
.graph{
  margin: 15px 0 0 0;
}
.el-calendar{
  height: 265px ;
}
.el-row {
  margin: 20px 0 0 0; /* 调整为您希望的间隔值 */
}

</style>

