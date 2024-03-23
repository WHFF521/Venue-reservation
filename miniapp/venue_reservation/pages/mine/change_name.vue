<template>
	<view>
		<view class="change">
			<view class="input">
				<u--form :model="form" ref="uForm">
					<u-form-item prop="name">
						<u-input v-model="form.new_nickName" maxlength="15" border="false" placeholder="请输入新昵称" :clearable="clear">	
							
						</u-input>
					</u-form-item>
				</u--form>
			</view>
			<view class="line">
				<u-line color="#5ac725" ></u-line>
			</view>
		</view>
		<!-- <view class="text">取个好名字吧！</view> -->
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
				form: {
					new_nickName: '',
				},
				// rule: {
				// 	name: [{
				// 		required: true
				// 	}]
				// },
				clear: true,
				
			}
		},
		
		methods: {
			submit(){
				if(this.form.new_nickName!=''){
					const self = this;
					uni.request({
					  	url:config.baseUrl+"/sys/user/updateUserName",
						method:"GET",
					  	header:{
					  		token:uni.getStorageSync("token")
					  	},
						data:{
							userMail:uni.getStorageSync("userMail"),
							newUserName:self.form.new_nickName
						},
					  	success: (res) => {
					 	 	console.log(res);
							const eventChannel = self.getOpenerEventChannel();
							eventChannel.emit('reload',"更改完毕");
							uni.navigateBack();
					  	}
					})
				} else{
					uni.showToast({
						title:'请输入',
						icon:'none'
					})
				}
			}
		},
		onReady() {
			// this.$refs.uForm.setRules(this.rule)
		}
	}
</script>

<style lang="scss">
	.change{
		padding-left: 30rpx;
		padding-right: 30rpx;
		.input{
			
		}
		.line{
			margin-top: -20rpx;
		}
		// .text{
		// 	margin-top: 10rpx;
		// }
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
			.saveButton{
				border: none;
			}
		}
	}
</style>
