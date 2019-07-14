document.write("<script language=javascript src='/js/handlebars-v4.1.2.js'></script>");

var cTable;
var editFlag = false;
$(document).ready( function () {
    var tpl = $("#tpl").html();
    //预编译模板
    var template = Handlebars.compile(tpl);

    cTable = $("#commodityTable").DataTable({
        ajax: {
            type: "POST",
            url: "apis/api/commodity/list",
            dataSrc: "commodityList.list"
        },
        columns: [
            { "data": "comId", "visible": false},
            { "data": "comNo" },
            { "data": "comName"},
            { "data": "comType"},
            { "data": "comDescription" },
            { "data": "comPrice" },
            { "data": "comStack" },
            {
                "data": "comImgPath",
                render: function (data) {
                    return "<img src='" + data + "' height='100px' width='100px' alt=''>";
                }
            },
            { "data": null , defaultContent: ''}
        ],
        columnDefs: [
            {
                targets: -1,
                sortable: false,
                render: function (data, type, row, meta) {
                    var context =
                        {
                            func: [
                                {"name": "编辑", "fn": "editCommodity(\'" + row.comId + "\',\'" + row.comNo + "\',\'" + row.comName + "\',\'" + row.comType + "\',\'" + row.comPrice + "\',\'" + row.comStack + "\')", "type": "info"},
                                {"name": "删除", "fn": "del(\'" + row.comId + "\')", "type": "danger"}
                            ]
                        };
                    var html = template(context);
                    return html;
                }
            }

        ],
        language: {
            "sProcessing": "处理中...",
            "sLengthMenu": "显示 _MENU_ 项结果",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
            "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            }
        },
    });

    $("#edit").click(function () {
        var data = {
            "comId": $("#comId").val(),
            "comPrice": $("#comPrice").val(),
            "comStack": $("#comStack").val(),
        };
        ajax(JSON.stringify(data));
    });
} );

function editCommodity(comId, comNo, comName, comType, comPrice, comStack) {
    editFlag = true;
    $("#comId").val(comId).attr("disabled",true);
    $("#comNo").val(comNo).attr("disabled",true);
    $("#comName").val(comName).attr("disabled",true);
    $("#comType").val(comType).attr("disabled",true);
    $("#comPrice").val(comPrice);
    $("#comStack").val(comStack);
    $("#editModal").modal("show");
}

function ajax(postJson) {
    $.ajax({
        url: "apis/api/commodity/update",
        data: postJson,
        type: "POST",
        contentType: "application/json",
        cache: !1,
        timeout: 3e4,
        dataType: "json",
        success: function (res) {
            if (res.IsSuccess) {
                alert("编辑成功！\n");
                cTable.ajax.reload();
                $("#editModal").modal("hide");
                clear();
            } else {
                alert("编辑失败！\n");
                $("#editModal").modal("hide");
                clear();
            }
        }
    });
}

function clear() {
    $("#comId").val("").attr("disabled",false);
    $("#comNo").val("").attr("disabled",false);
    $("#comName").val("").attr("disabled",false);
    $("#comType").val("").attr("disabled",false);
    $("#comPrice").val("");
    $("#comStack").val("");
    editFlag = false;
}