<template>
  <div class="container">
    <el-form :model="activityDetailForm" label-width="100px">
      <el-form-item label="活动名称">
        <el-input v-model="activityDetailForm.activityName" />
      </el-form-item>
      <el-form-item label="活动简介">
        <el-input
          type="textarea"
          placeholder="请输入活动简介"
          v-model="activityDetailForm.introduction"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="活动地址">
        <el-input placeholder="请输入活动地址" v-model="activityDetailForm.address" />
      </el-form-item>
      <el-form-item label="活动人数" prop="numOfPeople">
        <el-input-number v-model="activityDetailForm.numOfPeople" :precision="0" :step="1" :min="0" :max="10000" />
      </el-form-item>
      <el-form-item label="活动日期">
        <el-col :span="11">
          <el-date-picker v-model="activityDetailForm.date" type="date" placeholder="选择活动日期" style="width: 100%;" />
        </el-col>
      </el-form-item>
      <el-form-item label="活动时间选择">
        <el-select v-model="activityDetailForm.timeSelect" placeholder="请先选择活动日期" :disabled="isSelectTimeDisabled">
          <el-option v-for="timeOption in timeOptions" :key="timeOption.value" :label="timeOption.label" :value="timeOption.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="活动时间介绍">
        <el-input
          type="textarea"
          placeholder="输入活动时间介绍，同于展示于用户端"
          v-model="activityDetailForm.time"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="活动须知">
        <el-input
          type="textarea"
          placeholder="请输入活动须知"
          v-model="activityDetailForm.guideline"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="最早预约时间" prop="startTime">
        <el-date-picker
          v-model="activityDetailForm.earliestBookingTime"
          type="datetime"
          placeholder="选择日期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="最晚预约时间" prop="endTime">
        <el-date-picker
          v-model="activityDetailForm.latestBookingTime"
          type="datetime"
          placeholder="选择日期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="最晚取消时间">
        <el-input-number v-model="activityDetailForm.latestCancelTime" :precision="0" :step="1" :max="10000" />
        <span>小时前</span>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  created() {
    axios.get("http://localhost:8888/sys/activity/selectActByActId",{
        params:{
          actId:this.actId,
        }
      }
    ).then(res =>{
        console.log(res)
        this.activityDetailForm.activityName=res.data.actName;
        this.activityDetailForm.introduction=res.data.actIntroduction;
        this.activityDetailForm.address =res.data.actAddress;
        this.activityDetailForm.numOfPeople=res.data.actParticipantNum;
        this.activityDetailForm.guideline=res.data.actGuideline;
        this.activityDetailForm.latestCancelTime=res.data.actCancelTime;
        this.activityDetailForm.date=res.data.actDate;
        this.activityDetailForm.time=res.data.actTime;
        this.activityDetailForm.timeSelect=res.data.venueVisitScheduleId;
        this.activityDetailForm.earliestBookingTime=res.data.actEarliestBookingTime;
        this.activityDetailForm.latestBookingTime=res.data.actLatestBookingtime;
      }
    )
  },
  props:['actId'],
  data() {
    return {
      activityDetailForm: {
        activityName: '',
        introduction: '',
        timeSelect: '',
        guideline: '',
        address: '',
        numOfPeople: '',
        date: '',
        time: '',
        earliestBookingTime: '',
        latestBookingTime: '',
        latestCancelTime: '',
      },
      isSelectTimeDisabled:true,
      timeOptions: [],
      dialogVisible: false,
      fileList: [{
        name: 'food.jpeg',
        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
      },
      ],
    };
  },
  watch: {
    'activityDetailForm.date': function (newDate) {
      // 检查是否选择了有效的日期
      if (newDate) {
        this.isSelectTimeDisabled = false; // 启用时间选择

        // 获取星期几 (0 = 星期天, 1 = 星期一, ..., 6 = 星期六)
        let dayOfWeek = new Date(newDate).getDay();
        // 向后端发送请求根据星期几获取信息
        this.fetchTimeOptionsBasedOnDayOfWeek(dayOfWeek);
      }
    },
  },
  methods:{
    fetchTimeOptionsBasedOnDayOfWeek(dayOfWeek) {
      this.timeOptions=[];
      axios.get("http://localhost:8888/sys/venueVisitSchedule/allSchedulesByDateWeekForNewActivity",{
          params:{
            venueId:localStorage.getItem("venueId"),
            dateWeek:dayOfWeek,
          }
        }
      ).then(res =>{
        console.log(res);
        res.data.forEach(item =>{
            this.timeOptions.push({
                label:item.startTime+"-"+item.endTime,
                value:item.venueVisitScheduleId,
              }
            )
          }
        )
      })
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
  }
};
</script>

<style>
.container {
  max-width: 600px; /* 设置内容的最大宽度 */
  margin: 0 auto; /* 将内容居中显示 */
  padding: 20px; /* 添加内边距，留出四周空白 */
}
</style>
