package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.dao.VenueProfileDAO;
import com.ygstar.backend.sys.dao.VenueReservationDAO;
import com.ygstar.backend.sys.dto.StatusAndScore;
import com.ygstar.backend.sys.dao.VenueVisitDetailDAO;
import com.ygstar.backend.sys.dto.VenueReservationDTO;
import com.ygstar.backend.sys.entity.VenueReservation;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IVenueReservationService;
import com.ygstar.backend.sys.service.IVenueVisitScheduleService;
import io.swagger.annotations.ApiOperation;
import com.ygstar.backend.util.Response;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@RequestMapping("/sys/venueReservation")
@CrossOrigin
public class VenueReservationController {
    @Autowired
    private IVenueReservationService venueReservationService;

    @GetMapping("/count-by-week")
    public List<Integer> countByWeek(Integer venueId) {
        return venueReservationService.countTotalVisitorsByWeek(venueId);
    }

    @GetMapping("/getVRCByVeIdABTAVeVSI")
    @ApiOperation(value = "查询venueId，bookTime，venueVisitScheduleId相同的数据个数")
    public int getVRCByVeIdABTAVeVSI(Integer venueId, String bookTime, Integer venueVisitScheduleId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(bookTime, formatter);
        return venueReservationService.getVRCByVeIdABTAVeVSI(venueId,dateTime,venueVisitScheduleId);

    }

    @GetMapping("/getVenueReservations")
    public Response<List<VenueReservationDTO>> getVenueReservations(@RequestParam("status") Integer status,
                                                                   @RequestParam("userMail") String userMail){
        Response<List<VenueReservationDTO>> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(venueReservationService.getVenueReservations(status,userMail));
        return response;
    }

    @Autowired
    IVenueVisitScheduleService venueVisitScheduleService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @GetMapping("/insertVenueReservation")
    @ApiOperation(value = "插入VenueResrvation类，并返回成功与否的标志")
    public ResponseEntity<String> insertVenueReservation(VenueReservation venueReservation,String booktime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(booktime, formatter);
        venueReservation.setBookTime(dateTime);
        int hasSameRecord = venueReservationService.hasSameRecord(venueReservation);
        if(hasSameRecord>0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("已有相同记录");
        }
        try{
            int remainAccount = venueVisitScheduleService.getMCByVVSId(venueReservation.getVenueVisitScheduleId())
                    -venueReservationService.getVRCByVeIdABTAVeVSI(venueReservation.getVenueId(),
                    venueReservation.getBookTime(),venueReservation.getVenueVisitScheduleId());
            if(remainAccount>0){
                rocketMQTemplate.asyncSend("venue-reservation", MessageBuilder.withPayload(venueReservation).build(), new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println("发送成功");
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        System.out.println("发送失败");
                    }
                });
//                venueReservationService.insertVenueReservation(venueReservation);
                return ResponseEntity.status(HttpStatus.CREATED).body("预约成功");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("已无名额");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("预约失败");
        }

    }

    @GetMapping("/hasSameRecord")
    @ApiOperation(value = "查询是否有相同的记录")
    public int hasSameRecord(VenueReservation venueReservation,String booktime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(booktime, formatter);
        venueReservation.setBookTime(dateTime);
        return venueReservationService.hasSameRecord(venueReservation);
    }

    @GetMapping("/get-venue-reservations")
    public PageBean getVenueVisitDetails( Integer page,Integer pageSize, Integer venueId) {
        return venueReservationService.getVenueVisitDetails(venueId,page,pageSize);
    }

    @GetMapping("/getVenueProfile")
    @ApiOperation("首页简略表格")
    public List<VenueProfileDAO> getVenueProfile(Integer venueId){
        return venueReservationService.getVenueProfile(venueId);
    }


    @GetMapping("scoreVenue")
    @ApiOperation(value = "给场馆评分")
    public Response<Integer> scoreVenue(@RequestParam("orderNumber") Integer orderNumber,
                                        @RequestParam("venueID") Integer venueID,
                                        @RequestParam("score") Double score){
        venueReservationService.scoreVenue(orderNumber,venueID,score);
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
        response.setData(venueReservationService.getStatusAndScore(orderNumber));
        return response;
    }

    @GetMapping("/isDefaultCancel")
    @ApiOperation("判断是否是违约取消")
    public Response<Boolean> isDefaultCancel(@RequestParam("orderNumber")Integer orderNumber){
        Response<Boolean> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(venueReservationService.isDefaultCancel(orderNumber));
        return response;
    }

    @GetMapping("/cancelDefault")
    @ApiOperation("违约取消接口")
    public Response<Boolean> cancelDefault(@RequestParam("orderNumber") Integer orderNumber,
                                           @RequestParam("userMail") String userMail){
        Response<Boolean> response = new Response<>();
        int res = venueReservationService.cancelDefault(orderNumber,userMail);
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

    @GetMapping("/cancelNotDefault")
    @ApiOperation("正常取消预约接口")
    public Response<Boolean> cancel(@RequestParam("orderNumber")Integer orderNumber){
        Response<Boolean> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(venueReservationService.cancelNotDefault(orderNumber));
        return response;
    }

    @GetMapping("/visitVenue")
    @ApiOperation("扫描二维码按钮调用，使未参观变为已经参观")
    public Response<Boolean> visitVenue(@RequestParam("orderNumber")Integer orderNumber){
        Response<Boolean> response = new Response<>();
        response.setCode(200);
        response.setDesc("成功");
        response.setData(venueReservationService.visitVenue(orderNumber));
        return response;
    }
}
