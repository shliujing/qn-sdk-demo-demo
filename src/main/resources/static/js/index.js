var isCheck = true;

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

        if (data != null && data.result) {
            alert("检测成功，接口描述：" + data.desc + "\n" +
                "检测结果：" + data.obj.result + "\n" +
                "错误图片1 id：" + data.obj.data[0].id + "\n" +
                "错误图片1 name：" + data.obj.data[0].name + "\n" +
                "错误图片1 trueImgUrl：" + data.obj.data[0].trueImgUrl + "\n" +
                "错误图片1 wrongImgUrl：" + data.obj.data[0].wrongImgUrl);
            if (data.obj.result) {
                log = "检测成功";
                $("#quaNum").html(data.obj.quaNum);
            } else {
                log = "检测失败";
                data.obj.data.forEach(function (item,index) {
                    log += "</br><span style='color:red'>" + item.name + " 错误</span>";
                });
                $("#unQuaNum").html(data.obj.unQuaNum);
            }
        } else {
            alert("检测失败，接口描述：" + data.desc + "\n");
            log = "检测出现异常";
        }
        $(".conntent-result").html(log)
    });
}

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

            if (pos >= 4 * w * h) {
                ctx.putImageData(img, 0, 0);
                $.post("/main/testWebCam?t=" + new Date().getTime(), {
                    type: "pixel",
                    image: canvas.toDataURL("image/jpeg")
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
                $.post("/main/testWebCam?", {type: "pixel", image: image.join('|')}, function (data) {
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
        swffile: $("#jsbase").attr("src") + "jscam.swf",
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
