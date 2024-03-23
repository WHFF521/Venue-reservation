<template>
	<view>
		<view class="serchbar">
			<u-search :showAction="false" placeholder="请输入场馆名称" :animation="true" @search="onSearch" v-model="searchValue"></u-search>
		</view>
		<view class="u-page">
				<u-list
					@scrolltolower="scrolltolower"
				>
					<u-list-item
						v-for="(item, index) in indexList"
						:key="index"
					>
					<!-- <navigator url='/pages/Venue_details/Venue_details?venueId=${item.venueId}'> -->
						<u-cell :title="item.venueName" :label="item.openTime" @click="navi(item.venueId)">
							<u-avatar
								slot="icon"
								shape="square"
								size="60"
								:src="item.picture"
								customStyle="margin: -3px 5px -3px 0"
							></u-avatar>
							<text
							    	    slot="value"
							    	    class="u-slot-value"
										style="color:#f56c6c;font-size: 10pt;"
							    	>{{item.rating+"分"}}</text>
							</u-cell>
					<!-- </navigator> -->
					</u-list-item>
				</u-list>
			</view>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
			data() {
				return {
					indexList: [],
					searchValue:"",
				}
			},
			onLoad() {
				// this.loadmore()
				this.loadVenue();
			},
			methods: {
				loadVenue(){
					 uni.request({
					  	url:config.baseUrl+"/sys/venue/VenueList",
						method:"GET",
					  	header:{
					  		token:uni.getStorageSync("token")
					  	},
					  	success: (res) => {
					 	 	console.log(res);
							this.indexList=res.data;
					  	}
					})
				},
				scrolltolower() {
					this.loadmore()
				},
				loadmore() {
					// for (let i = 0; i < 20; i++) {
					// 	this.indexList.push({
					// 		// url: this.urls[uni.$u.random(0, this.urls.length - 1)]
					// 	})
					// }
				},
				navi(id){
					uni.navigateTo({
						url:"/pages/Venue_details/Venue_details?venueId="+id
					})
				},
				onSearch(){
					console.log(this.searchValue);
					if(!this.searchValue.length){
						this.loadVenue();
						return;
					}
					uni.request({
					  	url:config.baseUrl+"/sys/venue/searchVenueByName",
						method:"GET",
					  	header:{
					  		token:uni.getStorageSync("token")
					  	},
						data:{
							venueName:this.searchValue
						},
					  	success: (res) => {
					 	 	console.log(res);
							this.indexList=[];
							this.indexList=res.data;
					  	}
					})
				}
			},
		}
</script>

<style lang="scss">
	.serchbar{
		padding: 10px;
	}
</style>
