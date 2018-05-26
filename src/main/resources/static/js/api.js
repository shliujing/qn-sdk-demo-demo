$(function () {
    $("#access-gen-token").click(function () {
        $.post("/api/genAccessToken", {
            ak: $("#ak").val(), sk: $("#sk").val(), url: $("#url").val()
        }, function (data) {
            if (data != null && data.result) {
                $("#token").val(data.obj.token);
                $("#retuen-data").val(data.obj.json);
            } else {
                alert("生成token失败" + data.desc + "\n");
            }
        });
    });

    $("#upload-gen-token").click(function () {
        $.get("/api/genUpToken", {
            ak: $("#upload-ak").val(), sk: $("#upload-sk").val(), bucket: $("#upload-bucket").val(), key: $("#upload-key").val()
        }, function (data) {
            if (data != null && data.result) {
                $("#upload-token").val(data.obj.token);
                $("#upload-retuen-data").val(data.obj.json);
            } else {
                alert("生成token失败" + data.desc + "\n");
            }
        });
    });

    $("#down-gen-token").click(function () {
        $.get("/api/genDownToken", {
            ak: $("#down-ak").val(), sk: $("#down-sk").val(), url: $("#down-url").val(), expires: $("#down-expires").val()
        }, function (data) {
            if (data != null && data.result) {
                $("#down-token").val(data.obj.token);
                $("#down-retuen-data").val(data.obj.json);
            } else {
                alert("生成token失败" + data.desc + "\n");
            }
        });
    });
});

// region 无用

function successGenToken(responseText, statusText, xhr, $form) {
    if (responseText != null && responseText.result) {
        $("#token").val(responseText.obj.token);
        $(".api-hiden").css("display", "block");
    } else {
        alert("生成token失败" + data.desc + "\n");
    }
}

/**
 * 表单验证
 */
function showNotityRequest() {
    var title = $("#title").val();
    if (title == '') {
        alert("请输入标题！");
        $("#title").focus();
        return false;
    }
    var content = $("#content").val();
    if (content == '') {
        alert("请输入内容！");
        $("#content").focus();
        return false;
    }

    if ($("#approvalID").val() == '') {
        alert("请选择审核人！");
        $("#approvalID").focus();
        return false;
    }

    if ($("#recID").val() == '') {
        alert("请选择接收人！");
        $("#recID").focus();
        return false;
    }
}

/**
 * 这个可以公共用的
 */
function showResponse(responseText, statusText, xhr, $form) {
    if (statusText == "success") {
        if (responseText == 1) {
            $.fancybox.close();
            window.location.reload();
        } else {
            alert(responseText);
        }
    } else {
        alert(statusText);
    }
}

// endregion
