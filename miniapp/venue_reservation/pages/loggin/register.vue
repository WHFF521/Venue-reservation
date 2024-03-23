<template>
	<view class="wrap">
		<view class="top"></view>
		<view class="content">
			<view class="title">
				<image src="../../static/ygstar_logo.png" style="height: 150px;" mode="aspectFit"></image>
			</view>
			<u--form>				
				<u-form-item label="邮箱:">
					<u-input v-model="userDTO.userMail" placeholder="请输入邮箱" />
				</u-form-item>
				<u-form-item label="电话:">
					<u-input v-model="userDTO.phoneNumber" placeholder="请输入电话号" />
				</u-form-item>
				<u-form-item label="密码:">
					<u-input type="password" v-model="userDTO.password" placeholder="请输入密码" />
				</u-form-item>
				<u-form-item label="确认密码:" label-width="70">
					<u-input type="password" v-model="userDTO.confirmKey" placeholder="请确认您的密码" />
				</u-form-item>
				<u-form-item label="验证码:" label-width="60">
					<u-input placeholder="请输入验证码" v-model="userDTO.emailCode">
							<template slot="suffix">
								<u-code
									ref="uCode"
									@change="codeChange"
									seconds="60"
									changeText="X秒重新获取"
								></u-code>
								<u-button
									@tap="getCode"
									:text="tips"
									type="success"
									size="mini"
								></u-button>
							</template>
					</u-input>
				</u-form-item>
			</u--form>
			<!-- <view class="alternative"> -->
				<!-- <view class="password" @click="loginBy()">忘记密码</view> -->
				<!-- <view class="issue" @click="loginBy()">遇到问题</view> -->
			<!-- </view> -->
			<u-gap height="20"></u-gap>
			<u-button type="primary" shape="circle" @click="submit()" class="getSmsCode">确认注册</u-button>
			<!-- <button @click="submit" class="getSmsCode">登录</button> -->
			<!-- <view class="register" @click="jumpRegister()">-注 册-</view> -->
		</view>
	</view>
</template>

<script>
import config from '@/config.js';
	export default {
		data() {
			return {
				userDTO: {
				  userMail: '',
				  phoneNumber: '',
				  password: '',
				  confirmKey: '',
				  emailCode: ''
				},
				
				tips: ''
			}
		},
		computed: {

		},
		onLoad() {

		},
		methods: {
			submit() {	
				var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
				var phoneRegex = /^\d{11}$/;
				if(!emailRegex.test(this.userDTO.userMail)) {
					uni.showToast({
						title: '请输入正确的邮箱',
						icon: 'none'
					})
				} else if(!phoneRegex.test(this.userDTO.phoneNumber)){
					uni.showToast({
						title: '请输入正确的电话号',
						icon: 'none'
					})
				} else if(this.userDTO.emailCode == null){
					uni.showToast({
						title: '请输入验证码',
						icon: 'none'
					})
				} else {
					uni.request({
						url: config.baseUrl+"sys/user/register",
						method:"POST",
						header:{
							'content-type':'application/json;charset:utf-8'
							},
						data:{
							userMail: this.userDTO.userMail,
							phoneNumber: this.userDTO.phoneNumber,
							password: this.userDTO.password,
							confirmKey: this.userDTO.confirmKey,
							emailCode: this.userDTO.emailCode
						},
						success:(res)=>{
							if(res.data.code == 200){
								uni.showToast({
									title: res.data.desc,
									icon:'none',
								}),
								uni.redirectTo({
									url:"/pages/loggin/loggin"
								})
							}else{
								uni.showToast({
									title: res.data.desc,
									icon:'none',
								})
							}
						}
					});
				}
				// if(this.userMail == '123' && this.password == '123'){
				// 	uni.showToast({
				// 				title: '登录成功',
				// 				icon: 'none',
				// 			})
				// }
				// else{
				// 	uni.showToast({
				// 				title: '账号或密码错误',
				// 				icon: 'none',
				// 			})
				// }
				
			},
			loginBy(type) {
				this.$u.toast('开发中，敬请期待');
			},
			goPage(url) {
				this.$u.route({
					url: url
				})
			},
			codeChange(text) {
			        this.tips = text;
			      },
      getCode() {
		  const self=this;
        if (this.$refs.uCode.canGetCode) {
          // 模拟向后端请求验证码
          uni.showLoading({
            title: '正在获取验证码'
          })
          setTimeout(() => {
            uni.hideLoading();
            // 这里此提示会被this.start()方法中的提示覆盖
            // uni.$u.toast('验证码已发送');
            // 通知验证码组件内部开始倒计时
            this.$refs.uCode.start();
          }, 2000);
          uni.request({
            url:config.baseUrl+"sys/user/sendEmail",
            method: 'GET',
			// header: {'content-type': 'application/json;charset:utf-8' },
            data: {
              userMail: self.userDTO.userMail
            },
            success:(res)=>{
              uni.showToast({
                title:  res.data.desc,
                icon: 'none',
              })
            }
			
          })
        } else {
          uni.$u.toast('倒计时结束后再发送');
        }
      },
      change(e) {
        console.log('change', e);
      }
		}
	};
</script>

<style lang="scss" scoped>
	.wrap {
		font-size: 28rpx;

		.content {
			width: 600rpx;
			margin: 0 auto;
			padding-top: 80rpx;

			.title {
				text-align: left;
				font-size: 60rpx;
				font-weight: 500;
				margin-bottom: 10rpx;
			}

			input {
				text-align: left;
				margin-bottom: 10rpx;
				padding-bottom: 6rpx;

			}

			.tips {
				color: $u-info;
				font-size: 20rpx;
				margin-bottom: 60rpx;
				margin-top: 30rpx;
			}

			.getSmsCode {
				background-color: $u-success;
				color: $u-info-light;
				border: none;
				font-size: 30rpx;
				padding: 12rpx 0;

				&::after {
					border: none;
				}
			}

			.alternative {
				color: $u-tips-color;
				display: flex;
				justify-content: flex-end;
				margin-top: 2rpx;
			}
		}

		.bottom {
			.loginType {
				display: flex;
				padding: 260rpx 150rpx 150rpx 150rpx;
				justify-content: space-between;

				.item {
					display: flex;
					flex-direction: column;
					align-items: center;
					color: $u-content-color;
					font-size: 28rpx;
				}
			}

			.hint {
				padding: 20rpx 40rpx;
				font-size: 20rpx;
				color: $u-tips-color;

				.link {
					color: $u-warning;
				}
			}
		}
	}
</style>

