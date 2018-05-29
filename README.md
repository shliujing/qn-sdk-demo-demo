# qn-sdk-demo-web

七牛 java sdk demo 的web展示版。目前仅供IMC售前团队使用。

## 项目说明

采用SpringBoot开发，maven管理依赖。使用IDEA直接import项目，下载好依赖后执行run即可。


## todo

1. uploadToken 缺少encodedPutPolicy，过期时间
2. qshell的java封装，调用 https://blog.csdn.net/qq_34884729/article/details/71439248
3. （暂时不需要）搜索功能
4. （暂时不需要）用表单、验证

### 其他有趣的

1. 还有时间控件，截图裁剪
2. 后期可以加入 步骤（Form Wizards）


## 初始账户

- 测试网址：http://iamlj.com:82/
- 登录账号
    + 管理员
        + 账号：admin
        + 密码：111111
    + 操作员
        + 账号：a
        + 密码：111111

## 软件要求

1. maven3.3.9+
2. jdk1.8 +
3. tomcat 不需要
4. idea 2017+ 


## 部署运行

- 生成jar

```java 
mvn clean package
```

附：需要安装maven，并且将镜像换成阿里云的，不然拉依赖jar包会非常慢。可自行百度找教程。

- 运行jar

执行上述后，在`/target`下会有`qn-sdk-demo-web-1.0.0.jar`，在当前目录运行下述命令即可运行项目：

```java 
java -jar qn-sdk-demo-web-1.0.0.jar
```

接着通过浏览器访问 `http://localhost/`，进入登录首页。


## 服务器run
```
cd /alidata/java/qn-sdk-demo-web/
./del.sh
rz
./start.sh
```