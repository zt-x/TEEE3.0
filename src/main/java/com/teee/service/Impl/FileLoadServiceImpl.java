package com.teee.service.Impl;

import com.teee.project.ProjectCode;
import com.teee.service.FileLoadService;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
@Service

public class FileLoadServiceImpl implements FileLoadService {
    @Value("${path.file.files}")
    private String filePath;

    @Value("${path.file.temps}")
    private String tempsPath;
    @Override
    public Result downloadFile(String fileName, Integer fileType, HttpServletResponse response) throws UnsupportedEncodingException {
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
