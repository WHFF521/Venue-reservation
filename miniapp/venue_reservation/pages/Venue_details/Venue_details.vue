<template>
	<view class="background">
		
		<u--image :showLoading="true" :src="src" width="max-width" height="180px" mode="widthFix" :fade="true" duration="450" @click="click"></u--image>
		
		<view>
			<view class="main-content">
				<u-cell-group>
						<u-cell >
							<text slot="title" class="u-slot-title">{{ venueName }}</text>
								<view slot="value" class="u-slot-value">
									<view style="margin-bottom: 10px;">
										<u-tag
											type="primary"
											text="简介" size="mini" 
											icon="list" 
											plain
											@click="briefOpen"></u-tag>
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
										<text style="font-size: 12px">{{venueMap}}</text>
									</view>
									
									<!-- <u-tag text="安徽省合肥市怀宁路87号" size="mini" icon="map" plain borderColor="white"></u-tag> -->
								</view>
								
						</u-cell>
					</u-cell-group>
			</view>
		</view>
		<!-- <view style="height: 23em;"></view> -->
		<view class="under">
			<view class="save">
				<u-button color="#19be6b" size="large"  @click="reservationNow" shape="circle" class="button">点击进行预约</u-button>
			</view>
		</view>
		

		<u-popup :show="noticeShow" mode="center"  @close="noticeClose" @open="noticeOpen">
			<view class="noticeView">
				<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-Y" @scrolltoupper="upper"
									@scrolltolower="lower" @scroll="scroll">
				<text>{{noticeText}}</text>
				</scroll-view>
				<u-button type="primary" size="large" text="大小尺寸" @click="noticeClose" color="#19be6b" shape="circle">我知道了</u-button>
			</view>
		</u-popup>
		<u-popup :show="briefShow" mode="bottom"  @close="briefClose" @open="briefOpen">
			<view class="briefView">
				<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-Y" @scrolltoupper="upper"
									@scrolltolower="lower" @scroll="scroll">
				<text>{{briefText}}</text>
				</scroll-view>
				<u-button type="primary" size="large" text="大小尺寸" @click="briefClose" color="#19be6b" shape="circle">我知道了</u-button>
			</view>
		</u-popup>
		
		<hothot
		title="热门活动" 
		:goodsArr="hotActList" 
		jumpurl="/pages/act_details/act_details?actId=" 
		moreurl="/pages/act_list/act_list">
		</hothot>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		data() {
			return {
				venueId:0,
				value6: 0,
				noticeShow: false,
				briefShow:false,
				openTime:'',
				venueName:'',
				venueMap:'',
				src:'',
				noticeText:'',
				briefText:'',
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				hotActList:[],
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
				  		url:'/pages/venue_reservation/venue_reservation?venueId='+this.venueId
				      })
			  },
			  loadVenue(){
				  console.log(this.venueId);
			  	 uni.request({
			  	  	url:config.baseUrl+"/sys/venue/VenueInfo",
			  		method:"GET",
			  	  	header:{
			  	  		token:uni.getStorageSync("token")
			  	  	},
					data:{
						venueId: this.venueId
					},
			  	  	success: (res) => {
			  	 	 	console.log(res);
			  			this.noticeText=res.data.guideLine;
						this.venueName=res.data.venueName;
						this.venueMap=res.data.venueAddress;
						this.openTime=res.data.openTime;
						this.src=res.data.picture;
						this.briefText=res.data.venueIntroduction;
			  	  	}
			  	});
				uni.request({
				  	url:config.baseUrl+"/sys/activity/getActivityByVenueId",
					method:"GET",
				  	header:{
				  		token:uni.getStorageSync("token")
				  	},
					data:{
						venueId: this.venueId
					},
				  	success: (res) => {
				 	 	res.data.forEach(item =>{
				 	 		this.hotActList.push({Id:item.actId,Picture:item.actPicture,Name:item.actName,Rating:item.rating})
				 	 	})
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
				this.venueId=e.venueId;
				this.loadVenue();
				
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
	
</style>
