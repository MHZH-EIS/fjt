
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
     
        return dt.format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)
};

define(function () {
  return function () {
    var dg = $("#member_dg");
    var searchFrom = $("#member_search_from");
    var form;

    // 使用edatagrid，需要而外导入edatagrid扩展
    dg.datagrid({
      url: '/system/member/list',
      emptyMsg: "还未创建用户",
      idField: "userid",
      fit: true,
      rownumbers: true,
      fitColumns: true,
      border: false,
      pagination: true,
      singleSelect: true,
      ignore: ['roles'],
      pageSize: 30,
      columns: [[{
        field: 'name',
        title: '姓名',
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
        field: 'sex',
        title: '性别',
        align: 'center',
        width: 20,
        editor: {
          type: 'combobox',
          options: {
            data: [{
              text: '男',
              value: "男"
            }, {
              text: '女',
              value: "女"
            }],
            editable: false,
            required: true,
            panelHeight: 'auto'
          }
        },
        formatter: function (val) {
          return {
            "BOY": "男",
            "GIRL": "女"
          }[val];
        }
      }, {
        field: 'account',
        title: '账号',
        width: 30,
        editor: {
          type: 'validatebox',
          options: {
            required: true,
            validType: 'account'
          }
        },
        formatter: function (val) {
          return filterXSS(val);
        }
      }, {
        field: 'phone',
        title: '电话',
        width: 50,
        editor: {
          type: 'validatebox',
          options: {
            required: true
          }
        }
      }, {
        field: 'email',
        title: '邮箱',
        width: 50,
        editor: {
          type: 'validatebox',
          options: {
            required: true,
            validType: 'email'
          }
        }
      }, {
        field: 'entryTime',
        title: '入职日期',
        formatter:formatDatebox,
        width: 50,
        editor: {
          type: 'datebox',
          options: {
            editable: false
          }
        }
      }, {
        field: 'status',
        title: '状态',
        width: 20,
        align: 'center',
        editor: {
          type: 'checkbox',
          options: {
            on: true,
            off: false
          }
        },
        formatter: function (val, row) {
          return val == 1 ? "可用" : "禁用";
        }
      }, {
        field: 'roleName',
        title: '角色名称',
        width: 80
      },
        {
          field: 'test',
          title: '操作',
          width: 50,
          align: 'center',
          formatter: function (value, row, index) {
            return authToolBar({
              "member-edit": '<a data-id="' + row.id + '" class="ctr ctr-edit">编辑</a>',
              "member-delete": '<a data-id="' + row.id + '" class="ctr ctr-delete">删除</a>'
            }).join("");
          }
        }
      ]],
      toolbar: authToolBar({
        "member-create": {
          iconCls: 'fa fa-plus-square',
          text: "创建",
          handler: function () {
            createForm()
          }
        },
        "member-reset-password": {
          iconCls: 'fa fa-repeat',
          text: "重置密码",
          handler: function () {
            var row = dg.datagrid('getSelected');
            if (row) {
              $.messager.confirm('系统提示', '确定将【' + row.name + "】的密码重置为：0000", function (r) {
                if (r) {
                  $.get("/system/member/password/reset", {id: row.userid}, function (rsp) {
                    $.messager.alert("系统提示", "密码重置成功！");
                  })
                }
              })
            }
          }
        }
      })
    });


    /**
     * 操作按钮绑定事件
     */
    dg.datagrid("getPanel").on('click', "a.ctr-edit", function () {// 编辑按钮事件
    	var row = dg.edatagrid('getSelected');
        createForm.call(this, row.userid);
    }).on('click', "a.ctr-delete", function () {// 删除按钮事件
      $.messager.confirm("删除提醒", "确认删除此用户?", function (r) {
        if (r) {
         var row = dg.edatagrid('getSelected');
          $.get("/system/member/delete", {id: row.userid}, function () {
            // 数据操作成功后，对列表数据，进行刷新
            dg.datagrid("reload");
          });
        }
      });
    });

    /**
     * 搜索区域事件
     */
    searchFrom.on('click', 'a.searcher', function () {//检索
      dg.datagrid('load', searchFrom.formToJson());
    }).on('click', 'a.reset', function () {//重置
      searchFrom.form('reset');
      dg.datagrid('load', {});
    });


    /**
     * 创建表单窗口
     *
     * @returns
     */
    function createForm(id) {
      var row = dg.datagrid('getSelected');
      var dialog = $("<div/>", {class: 'noflow'}).dialog({
        title: (id ? "编辑" : "创建") + "用户",
        iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
        height: id ? 600 : 600,
        width: 420,
        href: '/system/member/form',
        queryParams: {
          id: id
        },
        modal: true,
        onClose: function () {
          $(this).dialog("destroy");
        },
        onLoad: function () {
          //窗口表单加载成功时执行
          form = $("#member-form");
          $("#member_account").textbox('setValue',row.account); 
         // $("#role").combobox('setValue',row.roleName);
          
          //这个字段比较特殊，有比较多的校验，所以单独拿出来实例化
          $("#member_account").textbox({
            label: '账号：',
            required: true,
            validType: ['account', 'length[6, 15]', "remote['/system/member/check','account']"]
          })
        },
        buttons: [{
          iconCls: 'fa fa-save',
          text: '保存',
          handler: function () {
            if (form.form('validate')) {
              $.post("/system/member/save", form.serialize(), function (res) {
                dg.datagrid('reload');
                dialog.dialog('close');
              })
            }
          }
        }]
      });
    }
  }
});