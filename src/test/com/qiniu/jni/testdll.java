package com.qiniu.jni;

/**
 * reference：https://blog.csdn.net/wwj_748/article/details/28136061
  * with package's compile and run，in test dirctory
  javac  com/qiniu/jni/Testdll.java
  java  com/qiniu/jni/Testdll
  javah com.qiniu.jni.testdll
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
