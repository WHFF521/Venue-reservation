<template>
	<view>
		<view class="s-select">
			<u-cell-group>
					<u-cell v-for="(item, index) in checkboxList1" @click="handleItemClick(item)">
						<text slot="title" class="u-slot-title">{{item.name}}</text>
							<view slot="value" class="u-slot-value">
								<u-icon name="checkmark-circle" :color="item.iconColor"></u-icon>
							</view>
						
							<view slot="label"
								class="u-slot-label">
								<text style="font-size: 12px;">{{item.idcardFake}}</text>
							</view>
							
					</u-cell>
				</u-cell-group>
		</view>
		<view style="height:15px"></view>
		<view class="under">
			<view class="save">
				<u-button color="#19be6b" @click="goAdd" plain>新增游客</u-button>
			</view>
			<view class="save2">
				<u-button color="#19be6b" @click="finishSelect">确定</u-button>
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
				text1:'最多选择三人！',
				checkboxValue1:[],
				// 基本案列数据
				checkboxList1: [
				],
			};
		},
		methods: {
			 onPullDownRefresh() {
			      this.loadPeople();
			    },
		        checkboxChange(n) {
		            console.log('change', n);
		        },
				handleItemClick(item) {
					if(item.iconColor!='#5ac725')
				      {item.iconColor = '#5ac725';
				    }else {item.iconColor='#f4f4f5';}
				},
				goAdd(){
					const self = this;
					uni.navigateTo({
						url:"/pages/mine/add_people",
						events: {
						    // 为指定事件添加一个监听器，获取被打开页面传送到当前页面的数据
						    reload: function(data) {
						      console.log(data);
							  self.loadPeople();
						    },
						}
					})
				},
				loadPeople(){
					const self = this;
					this.userMail=uni.getStorageSync("userMail");
					uni.request({
					  url: config.baseUrl+"/sys/visitor/getVisitorByUserMail",
					  method: "GET",
					  header: {
					    token: uni.getStorageSync("token")
					  },
					  data: {
					    userMail:this.userMail
					  },
					  success: (res) => {
						  self.checkboxList1=[],
					    res.data.forEach(item =>{
							const temp={name:item.visitorName,idcard:item.idcardNumber,idcardFake:item.idcardNumber.replace(/^(\d{6})\d+(\d{4}[\dX])$/, "$1********$2"),iconColor:'#f4f4f5',disabled: false};
							self.checkboxList1.push(temp);
						})
					  },
					  fail: (error) => {
					    reject(error);
					  }
					});
				},
				finishSelect(){
					const selectedList= [];
					var numP=0;
					this.checkboxList1.forEach(item =>{
						if(item.iconColor==='#5ac725'){
							numP++;
							selectedList.push({name:item.name,idcard:item.idcard});
						}
					});
					if(numP>3) {
						uni.showToast({
							title: '选择超过了三人',
							  icon: 'error', // 可选参数，图标，支持 success、loading、none
							  duration: 2000, // 可选参数，持续时间（毫秒），默认为 1500
							  mask: false, // 可选参数，是否显示透明蒙层，防止触摸穿透，默认为 false
						})
					}else{
						  const eventChannel = this.getOpenerEventChannel();
						  eventChannel.emit('selectPeopleNum', {num: numP});
						  eventChannel.emit('PeopleInformation',selectedList);
						  uni.navigateBack({});
					}
				},
		},
		onLoad() {
			this.loadPeople();
		}
	}
</script>

<style lang="scss">
.s-select{
	display: flex;
	flex-direction: row;
	margin: 10px,10px,10px,10px;
	padding: 10px,10px,10px,10px;
}
.container {
  display: flex;
  flex-direction: column;
}
.under{
			display: flex;
			justify-content: space-between;
			// align-items: center;
			width: 100%;
			height: 150rpx;
			position: fixed;
			bottom: 0;
			.save{
				display: flex;
				margin: 10rpx 20rpx 20rpx 20rpx;
				width: 300rpx;
			}
			.save2{
				display: flex;
				margin: 10rpx 20rpx 20rpx 20rpx;
				width: 400rpx;
			}
		}
</style>
