# device_check

保险片检测。采用SpringBoot开发，maven管理依赖。使用IDEA直接import项目，下载好依赖后执行run即可。


## todo

1. 轮询放后面，可以参考用session，application
2. 先测试直接连检测，摄像头实时展示的调试（摄像头能正常访问）
3. CheckRpc做两手准备
4. 


## 项目依赖的dll

包含opencv，serialport

```
链接：https://pan.baidu.com/s/185rLwYmXCBdynGXmhsdkuw 密码：fobl
```

## 初始账户

- 测试网址：http://iamlj.com:81/
- 登录账号
    + 管理员
        + 账号：admin
        + 密码：111111
    + 操作员
        + 账号：a
        + 密码：111111

## 软件要求

1. win7 +
2. jdk1.8 +
3. tomcat 不需要
4. idea 2017+ 
5. maven3.3.9+

## PRD设计

包含登录、管理员操作、业务员操作3大模块。

http://pub.iamlj.com/demo/device_check


## java调c++

参考 [JNI_最简单的Java调用C/C++代码](https://blog.csdn.net/wwj_748/article/details/28136061)

测试代码在 test目录下com.device.jni

## 部署运行

1. 项目会需要openCV的dll，jar。串口的dll
2. 项目的图片会记录在 `c:/device-check/main/`


生成前，修改`application.properties`中的`spring.profiles.active`的值由`dev`
改为`pro`，即采用生产配置。生产配置采用80端口，可自行创建数据库表。

- 生成jar

```java 
mvn clean package
```

附：需要安装maven，并且将镜像换成阿里云的，不然拉依赖jar包会非常慢。可自行百度找教程。

- 运行jar

执行上述后，在`/target`下会有`device_check-1.0.0.jar`，在当前目录运行下述命令即可运行项目：

```java 
java -jar device_check-1.0.0.jar
```

接着通过浏览器访问 `http://localhost/`，进入登录首页。