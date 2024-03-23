<template>
	<view class="pl">
      <u-swipe-action>
        <u-swipe-action-item
          :options="item.options"
          v-for="(item, index) in allPeople"
          :disabled="item.disabled"
          :key="index"
			@click="onClick(item.id,item.textName)"
        >
          <view
            class="swipe-action u-border-top"
            :class="[index === allPeople.length - 1 && 'u-border-bottom']"
          >
            <view class="swipe-action__content">
              <text decode=true>{{ item.textName }}</text>
			   <text decode=true style="font-size: 12px;">{{ item.textId }}</text>
            </view>
          </view>
        </u-swipe-action-item>
      </u-swipe-action>
	</view>
</template>

<script>
	import config from '@/config.js';
	export default {
		name:"people_list",
		
		props:{
			allPeople:{
				type: Array,
				default:()=>[]
			}	
		},
		data() {
			return {
                
			}
		},
		methods:{
			onClick(id,name){
				console.log(id);
				const self = this;
				uni.showModal({
				    title: '温馨提示',  //提示标题
				    content: '是否要删除'+name,  //提示内容
				    showCancel: true, //是否显示取消按钮
				    success: function (res) {
				        if (res.confirm) {  //confirm为ture，代表用户点击确定
				            console.log('点击了确定按钮');
							uni.request({
							  	url:config.baseUrl+"/sys/visitor/deleteVisitor",
								method:"GET",
							  	header:{
							  		token:uni.getStorageSync("token")
							  	},
								data:{
									idCardNumber:id,
									userMail:uni.getStorageSync("userMail"),
								},
							  	success: (res) => {
									console.log('删除成功');
									self.$emit("customEvent",'删除成功');
							  	},
							})
				        } else if (res.cancel) {  //cancel为ture，代表用户点击取消
				            console.log('点击了取消按钮');
				        }
				    }
				})
			}
		}
	};
</script>

<style lang="scss">
.pl{
	padding: 20rpx;
}
    .u-page {
        padding: 20rpx;
    }

    .u-demo-block__title {
        padding: 10px 0 2px 15px;
    }

    .swipe-action {
        &__content {
             padding: 25rpx 0;
    
            &__text {
                 font-size: 15px;
                 color: $u-main-color;
                 padding-left: 30rpx;
             }
        }
    }
</style>
