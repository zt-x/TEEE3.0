package com.teee.controller;

import com.teee.vo.Result;
import com.teee.vo.UploadErr;
import com.teee.vo.UploadResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/upload")
public class UploadController {
    @Value("${path.pic.works}")
    private String worksPicPath;

    @Value("${path.pic.faces}")
    private String facesPicPath;

    @Value("${path.file.files}")
    private String filePath;

    @Value("${path.file.temps}")
    private String tempsPath;

    @Value("${server.port}")
    private String port;

    @Value("${realMaxFileSizeMB}")
    private int maxSizeMB;

    ArrayList<String> suffixWhiteList;

    public UploadController() {
        suffixWhiteList= new ArrayList<>();
        suffixWhiteList.add(".png");
        suffixWhiteList.add(".jpg");
        suffixWhiteList.add(".jpeg");
        suffixWhiteList.add(".gif");
        suffixWhiteList.add(".ico");
        suffixWhiteList.add(".cur");
        suffixWhiteList.add(".jfif");
        suffixWhiteList.add(".pjpeg");
        suffixWhiteList.add(".pjp");
        suffixWhiteList.add(".svg");
        suffixWhiteList.add(".tif");
        suffixWhiteList.add(".webp");
        suffixWhiteList.add(".tiff");
    }

    private  UploadResult upload(MultipartFile file, HttpServletRequest request, String path, String dirName, boolean isPic){
        if(file == null){
            return new UploadResult(0, new UploadErr("未发现上传的文件"));
        }
        String originalFilename = file.getOriginalFilename();
        String appendName = originalFilename.substring(originalFilename.lastIndexOf("."));
        if(isPic && !suffixWhiteList.contains(appendName)){
            return new UploadResult(0, new UploadErr("图片格式不支持"));
        }
        File newMkdir = new File(path);
        if(!newMkdir.exists()){
            newMkdir.mkdirs();
        }
        String uploadFile = System.currentTimeMillis() + appendName;
        try {
            file.transferTo(new File(path+File.separator+uploadFile));
            String url = request.getScheme() + "://" + request.getServerName() + ":" + port + "/" + dirName + "/" + uploadFile;
            return new UploadResult(1, uploadFile,url);
        } catch (IOException e) {
            e.printStackTrace();
            return new UploadResult(0, new UploadErr("上传失败"));
        }
    }

    @RequestMapping("/works")
    public UploadResult uploadPicImg(@RequestParam("upload") MultipartFile file, HttpServletRequest request){
        UploadResult uploadResult = upload(file, request, worksPicPath, "pic", true );
        return uploadResult;
    }

}
