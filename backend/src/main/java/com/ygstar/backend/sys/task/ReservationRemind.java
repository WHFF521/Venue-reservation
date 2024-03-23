package com.ygstar.backend.sys.task;

import com.ygstar.backend.sys.mapper.ActivityReservationMapper;
import com.ygstar.backend.sys.mapper.VenueReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
public class ReservationRemind {
    @Autowired
    VenueReservationMapper venueReservationMapper;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    ActivityReservationMapper activityReservationMapper;

    //    @Scheduled(cron ="*/10 * * * * ?")
    @Scheduled(cron ="0 0 1 * * ?")
    public void remind() {
        System.out.println("-----remind begin-----");
        List<String> venueMails = venueReservationMapper.venueRemind();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("场馆预约提醒");
        LocalDate date = LocalDate.now();
        message.setText("尊敬的用户，您好！您于"+date+"有一份场馆预约，您可以在微信小程序查看详情");
        message.setFrom("756044528@qq.com");
        for (String mail : venueMails) {
            message.setTo(mail);
            try {
                mailSender.send(message);
            }catch (MailSendException e){
                System.out.println(mail+"邮件发送异常");
            }
        }
        System.out.println("-----activity begin-----");
        message.setSubject("活动预约提醒");
        message.setText("尊敬的用户，您好！您于"+date+"有一份活动预约，您可以在微信小程序查看详情");
        List<String> activityMails = activityReservationMapper.activityRemind();
        for (String mail : activityMails) {
            message.setTo(mail);
            try {
                mailSender.send(message);
            }catch (MailSendException e){
                System.out.println(mail+"邮件发送异常");
            }
        }
        System.out.println("-----remind end-----");
    }
}
