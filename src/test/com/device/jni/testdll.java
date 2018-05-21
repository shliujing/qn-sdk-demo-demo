package com.device.jni;

/**
 * reference：https://blog.csdn.net/wwj_748/article/details/28136061
  * with package's compile and run，in test dirctory
  javac  com/device/jni/Testdll.java
  java  com/device/jni/Testdll
  javah com.device.jni.testdll
 */
public class testdll {
    static {
        System.loadLibrary("testdll");
    }

    public native static int get();

    public native static void set(int i);

    public static void main(String[] args) {
        testdll test = new testdll();
        test.set(10);
        System.out.println(test.get());
    }
}
