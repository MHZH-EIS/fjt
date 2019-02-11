<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="workflow-test-form">
  <div class="field">
    <input class="easyui-textbox" id="experiemntId" name="experiemntId" style="width:100%" data-options="label:'试验项ID:',required:true,readonly:true">
   </div>
    <div class="field">
    <input id="device-form" class="easyui-combobox" style="width:100%" name="devId" data-options="label:'设备选择：',valueField:'devId',panelMaxHeight:200,panelHeight:'auto',textField:'name',url:'/resource/device/listAll',editable:false">
    </div>
</form>
