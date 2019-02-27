Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
        // millisecond
    }
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

function formatDatebox(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
    }

    return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)
};


define(function () {
    return function () {
        var dg = $("#report_dg");

        var form;
        var commit = 0;


        // 使用edatagrid，需要而外导入edatagrid扩展
        dg.datagrid({
            url: '/report-tool/project/list',
            onBeforeSave: function (index) {
                return true;
            },
            onSave: function (index, row) {
                if (dg.data('isSave')) {
                    //如果需要刷新，保存完后刷新
                    dg.edatagrid('reload');
                    dg.removeData('isSave');
                }
            },
            onClickRow: function (index, data) {
                var selectdata = data;
                if (selectdata) {
                    itemdg.datagrid('loadData', {total: 0, rows: []})

                    itemdg.datagrid('reload', {id: selectdata.id});
                }
            },
            emptyMsg: "还未查到自动报告任务",
            idField: "id",
            fit: true,
            rownumbers: true,
            fitColumns: true,
            border: false,
            pagination: true,
            queryParams: {taskName: ""},
            singleSelect: true,
            ignore: [''],
            pageSize: 30,
            columns: [[{
                field: 'projectNo',
                title: '项目编号',
                width: 30,
                editor: {
                    type: 'validatebox',
                    options: {
                        required: true
                    }
                },
                formatter: function (val) {
                    return filterXSS(val);
                }
            }, {
                field: 'reportNo',
                title: '报告编号',
                width: 50,
                editor: {
                    type: 'validatebox',
                    options: {
                        required: true,
                        hide: true
                    }
                },
                formatter: function (val) {
                    return filterXSS(val);
                }
            },
                {
                    field: 'projectName',
                    title: '项目名称',
                    width: 50,
                    editor: {
                        type: 'validatebox',
                        options: {
                            required: true
                        }
                    },
                    formatter: function (val) {
                        return filterXSS(val);
                    }
                },
                {
                    field: 'submitTime',
                    title: '提交时间',
                    width: 50,
                    editor: {
                        type: 'validatebox',
                        options: {
                            required: true
                        }
                    },
                    formatter: formatDatebox
                },

                {
                    field: 'trfNo',
                    title: 'TRF-NO',
                    width: 30,
                    editor: {
                        type: 'validatebox',
                        options: {
                            required: true
                        }
                    },
                    formmatter: function (val) {
                        return filterXSS(val);
                    }

                }, {
                    field: 'completeReportTime',
                    title: '完成时间',
                    width: 50,
                    editor: {
                        type: 'validatebox',
                        options: {
                            required: true
                        }
                    },
                    formatter: formatDatebox
                }, {
                    field: 'remark',
                    title: '备注',
                    width: 30,
                    editor: {
                        type: 'validatebox',
                        options: {
                            required: true
                        }
                    },
                    formatter: function (val) {
                        return filterXSS(val);
                    }
                }
            ]],
            toolbar: authToolBar({
                "report-submit": {
                    iconCls: 'fa fa-envira',
                    text: "提交资料",
                    handler: function () {
                        createForm();
                    }
                },
                "report-refresh": {
                    iconCls: 'fa fa-refresh',
                    text: "刷新报告",
                    handler: function () {
                        dg.edatagrid('reload');
                    }
                },
                "report-download": {
                    iconCls: 'fa fa-download',
                    text: "下载报告",
                    handler: function () {
                        var row = dg.edatagrid('getSelected');
                        if (!row) {
                            $.messager.alert({title: '提示', msg: "请先选择个项目", icon: 'info'});
                            return;
                        }
                        var form = $("<form>");//定义一个form表单
                        form.attr("style", "display:none");
                        form.attr("target", "");
                        form.attr("method", "post");
                        form.attr("action", "/report-tool/project/download");

                        var input2 = $("<input>");
                        input2.attr("type", "hidden");
                        input2.attr("name", "filePath");
                        input2.attr("value", row.testFilePath);
                        form.append(input2);
                        $(document.body).append(form);
                        form.submit();//表单提交
                    }
                }
            })
        });

        function createForm(id) {
            var dialog = $("<div/>", {class: 'flow'}).dialog({
                title: (id ? "编辑" : "创建") + "项目",
                iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
                height: 450,
                width: 420,
                collapsible:true,
                href: '/report-tool/project/form',
                queryParams: {
                    id: id
                },
                modal: true,
                onLoad: function () {
                    //窗口表单加载成功时执行
                    form = $("#report-tool-form");
                },
                onClose: function () {
                    $(this).dialog("destroy");
                },
                sucess:function(result) {

                },
                buttons: [
                    {
                        iconCls: 'fa fa-trash-o',
                        text: '取消',
                        handler: function(){
                            dialog.dialog('close');
                        }
                    },
                    {
                        iconCls: 'fa fa-save',
                        text: '保存',
                        handler: function () {
                            form.form('submit',{
                                type:"post",
                                url:"/report-tool/project/save",
                                success:function(data) {
                                    var obj = JSON.parse(data);
                                    if (obj.success) {
                                        if (id != null) {
                                            $.messager.alert({title:'提示',msg:"修改项目",icon:'info'});
                                        }else {
                                            $.messager.alert({title:'提示',msg:"提交项目信息成功",icon:'info'});
                                        }

                                        dialog.dialog('close');
                                        dg.datagrid('reload');
                                    }else {
                                        $.messager.alert({title:'提示',msg:obj.message,icon:'error'});
                                        dialog.dialog('close');
                                        dg.datagrid('reload');
                                    }
                                },
                                error :function(data) {
                                    $.messager.alert({title:'提示',msg:'提交失败',icon:'error'});
                                    dialog.dialog('close');
                                    dg.datagrid('reload');
                                }
                            });
                        }
                    }]
            });
        }
    }
});