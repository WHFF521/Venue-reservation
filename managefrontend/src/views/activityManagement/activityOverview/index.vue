<template>
  <div>
  <div class="menu-container">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">进行中</el-menu-item>
      <el-menu-item index="2">已过期</el-menu-item>
    </el-menu>
    <div class="flex-container">
      <el-input v-model="searchActivityName" placeholder="请输入活动名搜索" @keyup.enter.native="handleSearchActivityName" class="custom-input">
        <el-button slot="append" icon="el-icon-search" @click="handleSearchActivityName"></el-button>
      </el-input>
      <el-button type="primary" @click="goToNewActivityPage" class="custom-button">新建活动</el-button>
    </div>
  </div>



    <el-table v-show="activeIndex==='1'"
              :data="ongoingActivityTable"
              style="width: 100%"
              max-height="250">
      <el-table-column
        fixed
        sortable
        prop="activityDate"
        label="活动日期"
        width="120">
      </el-table-column>
      <el-table-column
        prop="activityName"
        label="活动名称"
        width="150">
      </el-table-column>
      <el-table-column
        prop="activityTime"
        label="活动时间"
        width="120">
      </el-table-column>
      <el-table-column
        prop="numOfPersons"
        label="活动人数"
        width="120">
      </el-table-column>
      <el-table-column
        prop="activityDetail"
        label="活动详情"
        width="120">
        <template slot-scope="scope">
          <el-button type="text"  @click.native.prevent="handleActivityDetailClick(scope.$index)">查看详情</el-button>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="120">
        <template slot-scope="scope">
          <el-button
            @click.native.prevent="removeRow(scope.$index)"
            type="text">
            移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-table v-show="activeIndex==='2'"
              :data="expiredActivityTable"
              style="width: 100%"
              max-height="250">
      <el-table-column
        fixed
        sortable
        prop="activityDate"
        label="活动日期"
        width="120">
      </el-table-column>
      <el-table-column
        prop="activityName"
        label="活动名称"
        width="150">
      </el-table-column>
      <el-table-column
        prop="activityTime"
        label="活动时间"
        width="120">
      </el-table-column>
      <el-table-column
        prop="numOfPersons"
        label="活动人数"
        width="120">
      </el-table-column>
      <el-table-column
        prop="activityDetail"
        label="活动详情"
        width="120">
        <template slot-scope="scope">
          <el-button type="text"  @click.native.prevent="handleActivityDetailClick(scope.$index)">查看详情</el-button>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        width="120">
        <el-button type="text">查看预约名单</el-button>
      </el-table-column>
    </el-table>

    <el-pagination
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="5"
      layout="total, prev, pager, next"
      :total="total">
    </el-pagination>
  </div>
</template>

<script>

import axios from "axios";

export default {
  created() {
    this.getAllOngoingActivities();
  },
  data() {
    return {
      activeIndex: '1',
      searchActivityName : '',
      ongoingActivityTable: [],
      expiredActivityTable: [],
      activityDetailDialogFormVisible: false,
      activityDetailForm: {
        activityName: '',
        introduction: '',
        timeSelect: '',
        guideline: '',
        address: '',
        numOfPeople: '',
        date: '',
        time: '',
        earliestBookingTime : '',
        latestBookingTime: '',
        latestCancelTime: '',
      },
      formLabelWidth: '120px',
      fileList: [{
        name: 'food.jpeg',
        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
      }
      ],
      currentPage: 1,
      total: 20,
    }
  },
  methods: {
    handleSearchActivityName(){
      console.log(this.activeIndex);
      if(this.activeIndex==='1'){
        this.ongoingActivityTable=[];
        this.expiredActivityTable=[];
        axios.get("http://localhost:8888/sys/activity/searchOngoingActivities",{
          params:{
            page:1,
            pageSize:5,
            venueId:localStorage.getItem("venueId"),
            actName:this.searchActivityName,
          }
          }).then(res =>{
            console.log(res);
            this.total=res.data.total;
            res.data.rows.forEach(item =>{
              this.ongoingActivityTable.push({
                activityName: item.actName,
                activityDate: item.actDate,
                activityTime: item.actTime,
                numOfPersons: item.actParticipantNum,
                actId:item.actId,
              });
            })
          })
      }else{
        this.ongoingActivityTable=[];
        this.expiredActivityTable=[];
        axios.get("http://localhost:8888/sys/activity/searchExpiredActivities",{
          params:{
            page:1,
            pageSize:5,
            venueId:localStorage.getItem("venueId"),
            actName:this.searchActivityName,
          }
        }).then(res =>{
          console.log(res);
          this.total=res.data.total;
          res.data.rows.forEach(item =>{
            this.expiredActivityTable.push({
              activityName: item.actName,
              activityDate: item.actDate,
              activityTime: item.actTime,
              numOfPersons: item.actParticipantNum,
              actId:item.actId,
            });
          })
        })
      }
    },
    getAllOngoingActivities(){
      this.ongoingActivityTable=[];
      axios.get("http://localhost:8888/sys/activity/getOngoingActivities",{
        params:{
          page:1,
          pageSize:5,
          venueId: localStorage.getItem("venueId")
        }
        }
      ).then(res =>{
        console.log(res);
        this.total=res.data.total;
        res.data.rows.forEach(item =>{
          this.ongoingActivityTable.push({
            activityName: item.actName,
            activityDate: item.actDate,
            activityTime: item.actTime,
            numOfPersons: item.actParticipantNum,
            actId:item.actId,
            });
        })
        })
    },
    getAllExpiredActivities(){
      this.expiredActivityTable=[];
      axios.get("http://localhost:8888/sys/activity/getExpiredActivities",{
          params:{
            page:1,
            pageSize:5,
            venueId: localStorage.getItem("venueId")
          }
        }
      ).then(res =>{
        console.log(res);
        this.total=res.data.total;
        res.data.rows.forEach(item =>{
          this.expiredActivityTable.push({
            activityName: item.actName,
            activityDate: item.actDate,
            activityTime: item.actTime,
            numOfPersons: item.actParticipantNum,
            actId:item.actId,
          });
        })
      })
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      if(this.activeIndex==='1'){
        if(this.searchActivityName){
          this.ongoingActivityTable=[];
          this.expiredActivityTable=[];
          axios.get("http://localhost:8888/sys/activity/searchOngoingActivities",{
            params:{
              page:val,
              pageSize:5,
              venueId:localStorage.getItem("venueId"),
              actName:this.searchActivityName,
            }
          }).then(res =>{
            console.log(res);
            this.total=res.data.total;
            res.data.rows.forEach(item =>{
              this.ongoingActivityTable.push({
                activityName: item.actName,
                activityDate: item.actDate,
                activityTime: item.actTime,
                numOfPersons: item.actParticipantNum,
                actId:item.actId,
              });
            })
          })
        }else{
          this.ongoingActivityTable=[];
          axios.get("http://localhost:8888/sys/activity/getOngoingActivities",{
              params:{
                page:val,
                pageSize:5,
                venueId: localStorage.getItem("venueId")
              }
            }
          ).then(res =>{
            console.log(res);
            this.total=res.data.total;
            res.data.rows.forEach(item =>{
              this.ongoingActivityTable.push({
                activityName: item.actName,
                activityDate: item.actDate,
                activityTime: item.actTime,
                numOfPersons: item.actParticipantNum,
                actId:item.actId,
              });
            })
          })
        }
      }else{
        if(this.searchActivityName){
          this.ongoingActivityTable=[];
          this.expiredActivityTable=[];
          axios.get("http://localhost:8888/sys/activity/searchExpiredActivities",{
            params:{
              page:val,
              pageSize:5,
              venueId:localStorage.getItem("venueId"),
              actName:this.searchActivityName,
            }
          }).then(res =>{
            console.log(res);
            this.total=res.data.total;
            res.data.rows.forEach(item =>{
              this.expiredActivityTable.push({
                activityName: item.actName,
                activityDate: item.actDate,
                activityTime: item.actTime,
                numOfPersons: item.actParticipantNum,
                actId:item.actId,
              });
            })
          })
        }else {
          this.expiredActivityTable=[];
          axios.get("http://localhost:8888/sys/activity/getExpiredActivities",{
              params:{
                page:val,
                pageSize:5,
                venueId: localStorage.getItem("venueId")
              }
            }
          ).then(res =>{
            console.log(res);
            this.total=res.data.total;
            res.data.rows.forEach(item =>{
              this.expiredActivityTable.push({
                activityName: item.actName,
                activityDate: item.actDate,
                activityTime: item.actTime,
                numOfPersons: item.actParticipantNum,
                actId:item.actId,
              });
            })
          })
        }
      }
    },
    handleSelect(key, keyPath) {
      this.activeIndex=key;
      console.log(key, keyPath);
      if(this.activeIndex===1){
        this.getAllOngoingActivities();
      }else{
        this.getAllExpiredActivities();
      }
    },

    removeRow(index) {
        this.$confirm('确认删除该活动吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 发送请求到后端删除与行相关的数据
          console.log(this.ongoingActivityTable[index].actId);
          axios.get("http://localhost:8888/sys/activity/deleteActivity",{
              params: {
                actId: this.ongoingActivityTable[index].actId,
              }
            }
          ).then(response => {
            // 如果后端删除成功，则从tableData数组中删除该行
            if (response.data) {
              this.$message({
                type: 'success',
                message: '删除成功！'
              });
              this.ongoingActivityTable.splice(index, 1);
              this.ongoingActivityTable=[];
              this.getAllOngoingActivities();
            } else {
              this.$message({
                type: 'error',
                message: '删除失败，请重试！'
              });
            }
          }).catch(error => {
            console.error('删除行时出错：', error);
            this.$message({
              type: 'error',
              message: '删除失败，请重试！'
            });
          });
        }).catch(() => {
          // 如果用户取消删除，则什么都不做
        });
    },

    deleteRow(index, rows) {
      rows.splice(index, 1);
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    closeActivityDetailDialog() {
      this.activityDetailDialogFormVisible = false;
    },

    confirmActivityDetailDialog() {
      // 在确认时执行必要的操作
      // 在这里你可以关闭对话框或执行其他操作
      this.activityDetailDialogFormVisible = false;
    },
    handleActivityDetailClick(index) {
      console.log('点击了查看详情按钮');
      if(this.activeIndex==='1'){
        const actId = this.ongoingActivityTable[index].actId;
        this.$router.push({ name: 'activityDetail', params: { actId } });
      }else {
        const actId = this.expiredActivityTable[index].actId;
        this.$router.push({name:'activityDetailWithoutUpdate',params:{ actId }});
      }
    },
    goToNewActivityPage() {
      this.$router.push({name: 'newActivity'});
    }
  }
}
</script>

<style scoped>
.menu-container {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
}

.flex-container {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
}

.custom-date-picker {
  margin-right: 10px; /* 根据需要调整边距 */
}

.custom-input {
  flex: 1; /* 填充剩余空间 */
  margin-right: 10px; /* 根据需要调整边距 */
}

.custom-button {
  margin-right: 10px; /* 根据需要调整边距 */
}

</style>
