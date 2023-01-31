package com.teee.vo;

import lombok.Data;

/**
 * @author Xu ZhengTao
 */
@Data
public class UploadErr {
    private String message;

    public UploadErr(String message) {
        this.message = message;
    }

}
