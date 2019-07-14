$(document).ready(function () {
    $("#add_commodity").submit(function () {
        let form = new FormData(document.getElementById('add_commodity'));
        $.ajax({
            url: "apis/api/commodity/new",
            type: "POST",
            contentType: false,
            processData: false,
            data: form,
            cache: !1,
            timeout: 3e4,
            dataType: "json",
            success: function (res) {
                if (res.IsSuccess) {
                    alert("添加成功\n");
                    window.location.href = "/commodities.html";
                } else {
                    alert("添加失败\n");
                }
            },
            error: function () {
                alert("添加失败\n");
            }
        });
    });
});

