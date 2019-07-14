$(document).ready(function () {
    $("#login_post").submit(function () {
        $.ajax({
            url: "apis/api/admin/login",
            type: "POST",
            contentType: "application/x-www-form-urlencoded",
            data: $("#login_post").serialize(),
            cache: !1,
            timeout: 3e4,
            dataType: "json",
            success: function (res) {
                if (res.IsSuccess) {
                    window.location.href = "/users.html";
                } else {
                    alert("登录失败\n");
                }
            },
            error: function () {
                alert("登录失败\n");
            }
        });
    });
});