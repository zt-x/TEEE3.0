package com.teee.vo;

import lombok.Data;

/**
 * @author Xu ZhengTao
 */
@Data
public class UploadResult {
    private Integer uploaded; // 1：上传成功
    private String fileName; //文件名
    private String url; //上传后的访问地址
    private UploadErr error; //错误信息

    public UploadResult(Integer uploaded, String fileName, String url) {
        this.uploaded = uploaded;
        this.fileName = fileName;
        this.url = url;
    }

    public UploadResult(Integer uploaded, UploadErr error) {
        this.uploaded = uploaded;
        this.error = error;
    }

    @Override
    public String toString() {
        return "UploadResult{" +
                "uploaded=" + uploaded +
                ", fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                ", error=" + error +
                '}';
    }
}
