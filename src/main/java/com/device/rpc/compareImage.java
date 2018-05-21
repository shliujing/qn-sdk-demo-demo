package com.device.rpc;

public class compareImage
{
    public static native double compareImage(String pathj,String pathg,float Vaulej,int x,int y,int width,int height,int type);


//      加载动态链接库
    public static double  getValue(String pathj,String pathg,float Vaulej,int x,int y,int width,int height,int type){
        System.loadLibrary("compareImage");
        compareImage nativeCode = new compareImage();
        double m = nativeCode.compareImage(pathj, pathg, Vaulej, x, y, width,height,type);

        return  m ;
    }


}


