
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
    var dg = $("#standard_dg");
    var searchFrom = $("#standard_search_from");
    var form;
    
    var itemdg = $("#items_dg");
    
    itemdg.edatagrid({
    	url:'/resource/standard/item/list',
    	saveUrl:'/resource/standard/add',
    	updateUrl:'/resource/standard/add',
    	destroyUrl:'/resource/standard/item/delete',
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
        emptyMsg: "还未查到标准测试项",
        idField: "itemId",
        fit: true,
        rownumbers: true,
        fitColumns: true,
        border: false,
        pagination: true,
        singleSelect: true,
        pageSize: 10,
        columns: [[{
            field: 'stId',
            title: '标准号',
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
          field: 'itemId',
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
            field: 'clause',
            title: '条款',
            width: 50,
            editor: {
              type: 'validatebox',
              options: {
                required: true
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
          "standard-item-create": {
            iconCls: 'fa fa-plus-square',
            text: "新建测试项",
            handler: function () {
           	    var row = dg.edatagrid('getSelected');
           	    if (!row) {
     				 $.messager.alert({title:'提示',msg:"请先选一个标准",icon:'info'});
           	    }else {
                	createItemForm();
           	    }

            }
          },
 
          "standard-item-delete":{
              iconCls: 'fa fa-trash',
              text: "删除测试项",
              handler: function () {
              	var row = itemdg.edatagrid('getSelected');
              	if (row) {
              		
              		itemdg.edatagrid('destroyRow');
              		alert(row.itemId);
              		//itemdg.edatagrid('reload');
              	 }else {
              		$.messager.alert({title:'提示',msg:"请先选一个测试项",icon:'info'});
              	 }
              	 
              }
          }
        })
    });
    
    // 使用edatagrid，需要而外导入edatagrid扩展
    dg.edatagrid({
      url: '/resource/standard/list',
      saveUrl: '/resource/standard/update',
      updateUrl: '/resource/standard/update',
      destroyUrl: '/resource/standard/remove',
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

        	  itemdg.datagrid('reload', {stId: selectdata.stId});
           }
      },
      emptyMsg: "还未查到标准",
      idField: "stId",
      fit: true,
      rownumbers: true,
      fitColumns: true,
      border: false,
      pagination: true,
      singleSelect: true,
      ignore: ['stId'],
      pageSize: 30,
      columns: [[{
          field: 'stNo',
          title: '标准号',
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
        field: 'name',
        title: '标准名称',
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
          field: 'switcher',
          title: '格式转换',
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
          field: 'releaseDate',
          title: '发布日期',
          width: 50,
          editor: {
            type: 'datebox',
            options: {
              required: true
            }
          },
          formatter:  formatDatebox
        },{
          field: 'status',
          title: '标准状态',
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
        field: 'cnas',
        title: 'CNAS授权',
        width: 50,
        editor: {
          type: 'validatebox',
          options: {
            required: true
          }
        }
      },
      {
          field: 'cnasDate',
          title: 'CNAS授权日期',
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
          field: 'cb',
          title: 'CB授权',
          width: 50,
          editor: {
            type: 'validatebox',
            options: {
              required: true
            }
          }
        },
      {
            field: 'cbDate',
            title: 'CB授权日期',
            width: 50,
            formatter:formatDatebox,
            editor: {
              type: 'datebox',
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
        "standard-create": {
          iconCls: 'fa fa-plus-square',
          text: "新建标准",
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
        "standard-delete":{
            iconCls: 'fa fa-trash',
            text: "删除标准",
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
          $.get("/resource/standard/remove", {id: id}, function () {
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


    function createItemForm(id) {
    	 var row = dg.edatagrid('getSelected');
    	  var dialog = $("<div/>", {class: 'flow'}).dialog({
    	        title: (id ? "编辑" : "创建") + "测试项",
    	        iconCls: 'fa ' + (id ? "fa-edit" : "fa-plus-square"),
    	        height: 480,
    	        width: 420,
    	        collapsible:true,
    	        href: '/resource/standard/items/form',
    	        queryParams: {
    	          id: row.stId
    	        },
    	        modal: true,
    	        onLoad: function () {
    	            //窗口表单加载成功时执行
    	            form = $("#standard-item-form");
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
    	        		 url:"/resource/standard/item/add",
    	        		 param:row.stId,
    	        		 onSubmit: function (param) {        //表单提交前的回调函数 
    	        	          var isValid = $(this).form('validate');//验证表单中的一些控件的值是否填写正确，比如某些文本框中的内容必须是数字 
    	        	          if (!isValid) { 
    	        	        	  $.messager.alert({title:'提示',msg:"校验失败请检查",icon:'error'});
    	        	          } 
    	        	          param.stId = row.stId;
    	        	          return isValid; // 如果验证不通过，返回false终止表单提交 
    	        	     }, 
    	        		 success:function(data) {
    	        			 var obj = JSON.parse(data);
    	        			 if (obj.success) {
    	        				 $.messager.alert({title:'提示',msg:"新建测试项成功",icon:'info'});
    	        				 dialog.dialog('close');
    	        				 itemdg.datagrid('reload', {stId: row.stId});
    	        			 }else {
    	        				 $.messager.alert({title:'提示',msg:obj.message,icon:'error'});
    	        			 }
    	        		 }
    	        	  });
    	          }
    	        }]
    	      });
    };
    
    

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
        href: '/resource/standard/form',
        queryParams: {
          id: id
        },
        modal: true,
        onLoad: function () {
            //窗口表单加载成功时执行
            form = $("#standard-form");
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
        		 url:"/resource/standard/save",
        		 success:function(data) {
        			 var obj = JSON.parse(data);
        			 if (obj.success) {
        				 $.messager.alert({title:'提示',msg:"新建标准成功",icon:'info'});
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