package com.teee.api.in;

import com.alibaba.fastjson.JSON;
import com.teee.project.ProjectCode;
import com.teee.utils.JWT;
import com.teee.utils.TypeChange;
import com.teee.vo.Result;
import com.teee.vo.exception.SystemException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.CompareFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.CompareFaceResponse;
import org.springframework.stereotype.Component;

/**
 * @author Xu ZhengTao
 */
@Component
public class Tencent {
    //@Value("${Tencent.token}")
    private String sToken = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJzZWNyZXRJZCI6IkFLSURqVTZaTXhXaW9va2FMelhqcnNqUHRBSUczaEVib3BXRSIsInNlY3JldEtleSI6ImdIaTVMUXdLV2JUQWgzREQyWXRBRkF2WTVSY2RWQmkzIiwiZXhwIjoxMDMxNDU3NjMyNiwianRpIjoiM2QyOWU5MWYtZjQxZS00NTE1LWI0MmUtYTk2OTM1ZDM4YTE3In0.7xkfCZvfqeG5Y-dpiMpONvSRndtZRzKO04TYbt_svdY";

    private final String secretId = String.valueOf(JWT.parse(sToken).get("secretId"));
    private final String secretKey= String.valueOf(JWT.parse(sToken).get("secretKey"));

    /**
     * 人脸识别
     * */
    public Result faceCheck(String fileUrl1, String fileUrl2){
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        try {
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, "ap-chengdu", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            CompareFaceRequest req = new CompareFaceRequest();
            req.setImageA(TypeChange.getImgBaseFile(fileUrl1));
            req.setImageB(TypeChange.getImgBaseFile(fileUrl2));
            // 返回的resp是一个CompareFaceResponse的实例，与请求对象对应
            CompareFaceResponse resp = null;
            resp = client.CompareFace(req);

            return new Result(ProjectCode.CODE_SUCCESS,JSON.parse(CompareFaceResponse.toJsonString(resp)), "验证接收成功");
        } catch (Exception e) {
            throw new SystemException(ProjectCode.CODE_EXCEPTION_SYSTEM, "Tencent api err", e);
        }
        // 输出json格式的字符串回包
    }




}