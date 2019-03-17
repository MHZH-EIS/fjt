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
        var timerId;

        // 使用edatagrid，需要而外导入edatagrid扩展
        dg.datagrid({
            url: '/report-tool/project/list',
            destroyUrl: '/report-tool/project/remove',
            updateUrl:'/report-tool/project/save',
            saveUrl:'/report-tool/project/save',
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
                "report-edit": {
                    iconCls: 'fa fa-pencil',
                    text: "编辑项目",
                    handler: function () {
                        var row = dg.edatagrid('getSelected');
                        if (!row) {
                            $.messager.alert({title: '提示', msg: "请先选择个项目", icon: 'info'});
                            return;
                        }
                    	createForm.call(this, row.projectNo);
 
                    }
                },
                "report-refresh": {
                    iconCls: 'fa fa-refresh',
                    text: "刷新项目",
                    handler: function () {
                        dg.edatagrid('reload');
                    }
                },
                "report-download": {
                    iconCls: 'fa fa-download',
                    text: "下载项目报告",
                    handler: function () {
                        var row = dg.edatagrid('getSelected');
                        if (!row) {
                            $.messager.alert({title: '提示', msg: "请先选择个项目", icon: 'info'});
                            return;
                        }
                        
                        $.get("/report-tool/project/downloadquery", {projectNo: row.projectNo}, function (data) {
           
                        	if (! data.success) {
                               $.messager.alert({title:'提示',msg:"项目还未生成报告",icon:'info'});
                               return;
                            }else {
                                var form = $("<form>");//定义一个form表单
                                form.attr("style", "display:none");
                                form.attr("target", "");
                                form.attr("method", "post");
                                form.attr("action", "/report-tool/project/download");

                                var input2 = $("<input>");
                                input2.attr("type", "hidden");
                                input2.attr("name", "projectNo");
                                input2.attr("value", row.projectNo);
                                form.append(input2);
                                $(document.body).append(form);
                                form.submit();//表单提交
                            }
                        });
                        
                        

                    }
                },
                "report-delete": {
                    iconCls:'fa fa-trash',
                    text: "删除项目",
                    handler:function () {
                        var row = dg.datagrid('getSelected');
                        if (row) {
                            $.messager.confirm("删除提醒", "确认删除此项目?", function (r) {
                                if (r) {
                                    $.get("/report-tool/project/remove", {projectNo: row.projectNo}, function () {
                                        // 数据操作成功后，对列表数据，进行刷新
                                        dg.datagrid("reload");
                                    });
                                }
                            });
                        }else {
                            $.messager.alert({title:'提示',msg:"请先选一个项目",icon:'info'});
                        }
                    }
                }
            })
        });

        function createForm(id) {
            var dialog = $("<div/>", {class: 'flow'}).dialog({
                title: (id ? "编辑" : "创建") + "项目",
                iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
                height: 550,
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
                    var progdig;
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
                            $("#report-tool-form").ajaxSubmit({
                                url: "/report-tool/project/save",
                                type: "POST",
                                dataType: 'json',
                                collapsible:true,
                                modal: true,
                                timeout:100000,//超时时间：30秒
                                beforeSend: function () {
                                	 $(".app-form").attr("readonly", "readonly");
                                	$('#prog').progressbar().removeClass('hidden');
                                	var value = $('#prog').progressbar('getValue'); 
                                    timerId = window.setInterval(function(){
                                    	if (value < 100) {
                                       	   value += 5;
                                     	    $('#prog').progressbar('setValue', value);
                                    	}
                                    },1000);
                                   
                                },
                                success: function (obj) {     
                                	$('#prog').progressbar('setValue', 100);
                                    window.clearInterval(timerId);
                                    //progdig.dialog('close');
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