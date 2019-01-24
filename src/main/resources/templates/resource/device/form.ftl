<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="device-form">
  <input type="hidden" name="id">
    <div class="field">
    <input class="easyui-textbox" name="name" style="width:100%" data-options="label:'设备名称:',required:true">
   </div>
   <div class="field">
    <input class="easyui-textbox" name="version" style="width:100%" data-options="label:'规格型号:',required:true ">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="deviceType" style="width:100%" data-options="label:'设备类型:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="adjust" style="width:100%" data-options="label:'是否校准:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-datebox" name="validityDate" style="width:100%" data-options="label:'有效期:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="cycle" style="width:100%" data-options="label:'校准周期:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="status" style="width:100%" data-options="label:'设备状态:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="abnormalState" style="width:100%" data-options="label:'异常状态:',required:true  ">
  </div>
  <div class="field">
    <input class="easyui-datebox" name="adjustDate" style="width:100%" data-options="label:'校准日期:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="org" style="width:100%" data-options="label:'校准机构:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="enclosure" style="width:100%" data-options="label:'附件:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="remarks" style="width:100%" data-options="label:'备注:',required:false ">
  </div> 
</form>


<script>
  $("#device-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>)
</script>


