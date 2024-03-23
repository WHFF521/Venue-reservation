<template>
	<view class="hothot">
		<view class="hothot">
			<view class="titlestyle">
				<text>
				{{title}}
				</text>
				<image class="image-after-text" src="/static/icon/hot.png"></image>
			</view>
			
			<!-- <u--text suffixIcon="" iconStyle="font-size: 18px" text="查看更多"></u--text> -->
			<u-scroll-list
				indicatorColor="#fff0f0"
				indicatorActiveColor="#f56c6c"
				@right="right"
				@left="left"
			>
				<view
					class="scroll-list"
					style="flex-direction: row;"
				>
					<view
						class="scroll-list__goods-item"
						v-for="(item, index) in goodsArr"
						:key="index"
						:class="[(index === 9) && 'scroll-list__goods-item--no-margin-right']"
					>
						<image
							class="scroll-list__goods-item__image"
							:src=item.Picture
							mode=""
							@click="piclick(item.Id)"
						></image>
						<text class="scroll-list__goods-item__text">{{item.Name}}</text>
					</view>
					<view
						class="scroll-list__show-more"
						@tap="showMore"
					>
						<text class="scroll-list__show-more__text">查看更多</text>
						<u-icon
							name="arrow-leftward"
							color="#f56c6c"
							size="12"
						></u-icon>
					</view>
				</view>
			</u-scroll-list>
		</view>
	</view>
</template>

<script>
	export default {
		name:"hothot",
		props:{
			title:{
				type: String,
				default:"默认标题",
			},
			goodsArr:{
				type: Array,
				default:()=>[]
			},
			jumpurl:{
				type:String
			},
			moreurl:{
				type:String
			}
		},
		data() {
			return {
				list: [{
				                    thumb: "https://cdn.uviewui.com/uview/goods/1.jpg"
				                }, {
				                    thumb: "https://cdn.uviewui.com/uview/goods/2.jpg"
				                }, {
				                    thumb: "https://cdn.uviewui.com/uview/goods/3.jpg"
				                }, {
				                    thumb: "https://cdn.uviewui.com/uview/goods/4.jpg"
				                }, {
				                    thumb: "https://cdn.uviewui.com/uview/goods/5.jpg"
				        }],
				goodsBaseUrl: 'https://cdn.uviewui.com/uview/goods/',
				menuBaseUrl: 'https://cdn.uviewui.com/uview/menu/',
			};
		},
		methods:{
			showMore() {
				uni.reLaunch({
					url:this.moreurl
				})
				
			},
			left() {
				console.log('left');
			},
			right() {
				console.log('right');
			},
			
			piclick(actId){
				uni.reLaunch({
					url:this.jumpurl+actId
				})
			}
		}
	}
</script>

<style lang="scss">
.titlestyle{
	padding-bottom: 15rpx;
}	
.image-after-text {
  width: 15px;
  height: 15px;
  margin-left: 5px;
}
.hothot{
	padding: 15rpx;
}
.scroll-list {
	@include flex(column);

	&__goods-item {
		margin-right: 20px;

		&__image {
			width: 100px;
			height: 100px;
			border-radius: 4px;
		}

		&__text {
			// color: #f56c6c;
			text-align: center;
			font-size: 12px;
			margin-top: 5px;
		}
	}

	&__show-more {
		background-color: #fff0f0;
		border-radius: 3px;
		padding: 3px 6px;
		@include flex(column);
		align-items: center;

		&__text {
			font-size: 12px;
			width: 12px;
			color: #f56c6c;
			line-height: 16px;
		}
	}

	&__line {
		@include flex;
		margin-top: 10px;

		&__item {
			margin-right: 15px;

			&__image {
				width: 61px;
				height: 48px;
			}

			&__text {
				margin-top: 5px;
				color: $u-content-color;
				font-size: 12px;
				text-align: center;
			}

			&--no-margin-right {
				margin-right: 0;
			}
		}
	}
}
</style>