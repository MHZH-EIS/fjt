<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="set-test-form">
   <input type="hidden" id="id" name="id" class="easyui-textbox" >
    <div class="field">
    <input id="result" class="easyui-combobox" style="width:100%" name="result" data-options="label:'测试结果：',valueField:'value',panelMaxHeight:200,panelHeight:'auto',textField:'value',editable:false,data: [{
			label: '检验通过',
			value: '检验通过'
		},{
			label: '检验未通过',
			value: '检验未通过'
		}]">
    </div>
</form>
