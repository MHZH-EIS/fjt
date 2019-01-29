<form class="app-form" id="task-form">
  <div class="field">
    <input class="easyui-textbox" name="name" style="width:80%" data-options="label:'职级名称：',required:true">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="remarks" style="width:100%" data-options="label:'备注：',required:true">
  </div>  
</form>


<script>
  $("#task-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>)
</script>