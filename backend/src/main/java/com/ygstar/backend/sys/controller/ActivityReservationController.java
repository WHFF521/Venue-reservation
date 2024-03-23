package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.dao.ActivityReservationDAO;
import com.ygstar.backend.sys.dto.StatusAndScore;
import com.ygstar.backend.sys.dto.VenueReservationDTO;
import com.ygstar.backend.sys.dao.ActivityDetailDAO;
import com.ygstar.backend.sys.dto.VenueActivityDTO;
import com.ygstar.backend.sys.entity.Activity;
import com.ygstar.backend.sys.entity.ActivityReservation;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IActivityReservationService;
import com.ygstar.backend.util.Response;
import io.swagger.annotations.ApiOperation;
import com.ygstar.backend.sys.service.IActivityService;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/sys/activityReservation")
@CrossOrigin
public class ActivityReservationController {
    @Autowired
    private IActivityReservationService iActivityReservationService;
    @Autowired
    private IActivityService iActivityService;

    @GetMapping("/getActivityReservations")
    public Response<List<ActivityReservationDAO>> getActivityReservations(@RequestParam("status") Integer status,
                                                                          @RequestParam("userMail") String userMail){
        Response<List<ActivityReservationDAO>> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(iActivityReservationService.getVenueReservations(status,userMail));
        return response;
    }

    @GetMapping ("/scoreActivity")
    @ApiOperation("用于给活动评分")
    public Response<Integer> scoreActivity (@RequestParam("orderNumber") Integer orderNumber,
                                            @RequestParam("activityID") Integer activityID,
                                            @RequestParam("score") Double score){
        iActivityReservationService.scoreActivity(orderNumber,activityID,score);
        Response<Integer> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        return response;
    }

    @GetMapping("/getStatusAndScore")
    @ApiOperation("获取状态以及评分，用于判断是否已经打过分并显示评分")
    public Response<StatusAndScore> getStatusAndScore(@RequestParam("orderNumber")Integer orderNumber){
        Response<StatusAndScore> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(iActivityReservationService.getStatusAndScore(orderNumber));
        return response;
    }

    @GetMapping("/isDefaultCancel")
    @ApiOperation("判断是否是违约取消活动")
    public Response<Boolean> isDefaultCancel(@RequestParam("activityId") Integer activityId){
        Response<Boolean> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(iActivityReservationService.isDefaultCancel(activityId));

        return response;
    }

    @GetMapping("/cancelNotDefault")
    @ApiOperation("正常取消活动预约接口")
    public Response<Boolean> cancel(@RequestParam("orderNumber") Integer orderNumber){
        Response<Boolean> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(iActivityReservationService.cancelNotDefault(orderNumber));
        return response;
    }

    @GetMapping("/cancelDefault")
    @ApiOperation("违约取消接口")
    public Response<Boolean> cancelDefault(@RequestParam("orderNumber") Integer orderNumber,
                                           @RequestParam("userMail") String userMail){
        Response<Boolean> response = new Response<>();
        int res = iActivityReservationService.cancelDefault(orderNumber,userMail);
        if(res==1){
            response.setCode(200);
            response.setDesc("成功");
            response.setData(true);
        } else if (res==2) {
            response.setCode(200);
            response.setDesc("成功");
            response.setData(false);
        } else {
            response.setCode(300);
            response.setDesc("服务器错误");
        }
        return response;
    }

    @GetMapping("/visitActivity")
    @ApiOperation("扫描二维码按钮调用，使未参观变为已经参观")
    public Response<Boolean> visitActivity(@RequestParam("orderNumber") Integer orderNumber){
        Response<Boolean> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(iActivityReservationService.visitActivity(orderNumber));
        return response;
    }

    @GetMapping("/getReservationCount")
    @ApiOperation(value = "查询活动报名的人数")
    public int getReservationCount(Integer activityId){
        return iActivityReservationService.getReservationCount(activityId);
    }


    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @GetMapping("/insertReservation")
    @ApiOperation(value = "插入预约数据")
    public ResponseEntity<String> insertReservation(Integer activityId, String userMail, String idCardNumber){
        int count = iActivityReservationService.hasSameRecord(idCardNumber);
        if(count>0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("已有相同记录");
        }
        else {
            try{
                int remainAccount = iActivityService.getMaximumCapacity(activityId)
                        -iActivityReservationService.getReservationCount(activityId);
                if(remainAccount<=0){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("预约人数已满");
                }else {
                    ActivityReservation activityReservation = new ActivityReservation();
                    activityReservation.setActivityId(activityId);
                    activityReservation.setUserMail(userMail);
                    activityReservation.setIdCardNumber(idCardNumber);
                    rocketMQTemplate.asyncSend("act-reservation", MessageBuilder.withPayload(activityReservation).build(), new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            System.out.println("发送成功");
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            System.out.println("发送失败");
                        }
                    });
//                    iActivityReservationService.insertReservation(activityId,userMail,idCardNumber);
                    return ResponseEntity.ok("预约成功");
                }

            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("插入失败");
            }
        }
    }

    @GetMapping("/hasSameRecord")
    @ApiOperation(value = "根据身份证号查询是否有相同记录")
    public int hasSameRecord(String idCardNumber){
        return iActivityReservationService.hasSameRecord(idCardNumber);
    }

    @GetMapping("/get-activity-info")
    public Object getActivityInfoByVenueId(@RequestParam Integer venueId) {
        List<VenueActivityDTO> activityInfoList = iActivityReservationService.getActivityInfoByVenueId(venueId);

        // 构建两个数组
        List<String> activityNames = new ArrayList<>();
        List<Integer> totalParticipants = new ArrayList<>();

        for (VenueActivityDTO activityInfo : activityInfoList) {
            activityNames.add(activityInfo.getActivityName());
            totalParticipants.add(activityInfo.getTotalParticipants());
        }

        return new Object[]{activityNames, totalParticipants};
    }

    //详情页活动数据获取
    @GetMapping("/get-activity-details")
    public PageBean getActivityDetails(Integer page, Integer pageSize, Integer venueId) {
        return iActivityReservationService.getActivityDetails(venueId,page,pageSize);
    }
}
