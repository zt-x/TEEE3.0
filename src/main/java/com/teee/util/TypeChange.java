package com.teee.util;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class TypeChange {
    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片地址
     * @return
     */
    public static String getImgBaseFile(String imgFile) {
        // 将图片文件转化为二进制流
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            System.out.println("FilePath = " + imgFile);
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 图片头
        //String imghead = "data:image/jpeg;base64,";
        Base64.Encoder encoder = Base64.getEncoder();
        System.out.println("Trans = " + new String(encoder.encode(data)));
        return new String(encoder.encode(data));
    }
    //获得图片的base64码
    public static String getImageBaseURL(String src) throws Exception {

        URL url = new URL(src);
        InputStream in = null;
        byte[] data = null;
        try {
            HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
            //in = url.openStream();//远程文件
            in = httpconn.getInputStream();
            int i = httpconn.getContentLength();
            data = new byte[i];
            int readCount=0;
            while(readCount<i){//循环读取数据
                readCount+=in.read(data,readCount,i-readCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Base64.Encoder encoder = Base64.getEncoder();
        String s =  encoder.encodeToString(data);
        in.close();
        return s;
    }
    public static BigDecimal Obj2BigDec(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }

    public static ArrayList<String> str2arrl(String str, String sep){
        if ("".equals(str) || "[]".equals(str) || str == null){
            return new ArrayList<>();
        }else{
            return new ArrayList<>(Arrays.asList(
                    str.substring(1, str.length() - 1).split(sep)
            ));
        }
    }
    public static ArrayList<String> str2arrl(String str){
        if ("".equals(str) || "[]".equals(str) || str == null){
            return new ArrayList<>();
        }else{
            return new ArrayList<>(Arrays.asList(
                    str.substring(1, str.length() - 1).split(", ")
            ));
        }
    }
    public static JSONArray str2Jarr(String str){

        return JSONArray.parseArray(str);
    }

    public static String Jarr2str(JSONArray jarr){
        String str = "";
        ArrayList<String> as = new ArrayList<>(jarr);
        return as.toString();
    }
    public static String arrL2str(ArrayList arr){
        return arr.toString();
    }
    public static JSONArray arrL2Jarr(ArrayList al){
        return JSONObject.parseArray(JSONObject.toJSONString(al));
    }
}
