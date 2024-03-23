<template>
	<view class="wrap">
		<view class="top"></view>
		<view class="content">
			<view class="title">
				<image src="../../static/ygstar_logo.png" style="height: 150px;" mode="aspectFit"></image>
			</view>
			<u--form>
				<u-form-item label="账号:">
					<u-input v-model="userMail" placeholder="请输入邮箱" />
				</u-form-item>
				<u-form-item label="密码:">
					<u-input type="password" v-model="password" placeholder="请输入密码" />
				</u-form-item>
			</u--form>
			<view class="alternative">
				<view class="password" @click="loginBy()">忘记密码</view>
				<!-- <view class="issue" @click="loginBy()">遇到问题</view> -->
			</view>
			<u-gap height="20"></u-gap>
			<u-button type="primary" shape="circle" @click="submit()" class="getSmsCode">登 录</u-button>
			<!-- <button @click="submit" class="getSmsCode">登录</button> -->
			<view class="register" @click="jumpRegister()">-注 册-</view>
		</view>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		data() {
			return {
				userMail: '',
				password: '',
			}
		},
		computed: {

		},
		onLoad() {

		},
		methods: {
			submit() {
				uni.request({
					url:config.baseUrl+"/sys/user/login",
					method:"GET",
					data:{
						userMail:this.userMail,
						password:this.password
					},
					success:(res)=>{
						console.log(res);
						if(res.data.status!=true){
							uni.showToast({
										title: '账号或密码错误',
										icon: 'none',
									})
						}else{
							// 后端生产的token，前端收到后保存在本地缓存里
							uni.setStorageSync("token",res.data.token);
							uni.setStorageSync("userMail",this.userMail);
							uni.showToast({
										title: '登录成功',
										icon: 'none',
									})
							uni.switchTab({
								url: '/pages/index/index'
							})
						}					
					},
					fail:(err)=>{	
					}
				});

					
			},
			loginBy(type) {
				this.$u.toast('开发中，敬请期待');
			},
			goPage(url) {
				this.$u.route({
					url: url
				})
			},
			jumpRegister(){
				uni.navigateTo({
					url: '/pages/login/register'
				});
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
			.register {
				color: $u-info;
				display: flex;
				justify-content: center;
				align-items: center;
				margin-top: 20rpx;
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

