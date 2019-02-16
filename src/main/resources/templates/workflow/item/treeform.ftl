<div class="easyui-layout" fit="true" >
	<div id="standard-resource-panel" style="width:200px;border-bottom: none;" data-options="headerCls:'noborder',region:'west',collapsible:false" title="请选择试验项目">
		<!-- 资源树 -->
		<ul id="standard-resource-tree" class="easyui-tree" data-options="url:'/resource/standard/listtree',method:'get',animate:true,checkbox:true"></ul>
	</div>
	<div id="test-item-pannel" data-options="headerCls:'noborder',region:'center',collapsible:false" title="填写测试要求和指定工程师">
	</div>
</div>
<script>
 function createItemField(data){ 
    
            	var one = '<div style="margin-left:5px; margin-top:5px;border:1px dashed #000 "  overflow: hidden; id="'
				one += data.itemId+'">';
			    one +=  '<div class="field" style="margin-left:5px;"  >';
			    one += '<div style="text-align:center;">'+data.text+'<br/><hr style=" height:2px;border:none;border-top:1px dotted #808080;" /></div>'
         		one +=  '<input class="easyui-textbox" id="requirement_'+data.itemId+'" '
         		one +=  'style="width:50%" data-options="label:\' ';
         		one += ' 测试要求:\',required:true,multiline:true" ' +'id="' + data.itemId+ '">';
				one += '</div>';
				one +=  '<div class="field" style="margin-left:5px; margin-top:5px;border-width:1px" >';
				one += ' <input id="standard-form_'+data.itemId+'"';
				one += ' class="easyui-combobox" style="width:50%" name="userId" data-options="label:\'测试工程师：\',valueField:\'userid\',panelMaxHeight:200,panelHeight:\'auto\',textField:\'name\',url:\'/system/member/testengineers\',editable:false,required:true">'
         		one += '</div>';
				one += '</div>';
				return one;
	 
  }



$('#standard-resource-tree').tree({    
    onCheck: function(data,checked){    
        var childList = $(this).tree('getChildren',data.target);
 
        if (childList.length == 0 ) {
        	if (checked) {
                var one = createItemField(data);
         		var targetObj = $(one).appendTo("#test-item-pannel");
         		$.parser.parse(targetObj);
        	}else {
				$("#"+data.itemId).remove();
			}

        }else {
			if(checked) {
        	 $.each(childList,function(index,curvalue) {
				var data = childList[index];
				   var one = createItemField(data);
        	   var targetObj = $( one).appendTo("#test-item-pannel");
        	   $.parser.parse(targetObj);
        	 });
			}else {
				$.each(childList,function(index,curvalue) {
					$("#"+childList[index].itemId).remove();
				});
			}
        }
    }    
});  
  
</script>