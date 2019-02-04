
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
     
        return dt.format("yyyy-MM-dd"); // 扩展的Date的format方法(上述插件实现)
    };

define(function () {
  return function () {
    var dg = $("#project_dg");
    var display = $("#display_dg");
    var searchFrom = $("#project_search_from");
    var form;
    
    // 使用edatagrid，需要而外导入edatagrid扩展
    dg.datagrid({
      url: '/resource/sample/project/list',
      saveUrl: '',
      updateUrl: '',
      destroyUrl: '',
      onBeforeSave: function(index) {
    	  return true;
      },
      onSave: function (index, row) {
    	     if (dg.data('isSave')) {
                   // 如果需要刷新，保存完后刷新
    	                              dg.edatagrid('reload');
    	                              dg.removeData('isSave');
    	                          }
           },
      onDblClickRow:function(index,data) {
               var selectdata = data;
               if(selectdata.status  == 1) {
                   $.messager.alert({title:'提示',msg:"项目未启动，未有流程展示!",icon:'info'});	
            	 // $("#img1").attr("alt","项目未启动，未有流程图片!");
               }else {
            	   $("#img1").attr("src", "workflow/image?projectId="+ selectdata.projectId);
               }
                
           },
      emptyMsg: "还未有项目",
      idField: "projectId",
      fit: true,
      rownumbers: true,
      fitColumns: true,
      border: false,
      pagination: true,
      singleSelect: true,
      ignore: ['projectId'],
      pageSize: 30,
      columns: [[{
        field: 'projectName',
        title: '项目名称',
        width: 50,
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
      } ,
      {
        field: 'contact',
        title: '联系人',
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
        field: 'contactWay',
        title: '联系方式',
        width: 50,
        editable: false,
        editor: {
          type: 'validatebox',
          options: {
            required: true
          }
        }
      }, {
        field: 'clientCompany',
        title: '委托单位',
        width: 50,
        editable: false,
        editor: {
          type: 'validatebox',
          options: {
            required: true 
          }
        }
      },{
          field: 'clientAddress',
          title: '委托单位地址',
          width: 60,
          editable: false,
          editor: {
            type: 'validatebox',
            options: {
              required: true
            }
          }
        } , {
              field: 'sampleNum',
              title: '样品数量',
              width: 50,
              editable: false,
              editor: {
                type: 'validatebox',
                options: {
                  required: true 
                }
              }
            },  
              {
                  field: 'mfName',
                  title: '制造商名称',
                  width: 50,
                  editable: false,
                  editor: {
                    type: 'validatebox',
                    options: {
                      required: true  
                    }
                  }
                },   {
                    field: 'reportType',
                    title: '报告类型',
                    width: 50,
                    editable: false,
                    editor: {
                      type: 'validatebox',
                      options: {
                        required: true  
                      }
                    }
                  },
                  {
                      field: 'testCost',
                      title: '测试费用',
                      width: 50,
                      editable: false,
                      editor: {
                        type: 'validatebox',
                        options: {
                          required: true  
                        }
                      }
                    },
                    {
                        field: 'cfCost',
                        title: '认证费用',
                        width: 50,
                        editable: false,
                        editor: {
                          type: 'validatebox',
                          options: {
                            required: true 
                          }
                        }
                      },
                      {
                          field: 'totalCost',
                          title: '合同总金额',
                          width: 50,
                          editable: false,
                          editor: {
                            type: 'validatebox',
                            options: {
                              required: true  
                            }
                          }
                        },
                        {
                            field: 'sendTotal',
                            title: '样品发送量',
                            width: 50,
                            editable: false,
                            editor: {
                              type: 'validatebox',
                              options: {
                                required: true 
                              }
                            }
                          },
                          {
                              field: 'signTotal',
                              title: '样品接收总量',
                              width: 50,
                              editable: false,
                              editor: {
                                type: 'validatebox',
                                options: {
                                  required: true 
                                }
                              }
                            },
 
            {
        field: 'registeDate',
        title: '登记日期',
        width: 50,
        formatter:formatDatebox,
        editable: false,
        editor: {
          type: 'datebox',
          options: {
              required: true 
          }
        }
      },
      {
          field: 'status',
          title: '项目状态',
          width: 50,
          editable: false,
          editor: {
            type: 'validatebox',
            options: {
                required: true 
            }
          },
          formatter: function (val) {
              return {
                  "1": "未启动项目",
                  "2": "在途项目",
                  "3": "归档项目"
                }[val];
        }
      }
      ]],
      toolbar: authToolBar({
        "project-deploy": {
          iconCls: 'fa fa-caret-square-o-right',
          text: "发起流程",
          handler: function () {
          	var row = dg.edatagrid('getSelected');
        	if (row) {
                var rowIndex = dg.datagrid('getRowIndex', row);
                if ( (row.signTotal != row.sendTotal) ||
                	 (row.signTotal != row.sampleNum)
                  ) {
                   $.messager.alert({title:'提示',msg:"项目中的样品数量不达标无法发起流程!",icon:'info'});	
                   return;
                }
                if (row.status != '1') {
                    $.messager.alert({title:'提示',msg:"项目已经启动不能再次启动!",icon:'info'});	
                    userId = $.session.get("userId");
                    return;
                }
                createAssignForm(row.projectId);
        	 }else  {
        		 $.messager.alert({title:'提示',msg:"请先选中要发起流程的项目",icon:'info'});
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
      $.messager.confirm("删除提醒", "确认删除此用户?", function (r) {
        if (r) {
          $.get("/system/member/delete", {id: id}, function () {
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


    function createAssignForm(id) {
        var dialog = $("<div/>", {class: 'flow'}).dialog({
            title:  "分配工程师",
            iconCls: "fa fa-plus-square",
            height: 150,
            width: 420,
            collapsible:true,
            href: '/resource/project/assignform',
            modal: true,
            onLoad: function () {
                // 窗口表单加载成功时执行
                form = $("#project-form");
                form2 = $("#assign-form");
              },
            onClose: function () {
              $(this).dialog("destroy");
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
            	  if (form.form('validate')) {
                      $.post("/workflow/start", {projectId:id,userId: form2.val()}, function (res) {
                    	  if (res.success) {
                  			 $.messager.alert({title:'提示',msg:"成功启动项目",icon:'info'});
            				  dialog.dialog('close');
            				  dg.datagrid('reload');
            				  $("#img1").attr("src", "workflow/image?projectId="+ id);
                    	  }else {
                  			 $.messager.alert({title:'提示',msg:res.message,icon:'error'});
                    		   dialog.dialog('close');
                               dg.datagrid('reload');
                    	  }
        
                        },"json");
                      
              	}
              }
            }]
          });
    }
    
    
    

    /**
	 * 创建表单窗口
	 * 
	 * @returns
	 */
    function createForm(id) {
      var dialog = $("<div/>", {class: 'flow'}).dialog({
        title: (id ? "编辑" : "创建") + "合同",
        iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
        height: 600,
        width: 420,
        collapsible:true,
        href: '/resource/contract/form',
        queryParams: {
          id: id
        },
        modal: true,
        onLoad: function () {
            // 窗口表单加载成功时执行
            form = $("#contract-form");
          },
        onClose: function () {
          $(this).dialog("destroy");
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
        	  if (form.form('validate')) {
              $.post("/resource/contract/save", form.serialize(), function (res) {
                dg.datagrid('reload');
                dialog.dialog('close');
              },"json")
          	}
          }
        }]
      });
    }
  }
});