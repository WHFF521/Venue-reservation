<template>
	<view>
		<view class="u-demo-block">
			 <u-swiper
			            :list="list5"
			            indicator
			            indicatorMode="dot"
			            circular
						:autoplay="auto"
			    ></u-swiper>
		</view>
		<view class="u-demo-block__content">
				<navigator url="/pages/venue_list/venue_list" class="u-page__button-item" open-type="redirect">
					<button class="largeBT" style="background-color: #8BC34A;color: aliceblue;">
						<text style="font-size: 18px;padding: 0px;">{{"参观预约"+"\n"}}</text>
						<text style="font-size: 12px;padding: 0px;">{{"Visit Appointment"}}</text>
						</button>
				</navigator>
				
				<navigator url="/pages/act_list/act_list" class="u-page__button-item" open-type="redirect">
					<button class="largeBT" style="background-color: #CDDC39;color: aliceblue;">
						<text style="font-size: 18px;padding: 0px;">{{"活动预约"+"\n"}}</text>
						<text style="font-size: 12px;padding: 0px;">{{"Activities Appointment"}}</text>
					</button>
				</navigator>
		</view>
		<hothot 
		title="热门活动" 
		:goodsArr="hotActList" 
		jumpurl="/pages/act_details/act_details?actId=" 
		moreurl="/pages/act_list/act_list">
		</hothot>
		<hothot 
		title="热门场馆" 
		:goodsArr="hotVenueList" 
		jumpurl="/pages/Venue_details/Venue_details?venueId="
		moreurl="/pages/venue_list/venue_list">
		</hothot>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		data() {
			return {
				auto:true,
				hotActList:[],
				hotVenueList:[],
				 list5: [
						'https://t7.baidu.com/it/u=4141604674,3317329080&fm=193&f=GIF',
						'https://t7.baidu.com/it/u=1208341692,1220880006&fm=193&f=GIF',
						'https://t7.baidu.com/it/u=2861023515,3957878495&fm=193&f=GIF',
						'https://t7.baidu.com/it/u=3387142997,2529721281&fm=193&f=GIF',
						],
				
			}
		},
		onShow() {
			if(uni.getStorageSync("token")){}else{
				uni.redirectTo({
					url:"/pages/login/login"
				})
			}
		},
		onLoad() {
			
			this.loadHotAct();
			this.loadHotVenue();
		},
		methods: {
			loadHotAct(){
				uni.request({
				  	url:config.baseUrl+"/sys/activity/getTopEightActivity",
					method:"GET",
				  	header:{
				  		token:uni.getStorageSync("token")
				  	},
				  	success: (res) => {
						res.data.forEach(item =>{
							this.hotActList.push({Id:item.actId,Picture:item.actPicture,Name:item.actName,Rating:item.rating})
						})
				  	}
				})
			},
			loadHotVenue(){
				uni.request({
				  	url:config.baseUrl+"/sys/venue/getTopEightVenue",
					method:"GET",
				  	header:{
				  		token:uni.getStorageSync("token")
				  	},
				  	success: (res) => {
						res.data.forEach(item =>{
							this.hotVenueList.push({Id:item.venueId,Picture:item.picture,Name:item.venueName,Rating:item.rating})
						})
				  	}
				})
			}
		}
	}
</script>

<style lang="scss">
	.u-page {
		&__button-item {
			margin: 15px 15px 15px 15px;
			// padding: 10px;
			// width: 120px;
		}
	}
	.u-demo-block__content{
		flex-direction: column;
		flex-wrap: wrap;
		align-items: center;
		display: flex;
		// justify-content: space-around;
	}
.largeBT{
	width: 340px; /* 设置按钮宽度为200像素 */
	height: 70px; /* 设置按钮高度为50像素 */
	text-align: center; /* 设置文字水平居中对齐 */
	line-height: 30px; /* 设置文字垂直居中对齐 */
}
</style>
