
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
    var dg = $("#verify_dg");
 
    var form;
    var commit =0 ;
    
 
    
    // 使用edatagrid，需要而外导入edatagrid扩展
    dg.datagrid({
      url: '/workflow/task/display',
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
          var selectdata = data;
          if (selectdata) {
        	  itemdg.datagrid('loadData',{total:0,rows:[]})

        	  itemdg.datagrid('reload', {id: selectdata.id});
           }
      },
      emptyMsg: "还未查到待审核的任务",
      idField: "id",
      fit: true,
      rownumbers: true,
      fitColumns: true,
      border: false,
      pagination: true,
      singleSelect: true,
      ignore: ['taskId'],
      queryParams:{taskName:"审核"},
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
          field: 'taskName',
          title: '任务名称',
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
            field: 'taskId',
            title: '任务ID',
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
          field: 'assignName',
          title: '处理人',
          width: 30,
          editor: {
            type: 'datebox',
            options: {
              required: true
            }
          },

        },{
          field: 'assignTime',
          title: '下发时间',
          width: 50,
          editor: {
            type: 'validatebox',
            options: {
              required: true
            }
          },
          formatter:  formatDatebox
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
        "assign-refresh": {
          iconCls: 'fa fa-refresh',
          text: "刷新任务",
          handler: function () {
        	     dg.edatagrid('reload');
          }
        },
        "assign-writedoc": {
          iconCls: 'fa fa-file-word-o',
          text: "审核报告",
          handler: function () {
            var row = dg.edatagrid('getSelected');
            if (!row) {
              $.messager.alert({title:'提示',msg:"请先选择个调整任务",icon:'info'});
            }
            POBrowser.openWindowModeless(document.URL+'testtask/editword?filePath='+ encodeURI(encodeURI(row.testFilePath)) , 'width=1200px;height=800px;');
            commit = commit+1;
          }
      },
      "assign-commit": {
          iconCls: 'fa fa-thumbs-o-up',
          text: "审核完成提交",
          handler: function () {
              var row = dg.edatagrid('getSelected');
              if (!row) {
                $.messager.alert({title:'提示',msg:"请先选择个调整任务",icon:'info'});
                return;
              }
              var itemrows = dg.datagrid("getRows"); 
              /*if (commit < itemrows.length  ) {
            	  $.messager.alert({title:'提示',msg:"待审核文档未审核完成，需要审核"+itemrows.length+"个报告",icon:'error'});
              } */
              if (itemrows.length == 0 ) {
            	  $.messager.alert({title:'提示',msg:"未有要审核的报告",icon:'error'});
              }
              $.messager.confirm("提交提醒", "确认提交?", function (r) {
                if (r) {
                   $.post("/workflow/completeTask",{"taskId":row.taskId},function(data){
 
                      if (data.success) {
                        $.messager.alert({title:'提示',msg:"审核提交成功",icon:'info'});
                  	    dg.datagrid('reload');
                      }else {
                        $.messager.alert({title:'提示',msg:"审核提交失败:"+data.message,icon:'error'});
                  	    dg.datagrid('reload');
                      }
                  },"json");
                }
              });
        
          }
        }
    })
    });
  }
});