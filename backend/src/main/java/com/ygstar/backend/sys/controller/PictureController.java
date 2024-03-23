package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.service.IUserService;
import com.ygstar.backend.sys.service.IVenueService;
import com.ygstar.backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/sys/picture")
@CrossOrigin
public class PictureController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IVenueService iVenueService;
     private SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
    public PictureController(){

    }

    private String savePicture(MultipartFile uploadFile, HttpServletRequest req){
        //获取文件名
        String fileName = uploadFile.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        try {
            BufferedImage bi = ImageIO.read(uploadFile.getInputStream());
            if (bi==null) return "图片格式不对";
        }catch (IOException e){
            e.printStackTrace();
            return "无法校验图片格式";
        }

        //重新生成文件名
        fileName = UUID.randomUUID()+suffixName;
        //添加日期目录
        String format = sd.format(new Date());
        //指定本地文件夹存储图片
        String filePath = "E:/Data_Ygstar/uploadFile/"+format+"/";
        File file = new File(filePath,fileName);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
            uploadFile.transferTo(new File(filePath+fileName));
            String url = "http://localhost"+":"+req.getServerPort()+"/"+format+"/"+fileName;
            return url;
        } catch (Exception e){
            e.printStackTrace();
            return "出现错误";
        }
    }
    //头像
    @PostMapping("/uploadAvatar")
    public Response<String> uploadAvatar(@RequestParam("userMail")String userMail,@RequestParam("file") MultipartFile uploadFile, HttpServletRequest req){
        Response<String> response = new Response<>();
        String url = savePicture(uploadFile, req);
//        if(url.startsWith("http://localhost")){
//            if (iUserService.updateAvatar(url, userMail)){
//                return "图像和数据库存储成功";
//            }else {
//                return "数据库存储失败";
//            }
//        } else {
//            return url;
//        }
        if(url.startsWith("http://localhost")){
            if(iUserService.updateAvatar(url, userMail)){
                response.setCode(200);
                response.setDesc("图像和数据库存储成功");
                response.setData(url);
            }else {
                response.setCode(500);
                response.setDesc("数据库存储失败");
            }
        } else {
            response.setCode(500);
            response.setDesc(url);
        }
        return response;
    }

    //  场馆图片
    @PostMapping("/uploadVenuePicture")
    public Response<String> uploadVenuePicture(@RequestParam Integer venueID, @RequestParam MultipartFile uploadFile, HttpServletRequest req){
        Response<String> response = new Response<>();
        String url = savePicture(uploadFile,req);
        if(url.startsWith("http://localhost")){
            if(iVenueService.updatePicture(venueID,url)){
                response.setCode(200);
                response.setDesc("图像和数据库存储成功");
            }else {
                response.setCode(500);
                response.setDesc("数据库存储失败");
            }
        } else {
            response.setCode(500);
            response.setDesc(url);
        }
        response.setData("");
        return response;
    }

    @PostMapping("/newPicture")
    public Response<String> newPicture(@RequestParam MultipartFile uploadFile, HttpServletRequest req){
        Response<String> response = new Response<>();
        String url = savePicture(uploadFile,req);
        if(url.startsWith("http://localhost")){
                response.setCode(200);
                response.setDesc(url);
        } else {
            response.setCode(500);
            response.setDesc(url);
        }
        response.setData("");
        return response;
    }

}
