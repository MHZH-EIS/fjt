<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="set-test-form">
   <input type="hidden" id="id" name="id" class="easyui-textbox" >
    <div class="field">
    <input id="result" class="easyui-combobox" style="width:100%" name="result" data-options="label:'测试结果：',valueField:'value',panelMaxHeight:200,panelHeight:'auto',textField:'value',editable:false,data: [{
			id:1,
			label: '检验通过',
			value: '检验通过'
		},{
		    id:2,
			label: '检验未通过',
			value: '检验未通过'
		}]">
    </div>
</form>
<script>
   $('#result').combobox({
  	 onLoadSuccess:function(data){
     var array=$(this).combobox("getData");
     $('#result').combobox('select',data[0].value);
    }
   }
  ); 
</script>