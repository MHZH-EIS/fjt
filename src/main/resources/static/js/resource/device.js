
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
    var dg = $("#device_dg");
    var searchFrom = $("#device_search_from");
    var form;
 
    // 使用edatagrid，需要而外导入edatagrid扩展
    dg.edatagrid({
      url: '/resource/device/list',
      saveUrl: '/resource/device/update',
      updateUrl: '/resource/device/update',
      destroyUrl: '/resource/device/remove',
      onBeforeSave: function(index) {
    	  return true;
      },
      onSave: function (index, row) {                    
    	   if (dg.data('isSave')) {
    	          //如果需要刷新，保存完后刷新
    	         dg.edatagrid('reload');
    	         dg.removeData('isSave');
    	   }
      },
      onClickRow:function(index,data) {
      },
      emptyMsg: "还未查到设备",
      idField: "devId",
      fit: true,
      rownumbers: true,
      fitColumns: true,
      border: false,
      pagination: true,
      singleSelect: true,
      ignore: ['devId'],
      pageSize: 30,
      columns: [[{
          field: 'name',
          title: '设备名称',
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
        },
    	{
        field: 'version',
        title: '规格型号',
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
          field: 'deviceType',
          title: '设备类型',
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
          field: 'adjust',
          title: '是否校准',
          width: 50,
          editor: {
            type: 'validatebox',
            options: {
              required: true
            }
          },
          formatter:  formatDatebox
        },
   	   {
           field: 'validityDate',
           title: '有效期',
           width: 50,
           editor: {
             type: 'datebox',
             options: {
               required: true
             }
           },
           formatter:  formatDatebox
         },
         {
          field: 'cycle',
          title: '校准周期',
          width: 50,
          editor: {
            type: 'validatebox',
            options: {
              required: true
            }
          }
        },
        {
          field: 'status',
          title: '设备状态',
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
        },
        {
            field: 'abnormalState',
            title: '异常状态',
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
          },
      {
          field: 'adjustDate',
          title: '校准日期',
          width: 50,
          formatter:formatDatebox,
          editor: {
            type: 'datebox',
            options: {
              editable: false
            }
           }
			},     
      {
          field: 'org',
          title: '校准机构',
          width: 50,
          editor: {
            type: 'validatebox',
            options: {
              required: true
            }
          }
        },
      {
            field: 'enclosure',
            title: '附件',
            width: 50,
            formatter:formatDatebox,
            editor: {
              type: 'validatebox',
              options: {
                editable: false
              }
        }
	 },{
	        field: 'remarks',
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
        "device-create": {
          iconCls: 'fa fa-plus-square',
          text: "新建设备",
          handler: function () {
            createForm()
          }
        },
       /* "contract-update":{
            iconCls: 'fa fa-pencil-square-o',
            text: "保存修改",
            handler: function () {
                dg.data('isSave', true).edatagrid('saveRow');
            }
        },*/
        "device-delete":{
            iconCls: 'fa fa-trash',
            text: "删除设备",
            handler: function () {
            	var row = dg.edatagrid('getSelected');
            	if (row) {
                    dg.edatagrid('destroyRow');
            	 }
            	 
            }
        }
      })
    });


 
    
    /**
	 * 操作按钮绑定事件
	 */
    dg.datagrid("getPanel").on('click', "a.ctr-edit", function () {// 编辑按钮事件
      createForm.call(this, this.dataset.id)
    }).on('click', "a.ctr-delete", function () {// 删除按钮事件
      var id = this.dataset.id;
      $.messager.confirm("删除提醒", "确认删除?", function (r) {
        if (r) {
          $.get("/resource/device/remove", {id: id}, function () {
            // 数据操作成功后，对列表数据，进行刷新
            dg.datagrid("reload");
          });
        }
      });
    });

    /**
	 * 搜索区域事件
	 */
    searchFrom.on('click', 'a.searcher', function () {// 检索
      dg.datagrid('loadData', { total: 0, rows: [] });
      dg.datagrid('load', searchFrom.formToJson());
    }).on('click', 'a.reset', function () {// 重置
      searchFrom.form('reset');
      dg.datagrid('load', {});
    });


 
    /**
	 * 创建表单窗口
	 * 
	 * @returns
	 */
    function createForm(id) {
      var dialog = $("<div/>", {class: 'flow'}).dialog({
        title: (id ? "编辑" : "创建") + "标准",
        iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
        height: 600,
        width: 420,
        collapsible:true,
        href: '/resource/device/form',
        queryParams: {
          id: id
        },
        modal: true,
        onLoad: function () {
            //窗口表单加载成功时执行
            form = $("#device-form");
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
        		 url:"/resource/device/save",
        		 success:function(data) {
        			 var obj = JSON.parse(data);
        			 if (obj.success) {
        				 $.messager.alert({title:'提示',msg:"新建设备成功",icon:'info'});
        				 dialog.dialog('close');
        				 dg.datagrid('reload');
        			 }else {
        				 $.messager.alert({title:'提示',msg:obj.message,icon:'error'});
        			 }
        		 }
        	  });
          }
        }]
      });
    }
  }
});