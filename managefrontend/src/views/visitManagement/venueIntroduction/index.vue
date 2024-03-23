<template>
  <div class="app-container">
    <el-form ref="form" :model="venueIntroduction" label-width="120px">
      <el-form-item label="场馆名称">
        <el-input placeholder="请输入场馆名称" v-model="venueIntroduction.name" />
      </el-form-item>
      <el-form-item label="场馆地址">
        <el-input placeholder="请输入场馆地址" v-model="venueIntroduction.address" />
      </el-form-item>
      <el-form-item label="场馆电话">
        <el-input placeholder="请输入电话" v-model="venueIntroduction.phoneNumber" />
      </el-form-item>
      <el-form-item label="开放日">
        <el-checkbox-group v-model="openDays">
          <el-checkbox-button v-for="(day, index) in days" :label="index"  :key="day">{{day}}</el-checkbox-button>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="总体简介">
        <el-input
          type="textarea"
          placeholder="请输入场馆简介"
          v-model="venueIntroduction.introduction"
          maxlength="100"
          show-word-limit
        >
        </el-input>
      </el-form-item>
      <el-form-item label="开放时间">
        <el-input
          type="textarea"
          placeholder="填写用于显示给用户端的开放时间介绍"
          v-model="venueIntroduction.openTImeIntroduction"
          maxlength="100"
          show-word-limit
        >
        </el-input>
      </el-form-item>
      <el-form-item label="最晚取消时间">
        <el-input-number v-model="venueIntroduction.latestCancelTime" :precision="0" :step="1" :max="10000" ></el-input-number>
        <span>小时前</span>
      </el-form-item>
      <el-form-item label="最早预约天数">
        <el-input-number v-model="venueIntroduction.earliestBookingTime" :precision="0" :step="1" :max="10000" ></el-input-number>
        <span>天前</span>
      </el-form-item>
      <el-form-item label="场馆须知">
        <el-input
          type="textarea"
          placeholder="请输入场馆须知"
          v-model="venueIntroduction.guideline"
          maxlength="100"
          show-word-limit
        >
        </el-input>
      </el-form-item>
      <el-form-item label="场馆图片">
        <el-upload
          class="upload-demo"
          action="http://localhost:8888/uploadVenuePicture"
          :on-success="handleSuccess"
          :on-error="handleError"
          :file-list="fileList"
          list-type="picture"
          :limit='1'
          :http-request="handleUpload"
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="openDialog">更新</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>

    <el-dialog :visible.sync="dialogVisible" title="是否保存修改" @close="closeDialog" width="30%" center>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="updateInfo">保存</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import axios from "axios";

const dayOptions = ['周一', '周二', '周三', '周四','周五','周六','周日'];
export default {
  data() {
    return {
      venueIntroduction: {
        name: '',
        address: '',
        phoneNumber: '',
        introduction: '',
        openTImeIntroduction: '',
        latestCancelTime: '',
        earliestBookingTime: '',
        guideline: '',
        imageUrl: '',
        binaryString: '',
      },
      venueId: localStorage.getItem("venueId"),
      dialogVisible: false,
      openDays: [],
      days: dayOptions,
      fileList: [{
        name: '',
        url: ''
      },
      ],
    }
  },
  watch: {
    openDays() {
      // 在 openDays 数组发生变化时，更新 binaryString
      this.updateBinaryString();
    },
    binaryString() {
      this.updateOpenDays();
    }
  },
  created() {
    axios.get("http://localhost:8888/sys/venue/getById",
      {
        params: {
          venueId: localStorage.getItem("venueId"),
        }
      }).then(res => {
        console.log(res);
        this.venueIntroduction.name = res.data.venueName;
        this.venueIntroduction.address = res.data.venueAddress;
        this.venueIntroduction.phoneNumber = res.data.phoneNumber;
        this.venueIntroduction.introduction = res.data.venueIntroduction;
        this.venueIntroduction.openTImeIntroduction = res.data.openTime;
        this.venueIntroduction.latestCancelTime = res.data.cancelTime;
        this.venueIntroduction.earliestBookingTime = res.data.advanceBookday;
        this.venueIntroduction.guideline = res.data.guideLine;
      }
    )

  },
  methods: {
    // onSubmit() {
    //   this.$message('submit!')
    // },
    onCancel() {
      this.$message({
        message: 'cancel!',
        type: 'warning'
      })
    },
    updateBinaryString() {
      const binaryArray = new Array(7).fill(0); // 创建一个包含7个0的数组
      // 遍历选中的多选框值，将对应的二进制位设置为1
      this.openDays.forEach(day => {
        binaryArray[day] = 1;
      });
      this.venueIntroduction.binaryString = binaryArray.join(''); // 将数组转换为字符串
    },
    updateOpenDays() {
      // 将二进制字符串转换为数组，更新 selectedDays
      this.openDays = this.venueIntroduction.binaryString.split("").map(bit => (bit === "1" ? Number(bit) : undefined));
    },
    openDialog() {
      this.dialogVisible = true;
    },
    closeDialog() {
      this.dialogVisible = false;
    },
    updateInfo() {
      // 在这里处理保存信息的逻辑
      this.updateBinaryString();
      axios.put("http://localhost:8888/sys/venue/updateVenue",{
          venueId: localStorage.getItem("venueId"),
          venueName: this.venueIntroduction.name,
          venueAddress: this.venueIntroduction.address,
          phoneNumber: this.venueIntroduction.phoneNumber,
          openDays: this.venueIntroduction.binaryString,
          venueIntroduction: this.venueIntroduction.introduction,
          openTime: this.venueIntroduction.openTImeIntroduction,
          cancelTime: this.venueIntroduction.latestCancelTime,
          advanceBookday: this.venueIntroduction.earliestBookingTime,
          guideLine: this.venueIntroduction.guideline,
        }
      )
      console.log('保存信息');
      // 保存完成后关闭弹窗
      this.closeDialog();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleSuccess(response, file) {
      if (response.code === 200) {
        // 上传成功，更新 fileList 或进行其他操作
        this.$message.success('上传成功');
      } else {
        // 上传失败，可以进行错误处理
        this.$message.error('上传失败: ' + response.desc);
      }
    },
    handleError(error, file) {
      // 上传失败，可以进行错误处理
      this.$message.error('上传失败: ' + error.message);
    },
    handleUpload(data) {
      try {
        const file = data.file
        const formData = new FormData();
        formData.append('venueID',this.venueId)
        formData.append('uploadFile', file);
        axios.post('http://localhost:8888/uploadVenuePicture', formData)
          .then(response => {
            console.log(response.data);
            this.handleSuccess(response.data, file);
          })
          .catch(error => {
            this.handleError(error, file);
          });
      } catch (error) {
        this.handleError(error, file);
      }
    }
  },
}
</script>

<style scoped>

</style>

