<template>
	<view>
		<view class="topcontainer">
		<view class="venue_name">
			<u--image :showLoading="true" :src="src" width="max-width" height="80px" mode="aspectFill" :fade="true" duration="450" @click="click"></u--image>
			<view class="nameText">
				<text>{{venueName}}</text>
			</view>
		</view>
		<view class="dateAndTime">
		<u-cell-group>
				<u-cell title="选择日期">
					<view slot="value" class="u-slot-value">
						<text>{{dateSelected}}</text>
						<u-icon name="arrow-right" @click="calendarShow=true"></u-icon>
						<u-calendar :show="calendarShow" @confirm="calendarConfirm" @close="calendarClose" :monthNum=2></u-calendar>
					</view>
				</u-cell>
				<u-cell title="选择时间">
					<view slot="value" class="u-slot-value">
						<text>{{timeSelected}}</text>
						<u-icon name="arrow-right" @click="clickTimeShow"></u-icon>
						<u-picker :show="timeShow" :columns="timeColumns" @confirm="timeConfirm" @cancel="timeCancel"></u-picker>
						<!-- <u-calendar :show="calendarShow" @confirm="calendarConfirm" @close="calendarClose"></u-calendar> -->
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
				calendarShow:false,
				timeShow:false,
				venueName:'',
				venueId:'',
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
				openDays:"",
			};
		},
		methods:{
			loadVenue(){
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
						this.venueName=res.data.venueName
						this.openTime=res.data.openTime;
						this.src=res.data.picture;
						this.briefText=res.data.venueIntroduction;
						this.openDays=res.data.openDays;
						console.log("opendays:"+this.openDays);
				  	}
				})
			},
			calendarConfirm(e) {
				console.log(e);
				
				this.calendarShow=false;
				const dateParts = e[0].split("-");
				const year = parseInt(dateParts[0]);
				const month = parseInt(dateParts[1]) - 1; // 月份从 0 开始，所以需要减去 1
				const day = parseInt(dateParts[2]);
				const date = new Date(year, month, day);
				var dayOfWeek = date.getDay(); // 获取星期几，返回值为 0（星期日）到 6（星期六）
				if(dayOfWeek==0) dayOfWeek=7;
				// this.dateSelected=date;
				for (let i = 0; i < this.openDays.length; i++){
					const char = this.openDays[i];
					if(char=="0"){
						if(dayOfWeek==i+1){
							uni.showToast({
								title: "当天闭馆",
								  icon: 'error', // 可选参数，图标，支持 success、loading、none
								  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
								  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
							})
							return ;
						}
					}
				} 
				this.timeSelected="未选择";
				this.selectWeek=dayOfWeek;
				this.dateSelected=e[0];
			},
			timeConfirm(e) {
				console.log("timeConfirm",e);
				this.timeSelected=e.value[0];
				for (var i = 0; i < this.vvsId.length; i++) {
				  if (this.timeSelected ===this.vvsId[i].value ) {
				    this.selectedvvsId=this.vvsId[i].id;
					break;
				  }
				}
				this.timeShow=false;
			},
			timeCancel(){
				this.timeShow=false;
			},
			calendarClose(){
				this.calendarShow=false;
			},
			selectPeople(){
				if(this.timeSelected=="未选择"){
					uni.showToast({
						title: "请选择时间",
						  icon: 'error', // 可选参数，图标，支持 success、loading、none
						  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
						  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
					})
					return;
				}
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
								//console.log(self.selectedPeople);
							}
						}
				    })
			},
			reservationCheck(){
				const self = this;
				var num=0;
				if(self.dateSelected=="未选择"){
					uni.showToast({
						title: "请选择日期",
						  icon: 'error', // 可选参数，图标，支持 success、loading、none
						  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
						  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
					})
					return
				}
				if(self.timeSelected=="未选择"){
					uni.showToast({
						title: "请选择时间",
						  icon: 'error', // 可选参数，图标，支持 success、loading、none
						  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
						  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
					})
					return
				}
				if(self.selectedPeople.length==0){
					uni.showToast({
						title: "请选择游客",
						  icon: 'error', // 可选参数，图标，支持 success、loading、none
						  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
						  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
					})
					return
				}
				
				console.log("timeSelected:"+self.timeSelected);
				console.log("self.vvsId[self.timeSelected]:"+self.vvsId[self.timeSelected]);
				const requestPromises = self.selectedPeople.map(item => {
				    return new Promise((resolve, reject) => {
				      uni.request({
				        url: config.baseUrl+"/sys/venueReservation/insertVenueReservation",
				        method: "GET",
				        header: {
				          token: uni.getStorageSync("token")
				        },
				        data: {
				          userMail: uni.getStorageSync("userMail"),
				          venueId: self.venueId,
				          status: 0,
				          venueVisitScheduleId: self.selectedvvsId,
				          idCardNum: item.idcard,
				          booktime: this.dateSelected + " " + "00:00:00",
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
				
			},
			clickTimeShow(){
				
				if(this.dateSelected=='未选择'){
					uni.showToast({
						title: "请选择日期",
						  icon: 'error', // 可选参数，图标，支持 success、loading、none
						  duration: 1000, // 可选参数，持续时间（毫秒），默认为 1500
						  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
					})
					return;
				}
				uni.request({
				  	url:config.baseUrl+"/sys/venueVisitSchedule/getVVScheduleByVenueIdAndWeek",
					method:"GET",
				  	header:{
				  		token:uni.getStorageSync("token")
				  	},
					data:{
						venueId: this.venueId,
						week: this.selectWeek,
					},
				  	success: (res) => {
						this.timeColumns[0].splice(0, this.timeColumns[0].length);
						var num=0;
						Promise.all(res.data.map(item => {
						  console.log(item);
						  var numP = "初始值";
						  num++;
						  return this.NumPeople(item.venueVisitScheduleId, this.venueId)
						    .then(result => {
						      numP = "还剩" + (item.maximumCapacity - result) + "人";
						      const ti = item.startTime.replace(/:\d{2}$/, '') + "-" + item.endTime.replace(/:\d{2}$/, '') + " " + numP;
						      return { id: item.venueVisitScheduleId, value: ti };
						    })
						    .catch(error => {
						      console.error(error);
						      return null;
						    });
						}))
						.then(results => {
						  // 处理所有异步请求的结果
						  results.forEach(result => {
						    if (result) {
						      this.timeColumns[0].push(result.value);
						      this.vvsId.push(result);
						    }
						  });
						})
						.catch(error => {
						  console.error(error);
						});
				 	//  	res.data.forEach(item =>{
						// 	console.log(item);
						// 	var numP="初始值";
						// 	num++;
						// 	this.NumPeople(item.venueVisitScheduleId,this.venueId).then(result => {
						// 			numP="还剩"+(item.maximumCapacity-result)+"人";
						// 			// console.log(result); // 输出异步请求的结果
						// 			const ti=item.startTime.replace(/:\d{2}$/, '')+"-"+item.endTime.replace(/:\d{2}$/, '')+" "+numP;
						// 			this.timeColumns[0].push(ti);
						// 			this.vvsId.push({id:item.venueVisitScheduleId,value:ti});
						// 		  })
						// 		  .catch(error => {
						// 			console.error(error); // 处理错误情况
						// 		  });
							
						// })
						if(num==0)this.timeColumns[0].push("无时间可选择");
				  	}
				});
				this.timeShow=true;
			},
			NumPeople(visitId,venueId){
				 return new Promise((resolve, reject) => {
				    uni.request({
				      url: config.baseUrl+"/sys/venueReservation/getVRCByVeIdABTAVeVSI",
				      method: "GET",
				      header: {
				        token: uni.getStorageSync("token")
				      },
				      data: {
				        bookTime: this.dateSelected + " " + "00:00:00",
				        venueId: venueId,
				        venueVisitScheduleId: visitId,
				      },
				      success: (res) => {
				        const result = res.data;
				        resolve(result);
				      },
				      fail: (error) => {
				        reject(error);
				      }
				    });
				  });
			}
		},
		onLoad(e){
			this.venueId=e.venueId;
			this.loadVenue();
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
