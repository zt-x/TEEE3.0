package com.teee.service;

import com.teee.vo.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface FileLoadService {
    Result downloadFile(String fileName, Integer fileType, HttpServletResponse response) throws UnsupportedEncodingException;

}
