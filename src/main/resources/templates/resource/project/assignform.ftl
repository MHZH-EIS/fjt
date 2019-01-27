<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="project-form">
   <div class="field">
    <input id="assign-form" class="easyui-combobox" style="width:100%" name="userid" data-options="label:'工程师：',valueField:'userid',panelMaxHeight:200,panelHeight:'auto',textField:'name',url:'/system/member/engineers',editable:false">
  </div>
    
</form>

<script>
  $("#assign-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>)
</script>


