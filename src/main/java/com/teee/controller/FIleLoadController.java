package com.teee.controller;

import com.teee.project.ProjectCode;
import com.teee.utils.MyAssert;
import com.teee.utils.TypeChange;
import com.teee.vo.Result;
import com.teee.vo.UploadErr;
import com.teee.vo.UploadResult;
import com.teee.vo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@Controller
@RequestMapping("/upload")
@Slf4j
public class FIleLoadController {
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


    ArrayList<String> suffixWhiteList;

    public FIleLoadController() {
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

    public  UploadResult upload(MultipartFile file, HttpServletRequest request, String path, String dirName, boolean isPic){
        if(file == null){
            return new UploadResult(0, new UploadErr("未发现上传的文件"));
        }
        String originalFilename = file.getOriginalFilename();
        MyAssert.notNull(originalFilename, "获取文件名失败");
        assert originalFilename != null;
        String appendName = originalFilename.substring(originalFilename.lastIndexOf("."));
        if(isPic && !suffixWhiteList.contains(appendName)){
            return new UploadResult(0, new UploadErr("图片格式不支持"));
        }
        File newMkdir = new File(path);
        if(!newMkdir.exists()){
            boolean mkdirs = newMkdir.mkdirs();
            MyAssert.isTrue(mkdirs, "创建文件夹失败了 ... ");
        }
        String uploadFile = System.currentTimeMillis() + appendName;
        try {
            file.transferTo(new File(path+File.separator+uploadFile));
            //String url = request.getScheme() + "://" + request.getServerName() + ":" + port + "/" + dirName + "/" + uploadFile;
            String url ="resources/" + dirName + "/" + uploadFile;
            log.info(String.valueOf(new UploadResult(1, uploadFile,url)));
            return new UploadResult(1, uploadFile,url);
        } catch (IOException e) {
            e.printStackTrace();
            return new UploadResult(0, new UploadErr("上传失败"));
        }
    }
    @RequestMapping("/file")
    @ResponseBody
    public Result uploadFile(@RequestParam("file") MultipartFile[] file){
        try{
            log.info("进入上传队列 ...");

            ArrayList<String> arrayList = new ArrayList<>();
            for (MultipartFile multipartFile : file) {
                if(multipartFile.isEmpty()){
                    throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "请不要上传空文件哦");
                }
                String fileName = multipartFile.getOriginalFilename();
                if(fileName == null){
                    fileName = UUID.randomUUID().toString();
                }
                int point = fileName.lastIndexOf(".");
                String suffixName = point>0?fileName.substring(point):"";
                log.info("上传: " + fileName + ", 后缀: " + suffixName);
                File fileTempObj = new File(filePath + File.separator + "TimeStamp_" +  System.currentTimeMillis() + "_" + fileName);
                if(!fileTempObj.getParentFile().exists()){
                    boolean mkdirs = fileTempObj.getParentFile().mkdirs();
                    MyAssert.isTrue(mkdirs, "上传文件后创建文件夹时出错");
                }
                try{
                    multipartFile.transferTo(fileTempObj);
                }catch (Exception e){
                    throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "写入文件失败:"+ fileTempObj.getName(), e);
                }
                arrayList.add("\"" + fileTempObj.getName() + "\"");
            }
            return new Result(ProjectCode.CODE_SUCCESS, TypeChange.arrL2str(arrayList), "上传成功！");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_SYSTEM, "上传文件时产生的未知异常 ... ", e);
        }

    }

    @RequestMapping("/works")
    @ResponseBody
    public UploadResult uploadWorkImg(@RequestParam("upload") MultipartFile file, HttpServletRequest request){
        return upload(file, request, worksPicPath, "pic/works", true );
    }
    @RequestMapping("/faces")
    @ResponseBody
    public UploadResult uploadFaceImg(@RequestParam("upload") MultipartFile file, HttpServletRequest request){
        return upload(file, request, facesPicPath, "pic_faces", true );
    }
    @RequestMapping("/files")
    @ResponseBody
    public UploadResult uploadFiles(@RequestParam("upload") MultipartFile file, HttpServletRequest request){
        return upload(file, request, filePath, "file_files", false );
    }
    @RequestMapping("/temps")
    @ResponseBody
    public UploadResult uploadTempFiles(@RequestParam("upload") MultipartFile file, HttpServletRequest request){
        return upload(file, request, tempsPath, "file_temp", false );
    }
    @RequestMapping("/getFile")
    @ResponseBody
    public Result downloadFile(@RequestParam("fileName") String fileName, @RequestParam("fileType") Integer fileType, HttpServletResponse response) throws UnsupportedEncodingException {
        String path = "";
        if(fileType == ProjectCode.FILETYPE_TEMP){
            //临时文件
            path = tempsPath;
        }else if(fileType == ProjectCode.FILETYPE_POTENCY){
            path = filePath;
        }else {
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "未找到您要的文件😖, 可能请求参数出了问题");
        }

        File file = new File(path + File.separator + fileName);

        String substring = fileName.substring(fileName.lastIndexOf("_")+1);
        String fileOriginName = substring.substring(substring.lastIndexOf("_")+1);
        if(!file.exists()){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "未找到您要的文件😖");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition", URLEncoder.encode(fileOriginName, "UTF-8"));
        long startTime = System.currentTimeMillis();
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(file);
            OutputStream os = response.getOutputStream();
            os.write(bytes);
            os.close();
        } catch (IOException e) {
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "启动下载失败了😫");
        }

        return null;
    }

}
