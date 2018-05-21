$(function () {
    $("#gen-token").click(function () {
        $.get("/api/token", {
            ak: $("#ak").val(), sk: $("#sk").val(), url: $("#url").val()
        }, function (data) {
            if (data != null && data.result) {
                $("#token").val(data.obj.token);
                $(".api-hiden").css("display", "block");
            } else {
                alert("生成token失败" + data.desc + "\n");
            }
        });
    });
});
