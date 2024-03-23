<template>
	<view>
		<view class="container">
		  <view class="center">
		    <u-icon name="checkmark-circle" size="150" color="#5ac725" label="您可以在我的预约中查看您的预约" labelPos="bottom"></u-icon>
		  </view>
		  <view style="margin:15rpx">
			<u-button size="large" text="查看预约" color="#19be6b" @click="tomine"></u-button>
		  </view>
		  <view style="margin:15rpx">
		  	<u-button size="large" text="返回首页" plain color="#19be6b" @click="gotoFirst"></u-button>
		  </view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
			};
		},
		methods:{
			gotoFirst(){
				uni.switchTab({
					url:"/pages/index/index"
				})
			},
			tomine(){
				uni.reLaunch({
					url:"/pages/mine/mine"
				})
			}
		}
	}
</script>

<style lang="scss">
.container {
  text-align: center;
  padding: 40rpx;
}

.center {
  display: inline-block;
}
</style>
