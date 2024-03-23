<template>
	<view>
		<u-sticky bg-color="#fff" style="width: 100%;">
			<u-tabs :list="list1" @click="tabClick" itemStyle="height: 44px; width:19%;" scrollable="false"></u-tabs>
		</u-sticky>
		<view class="reservation-list">
			<u-list @scrolltolower="scrolltolower">
				<u-list-item v-for="(item, index) in itemList" :key="index">
					<view class="reservation">
						<view class="res-venueName">
							<text>{{item.venueName}}</text>
						</view>
						<view class="res-description">
							<view class="description-text">
								<text>预约人：{{item.visitorName}}\n身份证：{{item.idCardNumber.replace(/^(.{8})(?:\d+)(.{4})$/, "$1******$2")}}\n预约时间：{{item.venueTime}}</text>
							</view>
							<view class="description-button">
								<view v-show="showButtonScore" class="current-button">
									<u-button size="small" shape="circle" color="#ffaa00" @click="openPop(item.venueId,item.orderNumber)">
										评价
									</u-button>
								</view>
								
								<view v-show="showButtonCancel" class="current-button">
									<u-button size="small" shape="circle" color="#c8c9cc" @click="confirmCancel(item.orderNumber)">
										取消预约
									</u-button>
								</view>
								
								<view v-show="showButtonCancel" class="current-button">
									<u-button size="small" shape="circle" color="#19be6b" @click="visitVenue(item.orderNumber)">
										查看二维码
									</u-button>
								</view>
							</view>
						</view>
					</view>
					<!-- <u-cell :title="`列表长度-${index + 1}`"> -->
						<!-- <u-avatar
							slot="icon"
							shape="square"
							size="35"
							:src="item.url"
							customStyle="margin: -3px 5px -3px 0"
						></u-avatar> -->
					<!-- </u-cell> -->
				</u-list-item>
			</u-list>
		</view>
		<view class="pop-up">
			<u-popup :show="showPop" :round="10" @close="closePop">
				<view class="rate">
					<u-rate v-model="rateValue" :minCount="1" size="36" allowHalf="true" :readonly="!showConfirm"></u-rate>
				</view>
				<view class="rate-button" v-show="showConfirm">
					<u-button text="确定评分" shape="circle" color="#19be6b" @click="scoreVenue"></u-button>
				</view>
			</u-popup>
		</view>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		data() {
			return {
                list1: [{
                    name: '待使用',
                }, {
                    name: '已参观',
                }, {
                    name: '已取消'
                }, {
                    name: '已过期'
                }],
				indexList: [
					{
						venueName: "3D展览",
						time: "2024-01-12(09:00-12:00)",
						name: "蔡登浪",
						idCard:"350824000000004972"
					},
					{
						venueName: "3D展览",
						time: "2024-01-12(09:00-12:00)",
						name: "蔡登浪",
						idCard:"350824000000004972"
					},
					{
						venueName: "3D展览",
						time: "2024-01-12(09:00-12:00)",
						name: "蔡登浪",
						idCard:"350824000000004972"
					}
				],
				itemList: [],
				showButtonCancel: true,
				showButtonScore: false,
				showPop: false,
				showConfirm: true,
				rateValue: 0,
				currentVenID: 0,
				currentOrderNumber: 0
			}
		},
		methods: {
             tabClick(item) {
				
                //console.log('item', item.index);
				if(item.index!=0){
					this.showButtonCancel=false;
				}else{
					this.showButtonCancel=true;
				}
				if(item.index==1){
					this.showButtonScore=true;
				}else{
					this.showButtonScore=false;
				}
				this.getList(item.index)
            },
			getList(index) {
				uni.request({
					url: config.baseUrl+"sys/venueReservation/getVenueReservations",
					method: "GET",
					header: {
						token:uni.getStorageSync("token")
					},
					data: {
						status: index,
						userMail: uni.getStorageSync("userMail")
					},
					success: (res) => {
						//console.log(res.data.data)
						this.itemList = res.data.data
					}
				})
			},
			getStatus(ordernum){
				uni.request({
					url: config.baseUrl+"sys/venueReservation/getStatusAndScore",
					method:"GET",
					header:{
						token:uni.getStorageSync("token")
					},
					data:{
						orderNumber: ordernum
					},
					success:(res)=>{
						if(res.data.data.status==4){
							this.showConfirm = false;
							this.rateValue = res.data.data.score;
						}else if(res.data.data.status==1){
							this.showConfirm = true;
							this.rateValue = 0;
						}
					}
				})
			},
			closePop() {
				this.showPop = false
				
			},
			async openPop(id,ordernum) {
				//打开评分栏函数
				this.currentVenID = id;
				this.currentOrderNumber = ordernum;
				await this.getStatus(ordernum);
				this.showPop = true;
			},
			scoreVenue(){
				//评分函数
				const self = this;
				uni.showModal({
						title: '确认评分',
						content: '评分后不可更改',
						success: function(res) {
						if (res.confirm) {
							uni.request({
								url: config.baseUrl+"sys/venueReservation/scoreVenue",
								method:"GET",
								header: {
									token:uni.getStorageSync("token")
								},
								data: {
									orderNumber: self.currentOrderNumber,
									venueID: self.currentVenID ,
									score: self.rateValue
								},
								success:(res)=> {
									uni.showToast({
										title: "评分成功",
										icon: "none"
									});
									self.closePop();
								}
							})
						} else {
							console.log('点击了取消')
						}
					}
				})
			},
			confirmCancel(orderNumber){
				const self = this;
				uni.request({
					url:config.baseUrl+"sys/venueReservation/isDefaultCancel",
					method:"GET",
					header:{
						token:uni.getStorageSync("token")
					},
					data:{
						orderNumber: orderNumber
					},
					success:(result)=>{
						if(result.data.data){
							uni.showModal({
								title:"警告",
								content:'您本次取消为违约取消，短时间内过多的违约取消会导致预约封禁',
								success: function(res) {
									if(res.confirm){
										uni.request({
											url:config.baseUrl+"sys/venueReservation/cancelDefault",
											method:"GET",
											header:{
												token:uni.getStorageSync("token")
											},
											data:{
												orderNumber: orderNumber,
												userMail: uni.getStorageSync("userMail")
											},
											success:(cancelRes)=>{
												if(cancelRes.data.code==200){
													if(cancelRes.data.data){
														uni.showToast({
															title: '取消成功，但因取消次数过多暂时封禁预约',
															icon:'none',
															duration:3000
														});
													}
													else{
														uni.showToast({
															title: '取消成功',
															icon:'success'
														});
													}
												}
												else{
													uni.showToast({
														title: '服务器错误',
														icon:'fail',
													});
												}
												self.getList(0);
											}
											
										})
									}
								}
							})
						} else {
							uni.showModal({
								title: "确认取消",
								content:'您确定取消本次预约码',
								success: function(res) {
									if(res.confirm){
										uni.request({
											url:config.baseUrl+"sys/venueReservation/cancelNotDefault",
											method:"GET",
											header:{
												token:uni.getStorageSync("token")
											},
											data:{
												orderNumber: orderNumber
											},
											success:(cancelResult) => {
												if(cancelResult){
													uni.showToast({
														title: '取消成功',
														icon:'success'
													});
												} else{
													uni.showToast({
														title: '服务器错误',
														icon:'fail'
													});
												}
												self.getList(0);
											}
										})
									}
								}
							})
						}
					}
				})
			},
			visitVenue(orderNumber){
				uni.request({
					url:config.baseUrl+"sys/venueReservation/visitVenue",
					method:"GET",
					header:{
						token:uni.getStorageSync("token")
					},
					data:{
						orderNumber: orderNumber
					},
					success:(res)=>{
						if(res.data.data){
							uni.showToast({
								title: "参观成功",
								icon: "none"
							});
						}else{
							uni.showToast({
								title: "服务器错误",
								icon: "fail"
							});
						}
						this.getList(0);
					}
				})
			}	
		},
		onLoad(){
			this.getList(0);
		}
	}
</script>

<style>
	page {
		background: #F4F4F4;
	}
</style>

<style lang="scss" scoped>
	.reservation-list{
		.reservation{
			border-radius: 20rpx;
			background: #fff;
			margin: 30rpx 40rpx 0rpx 40rpx;
			box-shadow: 0 0 5px 1px #eaeaeb;
			.res-venueName{
				font-weight: 600;
				font-size: 36rpx;
				padding:20rpx;
				border-radius: 20rpx 20rpx 0 0;
				background: #eaeaeb;
			}
			.res-description{
				padding: 10rpx 20rpx 10rpx 20rpx;
				margin-top: 20rpx;
				background: #fff;
				border-radius:0 0 20rpx 20rpx;
				.description-text{
					font-size: 24rpx;
					font-weight: 300;
				}
				.description-button{
					display: flex;
					justify-content: flex-end;
						
					.current-button{
						padding: 20rpx 20rpx 10rpx 20rpx;
					}
				}
			}
		}
	}
	.pop-up{
		.rate{
			display: flex;
			justify-content: center;
			margin: 20rpx;
		}
		.rate-button{
			margin-top: 10rpx;
			padding:0 60rpx 0 60rpx;
		}
	}

</style>
