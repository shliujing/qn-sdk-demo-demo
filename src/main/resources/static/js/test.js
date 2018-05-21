// region 检测操作

// 打开检测modal
function operator_check() {
    $('#user-check').modal({
        backdrop: "static"
    });
}

// 测试	检测
function testCheck() {
    $.post("/main/testCheck", {
        id: "1",
    }, function (data) {
        if (data != null && data.result) {
            alert("检测成功，接口描述：" + data.desc + "\n" +
                "检测结果：" + data.obj.result + "\n" +
                "错误图片1 id：" + data.obj.data[0].id + "\n" +
                "错误图片1 name：" + data.obj.data[0].name + "\n" +
                "错误图片1 trueImgUrl：" + data.obj.data[0].trueImgUrl + "\n" +
                "错误图片1 wrongImgUrl：" + data.obj.data[0].wrongImgUrl)
        } else {
            alert("检测失败，接口描述：" + data.desc + "\n");
        }
    });
}

// 测试	分割图片
function testCutImage() {
    $.post("/main/testCutImage", {
        id: "1",
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
    var w = 320, h = 240;
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
                $.post("/main/testWebCam?t=" + new Date().getTime(), {type: "pixel", image: canvas.toDataURL("image/jpeg")}, function (data) {
                    if (data != null && data.result) {
                        console.log("====" + data);
                        pos = 0;
                        $("#img").attr("src", data.obj.imgUrl);
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
                        $("#img").attr("src", data.obj.imgUrl);
                    } else {
                        alert("获取摄像头图像失败：" + data.desc);
                    }
                });
            }
        };
    }

    // todo  url 优化
    $("#webcam").webcam({
        width: w,
        height: h,
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
