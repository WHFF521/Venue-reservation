<template>
	<view>
		<view class="topcontainer">
		<view class="venue_name">
			<u--image :showLoading="true" :src="src" width="max-width" height="80px" mode="aspectFill" :fade="true" duration="450" @click="click"></u--image>
			<view class="nameText">
				<text>{{actName}}</text>
			</view>
		</view>
		<view class="dateAndTime">
		<u-cell-group>
				<u-cell title="时间:">
					<view slot="value" class="u-slot-value">
						<text>{{openTime}}</text>
					</view>
				</u-cell>
		</u-cell-group>
		</view>
		<view class="dateAndTime">
		<u-cell-group>
				<u-cell title="已预约人数:">
					<view slot="value" class="u-slot-value">
						<text>{{resedPeople}}</text>
					</view>
				</u-cell>
		</u-cell-group>
		</view>
		<view class="dateAndTime">
		<u-cell-group>
				<u-cell title="选择游客(最多三人)">
					<view slot="value" class="u-slot-value">
						<text>{{"已选 "+numPeople+" 人"}}</text>
						<u-icon name="arrow-right" @click="selectPeople"></u-icon>
					</view>
				</u-cell>
		</u-cell-group>
		</view>
		</view>
		<view class="under">
			<view class="save">
				<u-button shape="circle" type="primary" size="large" text="立即预约" color="#19be6b" @click="reservationCheck"></u-button>
			</view>
		</view>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		data() {
			return {
				resedPeople:'',
				openTime:'',
				calendarShow:false,
				timeShow:false,
				actName:'',
				actId:'',
				numPeople:0,
				dateSelected:'未选择',
				timeSelected:'未选择',
				selectWeek:0,
				src:'',
				timeColumns:[[]],
				// 存储转到选择人页面中选择的人的数据
				selectedPeople:[],
				vvsId:[],
				selectedvvsId:0,
			};
		},
		methods:{
			loadAct(){
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
						this.actName=res.data.actName
						this.openTime=res.data.actDate+" "+res.data.actTime;
						this.src=res.data.actPicture;
				  	}
				});
				uni.request({
				  	url:config.baseUrl+"/sys/activityReservation/getReservationCount",
					method:"GET",
				  	header:{
				  		token:uni.getStorageSync("token")
				  	},
					data:{
						activityId: this.actId
					},
				  	success: (res) => {
				 	 	console.log(res);
						this.resedPeople=res.data
				  	}
				})
			},
			selectPeople(){
				const self = this;
				uni.navigateTo({
						url:'/pages/select_people/select_people',
						events: {
						    // 为指定事件添加一个监听器，获取被打开页面传送到当前页面的数据
						    selectPeopleNum: function(data) {
						      console.log(data);
							  self.numPeople=data.num;
						    },
							PeopleInformation:function(data){
								self.selectedPeople=data;
							}
						}
				    })
			},
			reservationCheck(){
				const self = this;
				var num=0;
				if(self.selectedPeople.length==0){
					uni.showToast({
						title: "请选择游客",
						  icon: 'error', // 可选参数，图标，支持 success、loading、none
						  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
						  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
					})
					return
				}
				const requestPromises = self.selectedPeople.map(item => {
				    return new Promise((resolve, reject) => {
				      uni.request({
				        url: config.baseUrl+"/sys/activityReservation/insertReservation",
				        method: "GET",
				        header: {
				          token: uni.getStorageSync("token")
				        },
				        data: {
				          userMail: uni.getStorageSync("userMail"),
				          activityId: self.actId,
				          idCardNumber: item.idcard,
				        },
				        success: (res) => {
				          
				          console.log(res);
				          resolve(res);
						  if(res.data==='预约成功'){
							  num++;
						  				uni.showToast({
						  					title: '成功预约'+num+"人",
						  					  icon: 'success', // 可选参数，图标，支持 success、loading、none
						  					  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
						  					  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
						  				})
						  			}else{
						  				uni.showToast({
						  					title: '失败预约'+num+"人",
						  					  icon: 'error', // 可选参数，图标，支持 success、loading、none
						  					  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
						  					  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
						  				})
						  			}
				        },
				        fail: (error) => {
				          reject(error);
				        }
				      });
				    });
				  });
				
				  Promise.all(requestPromises)
				    .then((results) => {
				      console.log("num:" + num);
				      console.log("length:" + self.selectedPeople.length);
				
				      if (num == self.selectedPeople.length) {
				        uni.reLaunch({
				          url: "/pages/reservation_sucess/reservation_sucess"
				        });
				      }
				    })
				    .catch((error) => {
				      console.error(error);
				    });
				
			}
		},
		onLoad(e){
			this.actId=e.actId;
			this.loadAct();
		}
	}
</script>

<style lang="scss">
.venue_name{
	margin: 5px,5px,5px,5px;
	padding: 8px;
}
.nameText{
	margin: 5px,5px,5px,5px;
	text-align: left;
	font-size: 20px;
}
.u-slot-value{
	display: flex;
}
.dateAndTime{
	margin: 5px,5px,5px,5px;
	padding: 8px;
}
.topcontainer{
}
.footer {
	width: 100%;
	text-align: center;
	position:fixed;
	bottom: 1rem;;
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
