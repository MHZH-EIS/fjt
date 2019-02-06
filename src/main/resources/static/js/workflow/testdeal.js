
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
   
   function loadFilter(data, status) {//重新组织datagrid数据，把符合条件的内容加到定义的json字符串中。
    var value = {
        total: data.total,
        rows: []
    };
    var x = 0;
    for (var i = 0; i < data.rows.length; i++) {
        if (data.rows[i].status2 == status) {
            value.rows[x++] = data.rows[i];
        }
    }
    return value;
}



define(function () {
  return function () {
    var dg = $("#test_dg");
 
    var form;
    var commit =0 ;
    
    var itemdg = $("#device_items_dg");
    
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
      emptyMsg: "还未查到任务",
      idField: "id",
      fit: true,
      rownumbers: true,
      fitColumns: true,
      border: false,
      pagination: true,
      queryParams:{taskName:"测试"},
      singleSelect: true,
      ignore: ['taskId'],
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
          width: 50,
          editor: {
            type: 'datebox',
            options: {
              required: true
            }
          },

        },{
          field: 'assignTime',
          title: '下发时间',
          width: 30,
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
          text: "填写报告",
          handler: function () {
            var row = dg.edatagrid('getSelected');
            if (!row) {
              $.messager.alert({title:'提示',msg:"请先选择个测试任务",icon:'info'});
            }
            POBrowser.openWindowModeless(document.URL+'testtask/editword?filePath='+ encodeURI(encodeURI(row.testFilePath)) , 'width=1200px;height=800px;');
            commit = commit+1;
          }
      },
      "assign-commit": {
          iconCls: 'fa fa-check-square',
          text: "提交完成",
          handler: function () {
              var row = dg.edatagrid('getSelected');
              if (!row) {
                $.messager.alert({title:'提示',msg:"请先选择个测试任务",icon:'info'});
                return;
              }
              $.messager.confirm("提交确认", "确认填写完毕测试报告，确认提交?", function (r) {

                  createTestDealForm();
                  
                });
          }
        }
    })
    });

    
    
    itemdg.datagrid({
    	url:'/device/experiment/display/list',
        destroyUrl: '/device/experiment/remove',
        updateUrl: '/device/experiment/update',
        saveUrl: '/device/experiment/update',
        onBeforeSave: function(index) {
      	  return true;
        },
        onSave: function (index, row) {                    
      	   if (itemdg.data('isSave')) {
      	          //如果需要刷新，保存完后刷新
      	         itemdg.edatagrid('reload');
      	         itemdg.removeData('isSave');
      	   }
        },
        emptyMsg: "还未查到使用设备信息",
        idField: "itemId",
        ignore: ['itemId'],
        fit: true,
        rownumbers: true,
        fitColumns: true,
        border: false,
        pagination: true,
        singleSelect: true,
        pageSize: 10,
        columns: [[   {
            field: 'id',
            title: '试验项目ID',
            width: 30,
            editor: {
              type: 'validatebox',
              options: {
                required: true,
                hidden:true
              }
            },
            formatter: function (val) {
              return filterXSS(val);
            }
          },{
            field: 'name',
            title: '设备名称',
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
            field: 'devNo',
            title: '设备型号',
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
            field: 'deviceType',
                  title: '设备类型',
                  width: 30,
                  editor: {
                    type: 'validatebox',
                    options: {
                      required: true,
                      hidden:true
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
            } 
          } 
        ]],
        toolbar: authToolBar({
          "device-item-create": {
            iconCls: 'fa fa-plus-square',
            text: "添加试验使用设备",
            handler: function () {
           	    var row = dg.edatagrid('getSelected');
           	    if (!row) {
     			   $.messager.alert({title:'提示',msg:"请先选一个测试任务",icon:'info'});
           	    }else {
                	createTestItemForm();
           	    }
            }
          },
 
          "device-item-delete":{
              iconCls: 'fa fa-trash',
              text: "删除试验使用设备",
              handler: function () {
              	var row = itemdg.datagrid('getSelected');
              	if (row) {
                      $.messager.confirm("删除提醒", "确认删除此试验的设备?", function (r) {
                        if (r) {
                          $.get("/device/experiment/remove", {id: row.itemId}, function (data) {
                            if (data.success) {
                              $.messager.alert({title:'提示',msg:"删除试验使用设备成功",icon:'info'});
                            // 数据操作成功后，对列表数据，进行刷新
                            itemdg.datagrid("reload");
                            }
                            else {
                              $.messager.alert({title:'提示',msg:"删除试验使用设备失败："+data.message,icon:'info'});
                            }
                          });
                        }
                      });
              	 }else {
              		   $.messager.alert({title:'提示',msg:"请先选一个试验采用设备",icon:'info'});
              	 }
              }
          }
        })
    });
    
    function createTestDealForm(id) {
      var row = dg.edatagrid('getSelected');
      var dialog = $("<div/>", {class: 'flow'}).dialog({
        title: "设置项目测试结果",
        iconCls: "fa fa-plus-square",
        height: 280,
        width: 300,
        collapsible:true,
        href: '/workflow/item/dealform',
        modal: true,
        onLoad: function () {
               form = $("#set-test-form");
        	   $("#id").textbox('setValue',row.id); 
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
             url:"/resource/contract/experiment/setResult",
             onSubmit: function (param) {        //表单提交前的回调函数 
                    var isValid = $(this).form('validate');//验证表单中的一些控件的值是否填写正确，比如某些文本框中的内容必须是数字 
                    if (!isValid) { 
                      $.messager.alert({title:'提示',msg:"校验失败请检查",icon:'error'});
                    } 
                    return isValid; // 如果验证不通过，返回false终止表单提交 
               }, 
             success:function(data) {
               var obj = JSON.parse(data);
               if (obj.success) {
                 $.messager.alert({title:'提示',msg:"设置检验结果成功!",icon:'info'});
      
               	  var da = {"taskId":row.taskId};
                  $.post("/workflow/completeTask",da,function(data){
                           if (data.success) {
                             $.messager.alert({title:'提示',msg:"测试任务提交务成功",icon:'info'});
                           }else {
                             $.messager.alert({title:'提示',msg:"测试任务提交失败:"+data.message,icon:'error'});
                           }
                       },"json");
            	         dg.edatagrid('reload');
                 dialog.dialog('close');
               }else {
                 $.messager.alert({title:'提示',msg:obj.message,icon:'error'});
               }
             }
            },'json');
          }
        }]
      });
    	
    };

    function createTestItemForm(id) {
    	 var row = dg.edatagrid('getSelected');
    	 var dialog = $("<div/>", {class: 'flow'}).dialog({
    	        title: (id ? "编辑测试项设备" : "给测试项分配新") + "设备",
    	        iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
    	        height: 240,
    	        width: 420,
    	        collapsible:true,
    	        href: '/workflow/item/testform',
    	        queryParams: {
    	          projectId: row.projectId
    	        },
    	        modal: true,
    	        onLoad: function () {
    	            //窗口表单加载成功时执行
    	            form = $("#workflow-test-form");
    	            $("#experiemntId").textbox('setValue',row.id); 
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
    	        		 url:"/device/experiment/save",
    	        		 onSubmit: function (param) {        //表单提交前的回调函数 
    	        	          var isValid = $(this).form('validate');//验证表单中的一些控件的值是否填写正确，比如某些文本框中的内容必须是数字 
    	        	          if (!isValid) { 
    	        	        	  $.messager.alert({title:'提示',msg:"校验失败请检查",icon:'error'});
    	        	          } 
    	        	          return isValid; // 如果验证不通过，返回false终止表单提交 
    	        	     }, 
    	        		 success:function(data) {
    	        			 var obj = JSON.parse(data);
    	        			 if (obj.success) {
    	        				 $.messager.alert({title:'提示',msg:"给测试项分配设备成功",icon:'info'});
    	        				 dialog.dialog('close');
    	        				 itemdg.datagrid('reload', {id: row.id});
    	        			 }else {
    	        				 $.messager.alert({title:'提示',msg:obj.message,icon:'error'});
    	        			 }
    	        		 }
    	        	  },'json');
    	          }
    	        }]
    	      });
    };
  }
});