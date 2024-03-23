<template>
	<view class="user">
		
		<view class="user-activity-menu">
			<view class="menu-item" @click="changeUserAvatar()">
				<view class="left">
					<!-- <u-icon name="account" size="30" color="#FF5D3D" class="menu-icon"></u-icon> -->
					<!-- <image class="menu-icon" src="../../static/icon/icon_coupon.png"></image> -->
					<view class="menu-name">头像</view>
				</view>
				<view class="right">
					<u-avatar :src="userInfo.avg?userInfo.avg:user_avg" shape="square"></u-avatar>
					<u-icon name="arrow-right"></u-icon>
				</view>
				
			</view>
			<view class="menu-item" @click="goPage('change_name')">
				<view class="left">
					<!-- <u-icon name="list-dot" size="30" color="#02A7F0" class="menu-icon"></u-icon> -->
					<!-- <image class="menu-icon" src="../../static/icon/icon_order.png"></image> -->
					<view class="menu-name">昵称</view>
				</view>
				<view class="right">
					<view class="info">{{ userInfo.nickName?userInfo.nickName:user_name }}</view>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="menu-item" @click="goPage('change_phone')">
				<view class="left">
					<!-- <u-icon name="order" size="30" color="#F6A73E" class="menu-icon"></u-icon> -->
					<!-- <image class="menu-icon" src="../../static/icon/icon_userinfo.png"></image> -->
					<view class="menu-name">手机号</view>
				</view>
				<view class="right">
					<view class="info">{{ user_phone.replace(/^(.{3})(?:\d+)(.{4})$/, "$1****$2") }}</view>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view>
			<view class="menu-item" @click="mailShow()">
				<view class="left">
					<!-- <u-icon name="calendar" size="30" color="#8080FF" class="menu-icon"></u-icon> -->
					<!-- <image class="menu-icon" src="../../static/icon/icon_contact.png"></image> -->
					<view class="menu-name">邮箱</view>
				</view>
				<view class="right">
					<view class="info">{{user_mail}}</view>
					<u-icon name="arrow-right"></u-icon>
				</view>
				
			</view>
			
			
			<!-- <view class="menu-item">
				<view class="left">
					<image class="menu-icon" src="../../static/icon/icon_about.png"></image>
					<view class="menu-name">当前版本</view>
				</view>
				<view class="menu-number">{{ version }}</view>
			</view> -->
		</view>
		<!-- 列表菜单 - 结束 -->
		
		<view class="under">
			<view class="save">
				<u-button shape="circle" type="primary" size="large" text="退出登录" color="#dd6161" @click="logout"></u-button>
			</view>
		</view>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		data() {
			return {
				user_mail: uni.getStorageSync("userMail"),
				user_banner: '../../static/index_banner.png',
				user_avg: '',
				user_name: '',
				user_phone: '',
				userInfo: {}
			};
		},
		created() {
			
		},
		onShow() {
			
		},
		onLoad() {
			this.getuserinfo();
		},
		methods: {
			getuserinfo(){
				const self = this;
				uni.request({
					url:config.baseUrl+"/sys/user/getUserInfo",
					method:"GET",
					header:{
						token:uni.getStorageSync("token")
					},
					data:{
						userMail:uni.getStorageSync("userMail")
					},
					success: (res) => {
						console.log(res);
						self.user_name=res.data.nickName;
						self.user_avg=res.data.profilePicture;
						self.user_phone=res.data.phoneNumber;
						
					}
				})
			},
			logout(){
				uni.clearStorage({
				  success: function () {
				    console.log('清空成功');
				  }
				});
				uni.reLaunch({
					url:"/pages/login/login"
				})
			},
			/* 跳转页面 */
				goPage(pageName) {
					const self =this;
					switch (pageName) {
						case "change_name":
							uni.navigateTo({
								url: '/pages/mine/' + pageName,
								events: {
								    reload: function(data) {
								      console.log(data);
									  self.getuserinfo();
								    },
								}
							});break;
						case "change_phone":
							uni.navigateTo({
								url: '/pages/mine/' + pageName,
								events: {
								    reload: function(data) {
								      console.log(data);
									  self.getuserinfo();
								    },
								}
							});break;
						default:
							uni.navigateTo({
								url: '/pages/mine/' + pageName
							})
						break;
				}
			},
			mailShow(){
				uni.showToast({
					title:'暂不支持修改',
					icon: 'none'
				})
			},
			changeUserAvatar(){
				let _this = this;
				uni.chooseImage({
						count: 1, //默认9
						sizeType: ["original", "compressed"], //可以指定是原图还是压缩图，默认二者都有
						sourceType: ["album", "camera"], //从相册选择或者打开相机
						success: function (res) {
							console.log(res);
							if (res.tempFiles[0].size > 2 * 1024 * 1024) {
								// _this.$refs.uToast.show({
								// 	type: "error",
								// 	message: "图片大小不得超过2MB"
								// });
								uni.showToast({
									icon:'none',
									title:'图片大小不得超过2MB'
								})
							} else {
								uni.showToast({
									title:'成功'
								})
								uni.uploadFile({
									url: config.baseUrl+"sys/picture/uploadAvatar",
									filePath: res.tempFilePaths[0],
									name: "file",
									formData:{
										userMail: uni.getStorageSync("userMail")
									},
									success: uploadFileRes => {
			                            //console.log(uploadFileRes);
										_this.getuserinfo();
									}
								});
							}
						}
				});
			}
		}
	}
</script>

<style>
	page {
		background: #F4F4F4;
	}
</style>

<style lang="scss">
	.user {
		.user-header-image {
			width: 100%;
		}

		.user-info-box {
			background: #ffffff;
			padding: 30rpx;
			margin: -80rpx 20rpx 20rpx 20rpx;
			position: relative;
			border-radius: 20rpx;
			display: flex;
			align-items: center;

			.user-avg {
				width: 100rpx;
				height: 100rpx;
				border-radius: 10rpx;
			}

			
			.user-phone{
				margin-left: 20rpx;
				font-size: 24rpx;
				color: #666666;
			}
		}

		.user-recharge-wrapper {
			background: #ffffff;
			border-radius: 20rpx;
			padding: 30rpx;
			margin: 20rpx;
			
			.user-recharge-box {
				display: flex;
				align-items: center;
				justify-content: space-between;
				margin-bottom: 20rpx;

				.recharge-info {
					.info-title {
						color: #333;
						font-size: 28rpx;
						font-weight: bold;
					}

					.info-content {
						color: #999;
						font-size: 24rpx;
					}
				}

				.recharge-button {
					// background: #ff4131;
					color: #333;
					padding: 10rpx 20rpx;
					font-size: 22rpx;
					border-radius: 50rpx;
					flex-shrink: 0;
					border: 1rpx solid #dadbde;
				}
			}
			
			.recharge-user-money{
				display: flex;
				align-items: center;
				justify-content: space-between;
				margin-top: 20rpx;
				font-size: 26rpx;
				color: #333;
				
				.recharge-money{
					font-weight: bold;
					font-size: 30rpx;
				}
				
				.recharge-money::first-letter{
					font-size: 22rpx;
				}
			}
		}

		.user-activity-menu {
			padding: 30rpx;
			margin: 20rpx;
			border-radius: 20rpx;
			background: #fff;

			.menu-item {
				display: flex;
				align-items: center;
				justify-content: space-between;
				margin-top: 60rpx;

				.left {
					display: flex;
					align-items: center;

					.menu-icon {
						width: 40rpx;
						height: 40rpx;
					}


					.menu-name {
						font-size: 28rpx;
						margin-left: 20rpx;
						color: #333;
					}
				}
				.right{
					display: flex;
					align-items: center;
					
					.info {
						font-size: 28rpx;
						color: #767a82;
					}
				}
				.menu-number {
					font-size: 30rpx;
					color: #666666;
					letter-spacing: 2rpx;
				}
			}

			.menu-item:first-child {
				margin-top: 0;
			}
		}
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

</style>
