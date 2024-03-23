<template>

  <div class="app-container">
    <div class="header-container">
      <el-form ref="bookingManagementForm" :model="bookingManagementForm" label-width="120px">
          <el-select v-model="bookingManagementForm.day" placeholder="请选择星期" @change="handleDayChange">
            <el-option label="星期一" value="1"></el-option>
            <el-option label="星期二" value="2"></el-option>
            <el-option label="星期三" value="3"></el-option>
            <el-option label="星期四" value="4"></el-option>
            <el-option label="星期五" value="5"></el-option>
            <el-option label="星期六" value="6"></el-option>
            <el-option label="星期日" value="0"></el-option>
          </el-select>
      </el-form>

      <el-button type="primary" @click="openDialog" class="add-button">添加时间段</el-button>
    </div>


    <el-table
      :data="tableData"
      style="width: 100%"
      max-height="calc(100vh - 150px)"
      >
      <el-table-column
        fixed
        prop="day"
        label="星期"
        width="150">
      </el-table-column>
      <el-table-column
        prop="startTime"
        label="开始时间"
        width="120">
      </el-table-column>
      <el-table-column
        prop="endTime"
        label="结束时间"
        width="120">
      </el-table-column>
      <el-table-column
        prop="numOfPeople"
        label="人数"
        width="120">
      </el-table-column>
      <el-table-column
        label="操作"
        width="120">
        <template slot-scope="scope">
          <el-button @click.native.prevent="removeRow(scope.$index)" type="text" size="small">
            移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="5"
      layout="total, prev, pager, next"
      :total="total">
    </el-pagination>

    <el-dialog :visible.sync="dialogVisible" title="添加时间段" @close="closeDialog">
      <el-form ref="infoForm" :model="addTimeSlot" label-width="80px">
        <el-form-item label="星期">
          <el-select v-model="addTimeSlot.day" placeholder="请选择" >
            <el-option label="星期一" value="1"></el-option>
            <el-option label="星期二" value="2"></el-option>
            <el-option label="星期三" value="3"></el-option>
            <el-option label="星期四" value="4"></el-option>
            <el-option label="星期五" value="5"></el-option>
            <el-option label="星期六" value="6"></el-option>
            <el-option label="星期日" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker
            v-model="addTimeSlot.startTime"
            :picker-options="{
                selectableRange: '00:00:00 - 23:59:59'
                 }"
            :value-format="'HH:mm:ss'"
            placeholder="开始时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="addTimeSlot.endTime"
            :picker-options="{
                selectableRange: '00:00:00 - 23:59:59',
                }"
            :value-format="'HH:mm:ss'"
            placeholder="结束时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="人数" prop="numOfPeople">
          <el-input-number v-model="addTimeSlot.numOfPeople " :precision="0" :step="1" :min="0" :max="10000" >
          </el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="saveInfo">保存</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      bookingManagementForm: {
        day: '',
      },
      tableData: [],
      currentPage: 1,
      total: 20,

      dialogVisible: false,
      deleteDialogVisible:false,
      addTimeSlot: {
        day: '',
        startTime: '',
        endTime: '',
        numOfPeople: '',
      },
    }
  },
  created() {
    this.initializePage();
  },
  methods: {
    initializePage(){
      axios.get("http://localhost:8888/sys/venueVisitSchedule/schedules",
        {
          params:{
            page:1,
            pageSize:5,
            venueId: localStorage.getItem("venueId"),
          }
        }
      ).then(res =>{
          console.log(res);
          this.total= res.data.total;
          // 遍历 responseData.rows
          res.data.rows.forEach(item => {
            // 映射关系，1 对应 '周一'，2 对应 '周二'，以此类推
            const dayOfWeekMap = {
              1: '星期一',
              2: '星期二',
              3: '星期三',
              4: '星期四',
              5: '星期五',
              6: '星期六',
              0: '星期日'
            };
            // 将每个对象的属性映射到 tableData 的相应字段
            this.tableData.push({
              day: dayOfWeekMap[item.dateWeek],
              startTime: item.startTime,
              endTime: item.endTime,
              numOfPeople: item.maximumCapacity,
              venueVisitScheduleId: item.venueVisitScheduleId,
            });
          });
        }
      )
    },
    handleDayChange(newDay) {
      // 根据选择的星期值发起不同的后端请求
      switch (newDay) {
        case '1':
          this.sendRequestForDateWeek(1);
          break;
        case '2':
          this.sendRequestForDateWeek(2);
          break;
        case '3':
          this.sendRequestForDateWeek(3);
          break;
        case '4':
          this.sendRequestForDateWeek(4);
          break;
        case '5':
          this.sendRequestForDateWeek(5);
          break;
        case '6':
          this.sendRequestForDateWeek(6);
          break;
        case '0':
          this.sendRequestForDateWeek(0);
          break;
        default:
          break;
      }
    },
    sendRequestForDateWeek(day) {
      axios.get("http://localhost:8888/sys/venueVisitSchedule/allSchedulesByDateWeek",
        {
          params:{
            page:1,
            pageSize:5,
            venueId: localStorage.getItem("venueId"),
            dateWeek: day,
          }
        }
      ).then(res =>{
          this.tableData=[]; // 清空tableData数组
          console.log(res);
          this.total= res.data.total;
          // 遍历 responseData.rows
          res.data.rows.forEach(item => {
            // 映射关系，1 对应 '周一'，2 对应 '周二'，以此类推
            const dayOfWeekMap = {
              1: '星期一',
              2: '星期二',
              3: '星期三',
              4: '星期四',
              5: '星期五',
              6: '星期六',
              0: '星期日'
            };
            // 将每个对象的属性映射到 tableData 的相应字段
            this.tableData.push({
              day: dayOfWeekMap[item.dateWeek],
              startTime: item.startTime,
              endTime: item.endTime,
              numOfPeople: item.maximumCapacity,
              venueVisitScheduleId: item.venueVisitScheduleId,
            });
          });
        }
      )
    },
    onSubmit() {
      this.$message('submit!')
    },
    onCancel() {
      this.$message({
        message: 'cancel!',
        type: 'warning'
      })
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      console.log(this.bookingManagementForm.day);
      if(this.bookingManagementForm.day){
        axios.get("http://localhost:8888/sys/venueVisitSchedule/allSchedulesByDateWeek",
          {
            params:{
              page:val,
              pageSize:5,
              venueId: localStorage.getItem("venueId"),
              dateWeek: this.bookingManagementForm.day,
            }
          }
        ).then(res =>{
            this.tableData=[]; // 清空tableData数组
            console.log(res);
            this.total= res.data.total;
            // 遍历 responseData.rows
            res.data.rows.forEach(item => {
              // 映射关系，1 对应 '周一'，2 对应 '周二'，以此类推
              const dayOfWeekMap = {
                1: '星期一',
                2: '星期二',
                3: '星期三',
                4: '星期四',
                5: '星期五',
                6: '星期六',
                0: '星期日'
              };
              // 将每个对象的属性映射到 tableData 的相应字段
              this.tableData.push({
                day: dayOfWeekMap[item.dateWeek],
                startTime: item.startTime,
                endTime: item.endTime,
                numOfPeople: item.maximumCapacity,
                venueVisitScheduleId: item.venueVisitScheduleId,
              });
            });
          }
        )
      }else{
        axios.get("http://localhost:8888/sys/venueVisitSchedule/schedules",
          {
            params:{
              page:val,
              pageSize:5,
              venueId: localStorage.getItem("venueId"),
            }
          }
        ).then(res =>{
            this.tableData=[]; // 清空tableData数组
            console.log(res);
            this.total= res.data.total;
            // 遍历 responseData.rows
            res.data.rows.forEach(item => {
              // 映射关系，1 对应 '周一'，2 对应 '周二'，以此类推
              const dayOfWeekMap = {
                1: '星期一',
                2: '星期二',
                3: '星期三',
                4: '星期四',
                5: '星期五',
                6: '星期六',
                0: '星期日'
              };
              // 将每个对象的属性映射到 tableData 的相应字段
              this.tableData.push({
                day: dayOfWeekMap[item.dateWeek],
                startTime: item.startTime,
                endTime: item.endTime,
                numOfPeople: item.maximumCapacity,
                venueVisitScheduleId: item.venueVisitScheduleId,
              });
            });
          }
        )
      }
    },
    deleteRow(index, rows) {
      rows.splice(index, 1);
    },
    openDialog() {
      this.dialogVisible = true;
    },
    closeDialog() {
      this.dialogVisible = false;
    },
    saveInfo() {
      console.log(localStorage.getItem("venueId"), this.addTimeSlot.day, this.addTimeSlot.startTime, this.addTimeSlot.endTime, this.addTimeSlot.numOfPeople,)
      // 在这里处理保存信息的逻辑
      axios.get("http://localhost:8888/sys/venueVisitSchedule/insertVenueVisitSchedule",{
        params :{
          venueId: localStorage.getItem("venueId"),
          dateWeek: this.addTimeSlot.day,
          startTimeString: this.addTimeSlot.startTime,
          endTimeString: this.addTimeSlot.endTime,
          maximumCapacity: this.addTimeSlot.numOfPeople,
        }
        }
      ).then(res=>{
        console.log(res);
        if(res.data){
          this.$message({
            message: '添加成功',
            type: 'success'
          });
        }else{
          this.$message({
              message:'添加失败',
              type:'error'
            })
        }}
      )
      // 保存完成后关闭弹窗
      this.closeDialog();
    },
    removeRow(index) {
      this.$confirm('确认删除该时间段吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 发送请求到后端删除与行相关的数据
        axios.delete("http://localhost:8888/sys/venueVisitSchedule/deleteVenueVisitSchedule",{
          params: {
            venueVisitScheduleId: this.tableData[index].venueVisitScheduleId,
            venueId: localStorage.getItem("venueId"),
          }
          }
        ).then(response => {
            // 如果后端删除成功，则从tableData数组中删除该行
            if (response.data) {
              this.$message({
                type: 'success',
                message: '删除成功！'
              });
              this.tableData.splice(index, 1);
              this.tableData=[];
              this.initializePage();
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
    }
  }
}
</script>

<style scoped>

.header-container {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
}

.add-button {
  margin-right: 10px;
}

</style>

