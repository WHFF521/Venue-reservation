<template>
  <div class="app-container">
    <el-form ref="newActivityForm" :model="newActivityForm" label-width="120px">
      <el-form-item label="活动名称">
        <el-input v-model="newActivityForm.activityName" />
      </el-form-item>
      <el-form-item label="活动简介">
        <el-input
          type="textarea"
          placeholder="请输入活动简介"
          v-model="newActivityForm.introduction"
          maxlength="100"
          show-word-limit
        >
        </el-input>
      </el-form-item>
      <el-form-item label="活动地址">
        <el-input placeholder="请输入活动地址" v-model="newActivityForm.address" />
      </el-form-item>
      <el-form-item label="活动人数" prop="numOfPeople">
        <el-input-number v-model="newActivityForm.numOfPeople " :precision="0" :step="1" :min="0" :max="10000" >
        </el-input-number>
      </el-form-item>
      <el-form-item label="活动日期">
        <el-col :span="11">
          <el-date-picker v-model="newActivityForm.date" type="date" placeholder="选择活动日期" style="width: 100%;" />
        </el-col>
      </el-form-item>
      <el-form-item label="活动时间选择">
        <el-select v-model="newActivityForm.timeSelect" placeholder="请先选择活动日期" :disabled="isSelectTimeDisabled">
          <el-option v-for="timeOption in timeOptions" :key="timeOption.value" :label="timeOption.label" :value="timeOption.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="活动时间介绍">
        <el-input
          type="textarea"
          placeholder="输入活动时间介绍，样例：1月1日(9:00-11:00) "
          v-model="newActivityForm.time"
          maxlength="100"
          show-word-limit
        >
        </el-input>
      </el-form-item>
      <el-form-item label="活动须知">
        <el-input
          type="textarea"
          placeholder="请输入活动须知"
          v-model="newActivityForm.guideline"
          maxlength="100"
          show-word-limit
        >
        </el-input>
      </el-form-item>
      <el-form-item label="最早预约时间" prop="startTime">
        <el-date-picker
          v-model="newActivityForm.earliestBookingTime"
          type="datetime"
          placeholder="选择日期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="最晚预约时间" prop="endTime">
        <el-date-picker
          v-model="newActivityForm.latestBookingTime"
          type="datetime"
          placeholder="选择日期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="最晚取消时间">
        <el-input-number v-model="newActivityForm.latestCancelTime" :precision="0" :step="1" :max="10000" ></el-input-number>
        <span>小时前</span>
      </el-form-item>

      <el-form-item label="场馆图片">
        <el-upload
          class="upload-demo"
          action="http://localhost:8888/newPicture"
          :on-success="handleSuccess"
          :on-error="handleError"
          :file-list="fileList"
          list-type="picture"
          :limit="1"
          :http-request="handleUpload"
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="openDialog">保存</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="dialogVisible" title="是否保存修改" @close="closeDialog" width="30%" center>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="createNewActivity">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import axios from "axios";

export default {
  data() {
    return {
      newActivityForm: {
        activityName: '',
        timeSelect: '',
        introduction: '',
        guideline: '',
        address: '',
        numOfPeople: '',
        date: '',
        time: '',
        earliestBookingTime : '',
        latestBookingTime: '',
        latestCancelTime: '',

      },
      isSelectTimeDisabled:true,
      timeOptions: [],
      dialogVisible: false,
      fileList: [{
        name: '',
        url: ''
      },],
    }
  },

  watch: {
    'newActivityForm.date': function (newDate) {
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
  methods: {
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
    createNewActivity(){
      axios.get("http://localhost:8888/sys/activity/createActivity",{
        params:{
          actName:this.newActivityForm.activityName,
          actAddress:this.newActivityForm.address,
          actParticipantNum:this.newActivityForm.numOfPeople,
          actIntroduction:this.newActivityForm.introduction,
          actGuideline:this.newActivityForm.guideline,
          venueId:localStorage.getItem("venueId"),
          actCancelTime:this.newActivityForm.latestCancelTime,
          actDateString:this.newActivityForm.date,
          actTime:this.newActivityForm.time,
          venueVisitScheduleId:this.newActivityForm.timeSelect,
          actEarliestBookingTimeString:this.newActivityForm.earliestBookingTime,
          actLatestBookingtimeString:this.newActivityForm.latestBookingTime,
        }
      }).then(res =>{
        console.log(res);
        if(res.status===200){
          this.$message({
            message: '新建活动成功',
            type: 'success'
          });
        }else{
          this.$message({
            message:'新建活动失败',
            type:'error'
            })
        }
        })
      this.closeDialog();
    },
    openDialog() {
      this.dialogVisible = true;
    },
    closeDialog() {
      this.dialogVisible = false;
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
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleSuccess(response) {
      if (response.code === 200) {
        // 上传成功，更新 fileList 或进行其他操作
        this.$message.success('上传成功');
      } else {
        // 上传失败，可以进行错误处理
        this.$message.error('上传失败: ' + 'response:'+response.desc);
      }
    },
    handleError(error) {
      // 上传失败，可以进行错误处理
      this.$message.error('上传失败: ' + 'error:'+error.message);
    },
    handleUpload(data) {
      try {
        const file = data.file
        const formData = new FormData();
        formData.append('uploadFile', file);
        axios.post('http://localhost:8888/newPicture', formData)
          .then(response => {
            console.log(response.data)
            console.log(response.data.desc)
            this.handleSuccess(response.data);
            this.venueId = response.data.desc;
          })
          .catch(error => {
            this.handleError(error);
          });
      } catch (error) {
        this.handleError(error );
      }
    },
  }
}
</script>

<style scoped>
</style>

