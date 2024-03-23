<template>
	<view class="myPeople">
		<people_list :allPeople="peoList" @customEvent="handleCustomEvent"></people_list>
		
		<view class="under">
			<view class="save">
				<u-button color="#19be6b" @click="goAdd">新增游客</u-button>
			</view>
		</view>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		data() {
			return {
				peoList:[],
				}
		},
		methods: {
			onPullDownRefresh() {
			     this.getPeople();
			   },
				goAdd(){
					const self = this;
					uni.navigateTo({
						url: '/pages/mine/add_people',
						events: {
						    // 为指定事件添加一个监听器，获取被打开页面传送到当前页面的数据
						    reload: function(data) {
						      console.log(data);
							  self.getPeople();
						    },
						}
					})
				},
				getPeople(){
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
						  self.peoList=[];
					    res.data.forEach(item =>{
							const temp={id:item.idcardNumber,textName:item.visitorName+"\n",textId:"身份证："+item.idcardNumber.replace(/^(\d{6})\d+(\d{4}[\dX])$/, "$1********$2"),disabled: false,options: [{text: '删除',
						style: {
                            backgroundColor: '#fa3534',
                        }
                    }
                    ]};
						self.peoList.push(temp);
						})
					  },
					  fail: (error) => {
					    reject(error);
					  }
					});
				},
				handleCustomEvent(data) {
				      // 接收传递的信息
				      console.log('接收到信息:', data);
					  this.getPeople();
				    },
		},
		onLoad() {
			this.getPeople();
		}
	}
</script>

<style lang="scss">
	
.myPeople{
	padding: 10rpx;
}
.under{
			display: flex;
			justify-content: center;
			align-items: center;
			width: 100%;
			height: 150rpx;
			position: fixed;
			bottom: 0;
			.save{
				margin: 10rpx 20rpx 20rpx 20rpx;
				width: 650rpx;
			}
		}
</style>
