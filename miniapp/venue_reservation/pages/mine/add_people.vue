<template>
	<view class="add-people">
		<view class="form-field">
			<u-form :model="form" ref="uForm" error-type="toast">
				<u-form-item label="您的姓名:" prop="name" label-width="140rpx" :border-bottom="border_form">
					<u-input v-model="form.name" border="false" placeholder="请输入您的姓名"/>
				</u-form-item>
				<u-form-item label="身份证号:" prop="id" label-width="140rpx">
					<u-input v-model="form.id" border="false" placeholder="请输入您的身份证号" />
				</u-form-item>
			</u-form>
		</view>
		
		<view class="under">
			<view class="save">
				<u-button color="#19be6b" @click="submit">保   存</u-button>
			</view>
		</view>
	</view>
</template>

<script>
import config from '@/config.js';
	export default {
		data() {
			return {
				userMail:"",
				border_input: false,
				border_form:true,
				form: {
					name: '',
					id: '',
				},
				rules: {
						name: [
							{
								required: true,
								message: '请输入姓名',
								trigger: ['blur,change']
							}
						],
						id: [
							{
								required: true,
								message: '请输入身份证号',
								trigger: ['blur,change']
							},
							{
								pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
								transform(value) {
									return String(value)
								},
								message: '身份证格式错误',
								trigger: ['blur,change']
							}
						]
					}
			}
		},
		methods: {
			submit() {
				console.log("submit");
				const self=this;
						this.$refs.uForm.validate().then(res => {
								console.log('验证通过');
								this.userMail=uni.getStorageSync("userMail");
								uni.request({
								  	url:config.baseUrl+"/sys/visitor/insertVisitor",
									method:"GET",
								  	header:{
								  		token:uni.getStorageSync("token")
								  	},
									data:{
										userMail:self.userMail,
										visitorName:self.form.name,
										idCardNumber:self.form.id,
									},
								  	success: (res) => {
										console.log('插入成功');
										const eventChannel = self.getOpenerEventChannel();
										eventChannel.emit('reload',"添加完毕");
										uni.navigateBack();
								  	},
								})
							}).catch(errors=>{
								uni.$u.toast("校验失败")
							})
						},
		},
		onReady() {
			this.$refs.uForm.setRules(this.rules);
		}
	}
</script>

<style>
	page {
		background: #F4F4F4;
	}
</style>

<style lang='scss' scoped>
	.add-people{
		.form-field{
			padding: 10rpx 30rpx 10rpx 30rpx;
			margin: 20rpx;
			border-radius: 20rpx;
			background: #ffffff;
			
		}
		.under{
			display: flex;
			justify-content: center;
			align-items: center;
			width: 100%;
			height: 150rpx;
			background-color: #ffffff;
			position: fixed;
			bottom: 0;
			.save{
				margin: 10rpx 20rpx 20rpx 20rpx;
				width: 650rpx;
				
				
			}
		}
	}
	
</style>
