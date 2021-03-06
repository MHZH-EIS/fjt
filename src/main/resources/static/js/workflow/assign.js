
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
    var dg = $("#assign_dg");
 
    var form;
    
    var itemdg = $("#test_items_dg");
 
    

    // 使用edatagrid，需要而外导入edatagrid扩展
    dg.datagrid({
      url: '/workflow/task/display',
      onBeforeSave: function(index) {
    	  return true;
      },
      onSave: function (index, row) {                    
    	   if (dg.data('isSave')) {
    	          //如果需要刷新，保存完后刷新
    	         dg.datagrid('reload');
    	         dg.removeData('isSave');
    	   }
      },
      onLoadSuccess:function(data){
    	  if (data.rows.length != 0) {
    	       // dg.datagrid("selectRow", 0);
          	    //itemdg.datagrid('loadData',{total:0,rows:[]})
    	        //itemdg.datagrid('reload', {projectId: data.projectId});
    	  }
      },
      onClickRow:function(index,data) {
          var selectdata = data;
          if (selectdata) {
        	  itemdg.datagrid('loadData',{total:0,rows:[]})
        	  itemdg.datagrid('reload', {projectId: selectdata.projectId});
           }
      },
      emptyMsg: "还未下卡任务",
      idField: "projectId",
      fit: true,
      rownumbers: true,
      fitColumns: true,
      border: false,
      pagination: true,
      queryParams:{taskName:"下卡"},
      singleSelect: true,
      ignore: ['projectId'],
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
        "test-item-assign":{
            iconCls: 'fa fa-user-circle-o',
            text: "按照选定进行下卡",
            handler: function () {
              var itemrows = itemdg.datagrid("getRows"); 
              var row = dg.edatagrid('getSelected');
              if (!row) {
                $.messager.alert({title:'提示',msg:"请先选一个下卡任务",icon:'info'});
              }else {
                if (itemrows.length == 0) {
              	  $.messager.alert({title:'提示',msg:"还未给此任务分配试验项目",icon:'info'});
              	  return;
                }
                for (var i = 0; i < itemrows.length;i++) {
                  if (itemrows[i].assign == null) {
                    $.messager.alert({title:'提示',msg:"请先给所有测试项分配好工程师后才可以分配试验项目!",icon:'info'});
                    return;
                  }
                }
                var postDatas = [];
                for (var i = 0; i < itemrows.length;i++) {
                  var objectData = {
                                        id: itemrows[i].testId,
                                        projectId: itemrows[i].projectId,
                                        itemId: itemrows[i].itemId,
                                        userId: itemrows[i].assign.split(":")[0],
                                        exName: itemrows[i].testName
                                    };
                  postDatas.push(objectData);
                }

                $.post("/workflow/discard",{"jsonStr":JSON.stringify(postDatas)},function(data){

                    if (data.success) {
                        $.messager.alert({title:'提示',msg:"下卡成功",icon:'info'});
                 	    dg.datagrid('reload');
                 	   itemdg.datagrid('loadData', { total: 0, rows: [] });
                    }else {
                      $.messager.alert({title:'提示',msg:"下卡失败:"+data.data,icon:'error'});
                    } 
                },"json");
              }
            }
        }
    })
    });

    
    
    itemdg.datagrid({
    	url:'/resource/contract/experiment/display/list',
        destroyUrl: '/resource/contract/experiment/remove',
        updateUrl: '/resource/contract/experiment/update',
        saveUrl: '/resource/contract/experiment/update',
        onLoadSuccess:function(data){
            var rows = dg.datagrid("getRows"); 
            if(rows.length == 0) {
            	var itemrows = itemdg.datagrid('getRows');
                for(var i=itemrows.length-1;i>=0;i--){
                	itemdg.datagrid('deleteRow',i);
                }
            }
        },
        //开始不加载数据
        onBeforeLoad: function (param) {
        	var firstLoad = $(this).attr("firstLoad");
            if (firstLoad == "false" || typeof (firstLoad) == "undefined")
            {
                $(this).attr("firstLoad","true");
                return false;
            }
            return true;
        },
        onBeforeSave: function(index) {
      	  return true;
        },
        onSave: function (index, row) {                    
      	   if (itemdg.data('isSave')) {
      	          //如果需要刷新，保存完后刷新
      	         itemdg.datagrid('reload');
      	         itemdg.removeData('isSave');
      	   }
        },
        emptyMsg: "还未查到试验项目",
        idField: "testId",
        fit: true,
        rownumbers: true,
        fitColumns: true,
        border: false,
        pagination: true,
        singleSelect: true,
        pageSize: 10,
        ignore: ['itemId'],
        columns: [[   {
            field: 'projectId',
            title: '项目ID',
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
            field: 'testId',
            title: '测试项ID',
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
            field: 'testName',
            title: '测试项名',
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
                  field: 'requirement',
                  title: '测试要求',
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
            field: 'clause',
            title: '测试条款',
            width: 50,
            editor: {
              type: 'validatebox',
              options: {
                required: true
              }
            } 
          },{
  	        field: 'assign',           
  	        title: '分配测试工程师',
  	        width: 50,
  	        editor: {
  	          type: 'validatebox',
  	          options: {
  	            required: true
  	          }
  	        },
  	        formatter: function (val) {
  	          if(val == null || val == "") {
                val = '<span style="color:red"> 未指定工程师 </span>'
                return val;
              }   
  	          return filterXSS(val);
  	        }
  	      },
          {
  	          field: 'test',
  	          title: '操作',
  	          width: 50,
  	          align: 'center',
  	          formatter: function (value, row, index) {
  	            return authToolBar({
  	              "assign-test": '<a data-id="' + row.id + '" class="ctr ctr-edit">指定测试人员</a>'
  	            }).join("");
  	          }
  	        }
        ]],
        toolbar: authToolBar({
          "test-item-create": {
            iconCls: 'fa fa-plus-square',
            text: "新建试验项目",
            handler: function () {
           	    var row = dg.edatagrid('getSelected');
           	    if (!row) {
     				        $.messager.alert({title:'提示',msg:"请先选一个下卡任务",icon:'info'});
           	    }else {
                	createTestItemForm();
           	    }
            }
          },
          "test-tree-item-create": {
              iconCls: 'fa fa-plus-square',
              text: "新建树状测试项",
              handler: function () {
             	    var row = dg.edatagrid('getSelected');
             	    if (!row) {
       				        $.messager.alert({title:'提示',msg:"请先选一个下卡任务",icon:'info'});
             	    }else {
             	    	createTreeItemFrom();
             	    }
              }
            },
 
          "test-item-delete":{
              iconCls: 'fa fa-trash',
              text: "删除试验项目",
              handler: function () {
              	var row = itemdg.datagrid('getSelected');
              	if (row) {
                      $.messager.confirm("删除提醒", "确认删除此实验项?", function (r) {
                        if (r) {
                          $.get("/resource/contract/experiment/remove", {id: row.testId}, function () {
                            // 数据操作成功后，对列表数据，进行刷新
                        	  itemdg.datagrid("reload");
                          });
                        }
                      });
              	 }else {
              		$.messager.alert({title:'提示',msg:"请先选一个试验项目",icon:'info'});
              	 }
              	 
              }
          }
        })
    });
    
    
    /**
     * 操作按钮绑定事件
     */
    itemdg.datagrid("getPanel").on('click', "a.ctr-edit", function () {// 编辑按钮事件
        var itemrows = itemdg.datagrid("getRows"); 
       	createAssignForm(itemrows);
    });
    
  
    function createAssignForm(itemrows) {
    	 var row = dg.datagrid('getSelected');
    	 var rowitem = itemdg.datagrid('getSelected');
        if (row) {
        var dialog = $("<div/>", {class: 'flow'}).dialog({
          title: "分配任务",
          iconCls: 'fa fa-user-circle-o',
          height: 380,
          width: 420,
          collapsible:true,
          href: '/workflow/item/taskform',
          queryParams: {
            projectId: row.projectId
          },
          modal: true,
          onLoad: function () {
              //窗口表单加载成功时执行
              form = $("#task-form");
 	          $("#testName").textbox('setValue',rowitem.testName); 
	          $("#clause").textbox('setValue',rowitem.clause); 
	          $("#testid").textbox('setValue',rowitem.testId);
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
               type:"get",
               url:"/resource/contract/experiment/update",
               onSubmit: function (param) {        //表单提交前的回调函数 
                      var isValid = $(this).form('validate');//验证表单中的一些控件的值是否填写正确，比如某些文本框中的内容必须是数字 
                      if (!isValid) { 
                        $.messager.alert({title:'提示',msg:"分配校验失败请检查",icon:'error'});
                      } 
                      return isValid; // 如果验证不通过，返回false终止表单提交 
                 }, 
               success:function(data) {
                 var obj = JSON.parse(data);
                 if (obj.success) {
                   $.messager.alert({title:'提示',msg:"分配成功",icon:'info'});
                   dialog.dialog('close');
                   itemdg.datagrid('reload', {projectId: row.projectId});
                 }else {
                   $.messager.alert({title:'提示',msg:obj.message,icon:'error'});
                 }
               }
              },'json');
            }
          }]
        }); 
    	 }else {
    			$.messager.alert({title:'提示',msg:"请先选一个任务再进行分配",icon:'info'});
    	 }
    };


   function createTreeItemFrom(id) {
    var row = dg.edatagrid('getSelected');
    var dialog = $("<div/>", {class: 'flow'}).dialog({
           title: (id ? "编辑" : "创建") + "测试项",
           iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
           height: 680,
           width: 820,
           collapsible:true,
           href: '/workflow/item/treeform',
           queryParams: {
             projectId: row.projectId
           },
           modal: true,
           onLoad: function () {      
	            $("#projectNo").textbox('setValue',row.projectNo); 
	            $("#projectName").textbox('setValue',row.projectName); 
	            $("#projectId").textbox("setValue",row.projectId);
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
                 // 获取需要关联的资源节点
                 var nodes = $("#standard-resource-tree").tree('getChecked', ['checked', 'indeterminate']);
                 var alldata = [];
                 $.each(nodes,function(index,curvalue) {
                	 var childList =$("#standard-resource-tree").tree('getChildren',nodes[index].target);
                	 if (childList == 0) {
     					 var onenode =   new Object();
     					 onenode.projectId= row.projectId;
     					 onenode.projectNo = row.projectNo;
     					 onenode.projectName = row.projectName;
     					 onenode.itemId = nodes[index].itemId;
     					 onenode.exName = nodes[index].testName;
     					 onenode.stId = nodes[index].stId; 
     					 onenode.requirement = $("#requirement_"+nodes[index].itemId).textbox('getValue');
     					 onenode.clause = nodes[index].clause;
     					 onenode.userId = parseInt($("#standard-form_"+nodes[index].itemId).combobox('getValue'));  
     					 alldata.push(onenode);
     				
                	 }
 					 
 				});
             	console.info(JSON.stringify(alldata));
            	 $.ajax({
            		 type:"POST",
            		 url:"/resource/contract/experiment/saves",
            		 data:JSON.stringify(alldata),
                 dataType: "json",
                 contentType: "application/json;charset=UTF-8",
                 success: function(data) {
                         if (data.success) {
                           $.messager.alert({title:'提示',msg:"新建测试项成功",icon:'info'});
                           dialog.dialog('close');
                           itemdg.datagrid('reload', {projectId: row.projectId});
                         }else {
                           $.messager.alert({title:'提示',msg:data.message,icon:'error'});
                         }
                       }

            	 } 
            	 );
             }
           }]
         });
   };
    


   $("#standard-resource-save").on("click", function () {
     if (currentRoelId) {
       // 获取需要关联的资源节点
       var nodes = resourceTree.tree('getChecked', ['checked', 'indeterminate']);
       // 获取节点的ID列表
       var resourceId = [];
       $.each(nodes, function () {
         resourceId.push(this.id);
       });
       var prams = "roleId=" + currentRoelId + "&resourceId=" + resourceId.join("&resourceId=")
       // 发送请求保存关系
       $.post("/system/role/resource/save", prams, function (rsp) {
         if (rsp.success) {
           dg.datagrid("reload");
           $.messager.alert("系统提醒", "保存成功！");
         }
       });
     } else {
       $.messager.alert("系统提醒", "请先选择角色");
     }
   });
    
    
    function createTestItemForm(id) {
    	 var row = dg.edatagrid('getSelected');
    	 var dialog = $("<div/>", {class: 'flow'}).dialog({
    	        title: (id ? "编辑" : "创建") + "测试项",
    	        iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
    	        height: 480,
    	        width: 420,
    	        collapsible:true,
    	        href: '/workflow/item/form',
    	        queryParams: {
    	          projectId: row.projectId
    	        },
    	        modal: true,
    	        onLoad: function () {
    	            //窗口表单加载成功时执行
    	            form = $("#workflow-item-form");
    	            $("#projectNo").textbox('setValue',row.projectNo); 
    	            $("#projectName").textbox('setValue',row.projectName); 
    	            $("#projectId").textbox("setValue",row.projectId);
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
    	        		 type:"get",
    	        		 url:"/resource/contract/experiment/save",
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
    	        				 $.messager.alert({title:'提示',msg:"新建测试项成功",icon:'info'});
    	        				 dialog.dialog('close');
    	        				 itemdg.datagrid('reload', {projectId: row.projectId});
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