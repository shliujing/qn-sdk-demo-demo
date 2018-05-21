package com.qiniu.rpc;

import org.springframework.stereotype.Component;

@Component
public class serialport {
    static {
//        System.loadLibrary("serialport");
    }

    public native int getData(int data);
    public static void main(String[] args) {
        serialport nativeCode = new serialport();
        System.out.println(nativeCode.getData(65));
    }
}