<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="device-form">
  <input type="hidden" name="id">
    <div class="field">
    <input class="easyui-textbox" id="devNo" name="devNo" style="width:90%" data-options="label:'设备编号:',required:true">
   </div>
    <div class="field">
    <input class="easyui-textbox" id="name" name="name" style="width:90%" data-options="label:'设备名称:',required:true">
   </div>
   <div class="field">
    <input class="easyui-textbox" id="version" name="version" style="width:90%" data-options="label:'规格型号:',required:true ">
   </div>
  <div class="field">
    <input class="easyui-textbox" id="deviceType" name="deviceType"  style="width:90%" data-options="label:'设备类型:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" id="adjust"  name="adjust"  style="width:90%" data-options="label:'是否校准:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-datebox" id="validityDate"  name="validityDate" style="width:90%" data-options="label:'有效期:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" id="cycle"  name="cycle"  style="width:90%" data-options="label:'校准周期:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" id="status"  name="status"  style="width:90%" data-options="label:'设备状态:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" id="abnormalState" name="abnormalState"  style="width:90%" data-options="label:'异常状态:',required:true  ">
  </div>
  <div class="field">
    <input class="easyui-datebox" id="adjustDate"  name="adjustDate"  style="width:90%" data-options="label:'校准日期:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" id="org" name="org"  style="width:90%" data-options="label:'校准机构:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" id="enclosure"  name="enclosure"  style="width:90%" data-options="label:'附件:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" id="remarks" =name="remarks"  style="width:90%" data-options="label:'备注:',required:false ">
  </div> 
</form>

 
	
<script>
  $("#device-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>)
</script>


