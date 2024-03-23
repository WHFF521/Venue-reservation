<template>
	<view class="background">
		
		<u--image :showLoading="true" :src="src" width="max-width" height="180px" mode="widthFix" :fade="true" duration="450" @click="click"></u--image>
		
		<view>
			<view class="main-content">
				<u-cell-group>
						<u-cell >
							<text slot="title" class="u-slot-title">{{ actName }}</text>
								<view slot="value" class="u-slot-value">
									<view style="margin-bottom: 10px;">
										<!-- <u-tag
											type="primary"
											text="简介" size="mini" 
											icon="list" 
											plain
											@click="briefOpen"></u-tag> -->
									</view>
									<u-tag
										type="success"
										text="须知" size="mini" 
										icon="tags" 
										plain
										@click="noticeOpen"></u-tag>
								</view>
							
								<view slot="label"
									class="u-slot-label">
									<text class="opentime">{{openTime}}</text>
									<view class="iconAndMap">
										<u-icon name="map" color="#3c9cff"></u-icon>
										<text style="font-size: 12px">{{actMap}}</text>
									</view>
								</view>
								
						</u-cell>
					</u-cell-group>
			</view>
		</view>
			<view>
				<!-- <u--text text="简介:" bold size="18"></u--text> -->
				<u-divider text="简介"></u-divider>
			</view>
			<view class="text-box" scroll-y="true">
				<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-Youtside" @scrolltoupper="upper"
									@scrolltolower="lower" @scroll="scroll">
				<text>{{briefText}}</text>
				</scroll-view>	
			</view>
		<!-- <view style="height: 23em;"></view> -->
		<view class="under">
			<view class="save">
				<u-button color="#19be6b" size="large"  @click="reservationNow" shape="circle" class="button" :text="btText" :disabled="btDis"></u-button>
			</view>
		</view>
		

		<u-popup :show="noticeShow" mode="center"  @close="noticeClose" @open="noticeOpen">
			<view class="noticeView">
				<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-Y" @scrolltoupper="upper"
									@scrolltolower="lower" @scroll="scroll">
				<text>{{noticeText}}</text>
				</scroll-view>
				<u-button type="primary" size="large" text="大小尺寸" @click="noticeClose"  color="#19be6b" shape="circle">我知道了</u-button>
			</view>
		</u-popup>
		<u-popup :show="briefShow" mode="bottom"  @close="briefClose" @open="briefOpen">
			<view class="briefView">
				<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-Y" @scrolltoupper="upper"
									@scrolltolower="lower" @scroll="scroll">
				<text>{{briefText}}</text>
				</scroll-view>
				<u-button type="primary" size="large" text="大小尺寸" @click="briefClose" color="#19be6b" shape="circle" >我知道了</u-button>
			</view>
		</u-popup>
		
	
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		data() {
			return {
				actId:0,
				value6: 0,
				noticeShow: false,
				briefShow:false,
				openTime:'',
				actName:'',
				actMap:'',
				src:'',
				noticeText:'',
				briefText:'',
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				btText:"",
				btDis:false,
			};
		},
		methods: {
		      noticeOpen() {
				  this.noticeShow = true
		        // console.log('open');
		      },
		      noticeClose() {
		        this.noticeShow = false
		        // console.log('close');
		      },
			  briefOpen() {
			  	this.briefShow = true
			    // console.log('open');
			  },
			  briefClose() {
			    this.briefShow = false
			    // console.log('close');
			  },
			  reservationNow(){
				  uni.navigateTo({
				  		url:'/pages/act_reservation/act_reservation?actId='+this.actId
				      })
			  },
			  loadAct(){
				  const self = this;
			  	 uni.request({
			  	  	url:config.baseUrl+"/sys/activity/actinfo",
			  		method:"GET",
			  	  	header:{
			  	  		token:uni.getStorageSync("token")
			  	  	},
					data:{
						activityId: this.actId
					},
			  	  	success: (res) => {
			  	 	 	console.log(res);
			  			this.noticeText=res.data.actGuideline;
						this.actName=res.data.actName;
						this.actMap=res.data.actAddress;
						this.openTime=res.data.actDate+" "+res.data.actTime;
						this.src=res.data.actPicture;
						this.briefText=res.data.actIntroduction;
						const date = new Date(res.data.actLatestBookingtime);
						const currentTime = new Date();
						console.log("lasteddate:"+date);
						if(date>currentTime){
							self.btText="点击进行预约";
							self.btDis=false;
						}else 
						{
							self.btText="预约已结束";
							self.btDis=true;
						}
						
			  	  	}
			  	})
			  },
			  upper: function(e) {
			  				console.log(e)
			  			},
			  			lower: function(e) {
			  				console.log(e)
			  			},
			  			scroll: function(e) {
			  				console.log(e)
			  				this.old.scrollTop = e.detail.scrollTop
			  			},
			  			goTop: function(e) {
			  				// 解决view层不同步的问题
			  				this.scrollTop = this.old.scrollTop
			  				this.$nextTick(function() {
			  					this.scrollTop = 0
			  				});
			  				uni.showToast({
			  					icon: "none",
			  					title: "纵向滚动 scrollTop 值已被修改为 0"
			  				})
			  			}
		    },
			onLoad(e){
				this.actId=e.actId;
				this.loadAct();
				
			}
	}
</script>

<style lang="scss">
	.background{
		
		.main-content{
			margin: 10px 10px 10px 10px;
			background-color: white;
		}
		.u-slot-title{
			font-size: 20px;
		}
		.iconAndMap{
			display: flex;
		}
		.opentime{
			margin-left: 5px;
			font-size: 12px;
		}
		.noticeView{
			margin: 10px 10px 10px 10px;
			width:300px;
			height: auto;
		}
		.briefView{
			padding: 10px;
		}
		.bottom{
			width: 100%;
			position:fixed;
			bottom: 1rem;
			
			.button{
				margin: 10rpx;
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
						
						.saveButton{
							background-color: $u-success;
							color: $u-info-light;
							border: none;
							font-size: 36rpx;
						}
					}
				}

	}
	.scroll-Y {
			height: 600rpx;
		}
		.scroll-view_H {
			white-space: nowrap;
			width: 100%;
		}
		.scroll-view-item {
			height: 300rpx;
			line-height: 300rpx;
			text-align: center;
			font-size: 36rpx;
		}
		.scroll-view-item_H {
			display: inline-block;
			width: 100%;
			height: 300rpx;
			line-height: 300rpx;
			text-align: center;
			font-size: 36rpx;
		}
	.scroll-Youtside {
			height: 600rpx;
		}
		.scroll-view_H {
			white-space: nowrap;
			width: 100%;
		}
		.scroll-view-item {
			height: 300rpx;
			line-height: 300rpx;
			text-align: center;
			font-size: 36rpx;
		}
		.scroll-view-item_H {
			display: inline-block;
			width: 100%;
			height: 300rpx;
			line-height: 300rpx;
			text-align: center;
			font-size: 36rpx;
		}
	.text-box {
		margin-bottom: 40rpx;
		padding: 20rpx;
		display: flex;
		min-height: 300rpx;
		background-color: #FFFFFF;
		justify-content: center;
		align-items: center;
		text-align: left;
		font-size: 30rpx;
		color: #353535;
		line-height: 1.8;
	}
</style>
