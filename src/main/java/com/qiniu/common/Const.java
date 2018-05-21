package com.qiniu.common;

public class Const {
    public final static int successCode = 0;
    public final static String successDesc = "成功";
    public final static int errorCode = 1;
    public final static String errorDesc = "失败";

    //设置好账号的ACCESS_KEY和SECRET_KEY
    public static final String ACCESS_KEY = "VYw7rh_P_P6g6IH_v0zw1Upry5muffStXIly8AsS";
    public static final String SECRET_KEY = "ctSGEg8UXy_MNpjMbcHf66zISEXX69cRogZVurIk";

    //要上传的空间
    public static final String BUCKET_NAME = "test-pub";
    //设置转码操作参数
    public static final  String F_OPS = "avthumb/mp4/ab/128k/ar/22050/acodec/libfaac/r/30/vb/300k/vcodec/libx264/s/320x240/autoscale/1/stripmeta/0";
    //设置转码的队列
    public static final  String PIPE_LINE = "12349";

}
