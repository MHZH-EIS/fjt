
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
    var dg = $("#rank_dg");
    var searchFrom = $("#rank_search_from");
    var form;

    // 使用edatagrid，需要而外导入edatagrid扩展
    dg.datagrid({
      url: '/system/rank/list',
      saveUrl: '/system/rank/update',
      updateUrl: '/system/rank/update',
      destroyUrl: '/system/rank/delete',
      emptyMsg: "还未有职级",
      idField: "postid",
      fit: true,
      rownumbers: true,
      fitColumns: true,
      border: false,
      pagination: true,
      singleSelect: true,
      pageSize: 30,
      idField: "rankId",
      columns: [[{
        field: 'rankId',
        title: '职务ID',
        width: 30,
        editable: false,
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
        field: 'name',
        title: '职务名称',
        align: 'center',
        editable: false,
        width: 20,
        editor: {
            type: 'validatebox',
            options: {
              required: true
            }
        
          } 
      }, {
        field: 'remarks',
        title: '备注',
        width: 20,
        align: 'center',
        editable: false,
        editor: {
            type: 'validatebox',
            options: {
              required: true
            }
          }
      } ,
        {
          field: 'test',
          title: '操作',
          width: 50,
          align: 'center',
          formatter: function (value, row, index) {
            return authToolBar({
              "rank-edit": '<a data-id="' + row.id + '" class="ctr ctr-edit">编辑</a>',
              "rank-delete": '<a data-id="' + row.id + '" class="ctr ctr-delete">删除</a>'
            }).join("");
          }
        }
      ]],
      toolbar: authToolBar({
        "rank-create": {
          iconCls: 'fa fa-plus-square',
          text: "创建",
          handler: function () {
            createForm()
          }
        } 
      })
    });


    /**
     * 操作按钮绑定事件
     */
    dg.datagrid("getPanel").on('click', "a.ctr-edit", function () {// 编辑按钮事件
    	var row = dg.edatagrid('getSelected');
        createForm.call(this, row.rankId);
    }).on('click', "a.ctr-delete", function () {// 删除按钮事件

      $.messager.confirm("删除提醒", "确认删除此职务?", function (r) {
        if (r) {
        	var row = dg.edatagrid('getSelected');
          $.get("/system/rank/delete", {id: row.rankId}, function () {
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
      var dialog = $("<div/>", {class: 'noflow'}).dialog({
        title: (id ? "编辑" : "创建") + "职级",
        iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
        height: id ? 190 : 210,
        width: 420,
        href: '/system/rank/form',
        queryParams: {
          id: id
        },
        modal: true,
        onClose: function () {
          $(this).dialog("destroy");
        },
        onLoad: function () {
          //窗口表单加载成功时执行
          form = $("#rank-form");
        },
        buttons: [{
          iconCls: 'fa fa-save',
          text: '保存',
          handler: function () {
            if (form.form('validate')) {
              $.post("/system/rank/save", form.serialize(), function (res) {
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