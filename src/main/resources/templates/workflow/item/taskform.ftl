<form class="app-form" id="task-form">
    <input   id="testid" name="id"  type="hidden" style="width:80%" data-options="required:true">
  <div class="field">
    <input class="easyui-textbox" id="testName" name="testName" style="width:80%" data-options="label:'测试名称：',required:true">
  </div>
   <div class="field">
    <input class="easyui-textbox" id="clause" name="clause" style="width:80%" data-options="label:'测试条款：',required:true">
  </div>
   <div class="field">
    <input id="standard-form" class="easyui-combobox" style="width:80%" name="userId" data-options="label:'测试工程师：',valueField:'userid',panelMaxHeight:200,panelHeight:'auto',textField:'name',url:'/system/member/testengineers',editable:false">
    </div>
  <div class="field">
    <input class="easyui-textbox" name="requirement" style="width:80%" data-options="label:'测试要求：',multiline:true,required:true">
  </div>
 
</form>


<script>
  $("#task-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>)
</script>