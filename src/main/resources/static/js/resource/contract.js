
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
    var dg = $("#contract_dg");
    var searchFrom = $("#contract_search_from");
    var form;
    
    // 使用edatagrid，需要而外导入edatagrid扩展
    dg.edatagrid({
      url: '/resource/contract/list',
      saveUrl: '/resource/contract/update',
      updateUrl: '/resource/contract/update',
      destroyUrl: '/resource/contract/delete',
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
      emptyMsg: "还未有合同",
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
        editor: {
          type: 'validatebox',
          options: {
            required: true
          }
        },
        formatter: function (val) {
          return filterXSS(val);
        }
      },{
          field: 'projectVersion',
          title: '项目标识',
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
        field: 'contact',
        title: '联系人',
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
        field: 'contactWay',
        title: '联系方式',
        width: 50,
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
          editor: {
            type: 'validatebox',
            options: {
              required: true 
            }
          }
        }, {
            field: 'verifyMethod',
            title: '检验方式',
            width: 50,
            editor: {
              type: 'validatebox',
              options: {
                required: true 
              }
            }
          }, {
              field: 'sampleNum',
              title: '样品数量',
              width: 50,
              editor: {
                type: 'validatebox',
                options: {
                  required: true 
                }
              }
            }, {
                field: 'cheackType',
                title: '检验类型',
                width: 50,
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
                          editor: {
                            type: 'validatebox',
                            options: {
                              required: true 
                            }
                          }
                        },
                        {
                            field: 'exCase',
                            title: '特殊情况',
                            width: 50,
                            editor: {
                              type: 'validatebox',
                              options: {
                                required: true 
                              }
                            }
                          },
                          {
                              field: 'remarks',
                              title: '备注',
                              width: 50,
                              editor: {
                                type: 'validatebox',
                                options: {
                                  required: true 
                                }
                              }
                            },
           
          {
                    field: 'mfAddress',
                    title: '制造商地址',
                    width: 50,
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
        editor: {
          type: 'datebox',
          options: {
            editable: false
          }
        }
      }
      ]],
      toolbar: authToolBar({
        "contract-create": {
          iconCls: 'fa fa-plus-square',
          text: "创建合同",
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
        "contract-delete":{
            iconCls: 'fa fa-trash',
            text: "删除合同",
            handler: function () {
            	var row = dg.edatagrid('getSelected');
            	if (row) {
                    var rowIndex = dg.datagrid('getRowIndex', row);
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
            //窗口表单加载成功时执行
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