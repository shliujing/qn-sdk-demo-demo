var isCheck = true;//是按下了摄像头 还是检测，后面用函数名 参数替代


// region 常规，人员操作
//	删除人员
function user_del(account) {
    if (confirm("确定删除？")) {
        $.post("/user/delete", {account: account}, function (data) {
            if (data != null && data.result) {
                window.location.reload();
            } else {
                alert(data.desc);
            }
        });
    }
}

//	打开 编辑人员
function user_edit(id) {
    $.post("/user/toEdit", {id: id}, function (data) {
        if (data != null && data.result && data.obj != null) {
            $('#user-edit').modal({
                backdrop: "static"
            });
            $("#edit-id").val(data.obj.id);
            $("#edit-userName").val(data.obj.userName);
            $("#edit-account").val(data.obj.account);
            $("#edit-password").val(data.obj.password);
            $("#edit-password_check").val(data.obj.password);
            $("#edit-userTypeName").val(data.obj.userTypeName);
        }
    });
}

//	提交 编辑人员
function edit_submit() {
    if (edit_pwd_check()) {
        return;
    }

    $.post("/user/edit", {
        id: $("#edit-id").val(),
        userName: $("#edit-userName").val(),
        account: $("#edit-account").val(),
        password: $("#edit-password").val(),
        userTypeName: $("#edit-userTypeName").val()
    }, function (data) {
        if (data != null && data.result) {
            alert("修改成功");
            window.location.reload();
        } else {
            document.getElementById("edit-warning").innerHTML = data.desc;
        }
    });
}

//	提交 添加人员
function add_submit() {
    if (add_pwd_check()) {
        return;
    }

    $.post("/user/add", {
        userName: $("#add-userName").val(),
        account: $("#add-account").val(),
        password: $("#add-password").val(),
        userTypeName: $("#add-userTypeName").val()
    }, function (data) {
        if (data != null && data.result) {
            window.location.reload();
        } else {
            document.getElementById("add-warning").innerHTML = data.desc;
        }
    });
}

// 密码核对
function add_pwd_check() {
    var result = false;
    if ($("#add-password").val() != $("#add-password_check").val()) {
        document.getElementById("add-warning").innerHTML = "两次密码的输入不一致";
        result = true;
    } else {
        document.getElementById("add-warning").innerHTML = "";
    }
    return result;
}

// 密码核对
function edit_pwd_check() {
    var result = false;
    if ($("#edit-password").val() != $("#edit-password_check").val()) {
        document.getElementById("edit-warning").innerHTML = "两次密码的输入不一致";
        result = true;
    } else {
        document.getElementById("edit-warning").innerHTML = "";
    }
    return result;
}

// endregion


// region 检测操作

// 打开检测modal
function operator_check() {
    $('#user-check').modal({
        backdrop: "static"
    });
}

// 测试	摄像头截图
function testWebCamCapture() {
    isCheck = false;
    this.savePhoto();
}

// 测试	检测1 保存图片
function testCheck() {
    isCheck = true;
    this.savePhoto();
}

// 测试	检测2 发起请求
function testCheckAjax(currentImgUrl) {
    $.post("/main/testCheck", {
        id: "1", currentImgUrl: currentImgUrl
    }, function (data) {
        var log = "";
        // CheckResultDTO data.result
        if (data != null && data.result) {
            // alert("检测成功，接口描述：" + data.desc + "\n" +
            //     "检测结果：" + data.obj.result + "\n" +
            //     "错误图片1 id：" + data.obj.data[0].id + "\n" +
            //     "错误图片1 name：" + data.obj.data[0].name + "\n" +
            //     "错误图片1 trueImgUrl：" + data.obj.data[0].trueImgUrl + "\n" +
            //     "错误图片1 wrongImgUrl：" + data.obj.data[0].wrongImgUrl);
            if (data.obj.result) {
                log = "检测成功";
                $("#quaNum").html(data.obj.quaNum);
                //检测成功，打印标签，传递标签code;  20180427 sxf
                labelPrint(data);
            } else {
                // 显示错误区域 画X
                log = "检测失败";
                data.obj.data.forEach(function (item, index) {
                    log += "</br><span style='color:red'>" + item.coordinate.name + " 错误</span>";
                });
                $("#unQuaNum").html(data.obj.unQuaNum);

                var len = data.obj.data.length;
                if (len > 0) {
                    var canvasSreen = document.getElementById("canvasEl");
                    var ctx = canvasSreen.getContext('2d');
                    ctx.font = "14px Arial";
                    ctx.fillStyle = "#FF2976";
                    ctx.strokeStyle = "#FF2976";//线条的颜色

                    for (var i = 0; i < len; i++) {
                        var x1= data.obj.data[i].coordinate.x;
                        var x2= data.obj.data[i].coordinate.x + data.obj.data[i].coordinate.w;
                        var y1= data.obj.data[i].coordinate.y;
                        var y2= data.obj.data[i].coordinate.y + data.obj.data[i].coordinate.h;
                        ctx.fillText(data.obj.data[i].coordinate.name, x1-25, y1+14);
                        ctx.moveTo(x1, y1);
                        ctx.lineTo(x2, y2);
                        ctx.stroke();

                        ctx.moveTo(x2, y1);
                        ctx.lineTo(x1, y2);
                        ctx.stroke();
                    }
                }
            }
        } else {
            alert("检测失败，接口描述：" + data.desc + "\n");
            log = "检测出现异常";
        }
        $(".conntent-result").html(log)
    });
}

/*-  标签打印 sxf  20180427 start   -*/
function labelPrint(data) {
//    var code = data.code;  先写死
    var code = "LZ-FA-060-01";
    $.ajax({
        url: "/main/labelPrint",
        data: code, // 参数
        type: "post",
        cache: false,
        dataType: "json" //返回json数据


    });
}

/*-  标签打印 sxf  20180427 end   -*/

// 测试	分割图片
function testCutImage() {
    $.post("/main/testCutImage", {
        type: 1, value: 0.6, currentImgUrl: "muban-1-F01"
    }, function (data) {
        if (data != null && data.result) {
            alert("分割成功，接口描述" + data.desc + "\n" +
                "分割 type：" + data.obj.id + "\n" +
                "分割 value：" + data.obj.value + "\n" +
                "分割 currentImgUrl：" + data.obj.currentImgUrl + "\n" +
                "分割 resultImgUrl：" + data.obj.resultImgUrl)
        } else {
            alert("分割失败，接口描述：" + data.desc + "\n");
        }
    });
}

var time = setTimeout('trainingCheck()', 5000);//轮询执行，500ms一次

// 轮询 检测
function trainingCheck() {
    $.ajax({
        type: "get",
        async: false,
        url: "/main/trainingCheck",
        dataType: "json",
        timeout: 60000
    }).done(function (data) {
        if (data != null && data.result) {
            if (data.obj == 2) {//  继续轮询
                console.log(data.obj + "," + data.desc);
            } else if (data.obj == 1) {//触发检测
                $(".conntent-result").text("检测中...");
                $("#a3").attr("disabled", true);
                $("#check").val("检测中");
                $("#check").attr("disabled", true);
                testCheck();
                console.log(data.obj + "," + data.desc);
                $(".conntent-result").text("等待检测");
                $("#a3").attr("disabled", false);
                $("#check").attr("disabled", false);
                $("#check").val("检测");
            } else if (data.obj == 0) {//设备故障
                console.log(data.obj + "," + data.desc);
                alert(data.desc);
                $(".conntent-result").html("<span style='color:red'>" + data.desc + "</span>");
            }
        } else {
            alert("轮询异常");
        }
        time = setTimeout('trainingCheck()', 2000);//轮询执行，500ms一次
    });
}


// function trainingCheck() {  　　　　　　　　　　//jQuery的AJAX执行的配置对象
//     $.ajax({
//         type: "get",　　　　　　//设置请求方式，默认为GET，
//         async: false,
//         url: "/main/trainingCheck",
//         dataType: "json",　　　　//设置期望的返回格式，因服务器返回json格式，这里将数据作为json格式对待
//         success: function (data) {
//             if (data != null && data.result) {
//                 if (data.obj == 2) {//  2继续轮询
//                     console.log(data.obj + "," + data.desc);
//                 } else if (data.obj == 1) {//1触发检测
//                     console.log(data.obj + "," + data.desc);
//                     // $(".conntent-result").html( bvcv ??"检测中");
//                     // $("#a3").attr("disable",true);
//                     // $("#check").attr("disable",true);
//                     // testCheck();
//                     // $(".conntent-result").html("等待检测");
//                     // $("#a3").attr("disable",false);
//                     // $("#check").attr("disable",false);
//                 } else if (data.obj == 0) {//设备故障
//                     console.log(data.obj + "," + data.desc);
//                     // alert(data.desc);
//                     $(".conntent-result").html("<span style='color:red'>data.desc</span>");
//                 }
//             } else {
//                 alert("轮询异常");
//             }
//             setTimeout(trainingCheck(), 3000);　　//成功时的回调函数，处理返回数据，并且延时建立新的请求连接
//         },
//         error: function (data) {
//             alert(data.desc);
//             $(".conntent-result").html("<span style='color:red'>data.desc</span>");
//             setTimeout(trainingCheck(), 3000);
//         }　　　　//成功时的回调函数，处理返回数据，并且延时建立新的请求连接
//     })
// };
// setTimeout(trainingCheck(), 3000);//执行ajax请求。

// endregion

// region 摄像头
$(function () {
    var w = 1280, h = 1024;
    var wDisplay = 320, hDisplay = 240;

    var pos = 0, ctx = null, saveCB, image = [];

    var canvas = document.createElement("canvas");
    canvas.setAttribute('width', w);
    canvas.setAttribute('height', h);

    if (canvas.toDataURL) {
        ctx = canvas.getContext("2d");
        image = ctx.getImageData(0, 0, w, h);
        saveCB = function (data) {

            var col = data.split(";");
            var img = image;

            for (var i = 0; i < w; i++) {
                var tmp = parseInt(col[i]);
                img.data[pos + 0] = (tmp >> 16) & 0xff;
                img.data[pos + 1] = (tmp >> 8) & 0xff;
                img.data[pos + 2] = tmp & 0xff;
                img.data[pos + 3] = 0xff;
                pos += 4;
            }
            //base64 图像变成字符串 ajax 异步  同步
            if (pos >= 4 * w * h) {
                ctx.putImageData(img, 0, 0);
                $.post("/main/testWebCam?t=" + new Date().getTime(), {
                    type: "pixel",
                    async: false,
                    image: canvas.toDataURL("image/jpeg")//png
                }, function (data) {
                    if (data != null && data.result) {
                        console.log("图像存储成功，imgName：" + data.obj.imgUrl);
                        pos = 0;
                        if (isCheck) {
                            testCheckAjax(data.obj.imgUrl);
                        } else {
                            $("#left-img").attr("src", data.obj.imgUrl);
                        }
                    } else {
                        alert("获取摄像头图像失败：" + data.desc);
                    }
                });
            }
        };
    } else {
        saveCB = function (data) {
            image.push(data);

            pos += 4 * w;
            if (pos >= 4 * w * h) {
                $.post("/main/testWebCam?", {type: "pixel", async: false, image: image.join('|')}, function (data) {
                    if (data != null && data.result) {
                        console.log("====" + data);
                        pos = 0;
                        if (isCheck) {
                            testCheckAjax(data.obj.imgUrl);
                        } else {
                            $("#left-img").attr("src", data.obj.imgUrl);
                        }
                    } else {
                        alert("获取摄像头图像失败：" + data.desc);
                    }
                });
            }
        };
    }

    // todo  url 优化
    $(".conntent-realtime").webcam({
        width: wDisplay,
        height: hDisplay,
        mode: "callback",
        swffile: $("#indexjs").attr("src").substring(0, 4) + "jscam.swf",
        onSave: saveCB,
        onCapture: function () {
            webcam.save();
        },

        debug: function (type, string) {
            console.log(type + ": " + string);
        }
    });
});

//拍照
function savePhoto() {
    webcam.capture();
}

// endregion



// test

// 测试	检测2 发起请求
function testCheckAjax1(currentImgUrl) {
    $.post("/main/testCheck1", {
        id: "1", currentImgUrl: currentImgUrl
    }, function (data) {
        var log = "";
        if (data != null && data.result) {
            // alert("检测成功，接口描述：" + data.desc + "\n" +
            //     "检测结果：" + data.obj.result + "\n" +
            //     "错误图片1 id：" + data.obj.data[0].id + "\n" +
            //     "错误图片1 name：" + data.obj.data[0].name + "\n" +
            //     "错误图片1 trueImgUrl：" + data.obj.data[0].trueImgUrl + "\n" +
            //     "错误图片1 wrongImgUrl：" + data.obj.data[0].wrongImgUrl);
            if (data.obj.result) {
                log = "检测成功";
                $("#quaNum").html(data.obj.quaNum);
                //检测成功，打印标签，传递标签code;  20180427 sxf
                labelPrint(data);
            } else {
                // 显示错误区域 画X
                log = "检测失败";
                data.obj.data.forEach(function (item, index) {
                    log += "</br><span style='color:red'>" + item.name + " 错误</span>";
                });
                $("#unQuaNum").html(data.obj.unQuaNum);

                var len = data.obj.data.length;
                if (len > 0) {
                    var canvasSreen = document.getElementById("canvasEl");
                    var ctx = canvasSreen.getContext('2d');
                    ctx.font = "14px Arial";
                    ctx.fillStyle = "#FF2976";
                    ctx.strokeStyle = "#FF2976";//线条的颜色

                    for (var i = 0; i < len; i++) {
                        var x1= data.obj.data[i].coordinate.x;
                        var x2= data.obj.data[i].coordinate.x + data.obj.data[i].coordinate.w;
                        var y1= data.obj.data[i].coordinate.y;
                        var y2= data.obj.data[i].coordinate.y + data.obj.data[i].coordinate.h;
                        ctx.fillText(data.obj.data[i].name, x1, y1+14);
                        ctx.moveTo(x1, y1);
                        ctx.lineTo(x2, y2);
                        ctx.stroke();

                        ctx.moveTo(x2, y1);
                        ctx.lineTo(x1, y2);
                        ctx.stroke();
                    }
                }
            }
        } else {
            alert("检测失败，接口描述：" + data.desc + "\n");
            log = "检测出现异常";
        }
        $(".conntent-result").html(log)
    });
}
