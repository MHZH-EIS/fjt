<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="sample-send-form">
  <input type="hidden" name="id">
    <input type="hidden" name="projectNo" id = "projectNo">
    <div class="field">
      <select id="send-project-form" value="${projectId!''}" id="projectId" name="projectId" url="/resource/contract/listwithtext" label="项目:"  style="width:90%"></select>
    </div>
   
   <div class="field">
    <input class="easyui-datebox" name="sendDate" style="width:90%" data-options="label:'发送日期:',required:true ">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="track" style="width:90%" data-options="label:'快递单号:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="contact" style="width:90%" data-options="label:'发货联系人:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="phone" style="width:90%" data-options="label:'联系电话:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="sendNum" style="width:90%" data-options="label:'发货数量:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="remarks" style="width:90%" data-options="label:'备注:'  ">
  </div>
</form>
<script>
  $("#send-project-form").combotree();
  
  $("#sample-send-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>);
</script>
