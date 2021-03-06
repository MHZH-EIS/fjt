<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="sample-sign-form" enctype="multipart/form-data"  method="post">
  <input type="hidden" name="id">
     <div class="field">
      <select id="sign-project-form" value="${projectId!''}"  name="projectId" url="/resource/contract/listwithtext" label="项目:"  style="width:90%"></select>
    </div>
 
   <div class="field">
    <input class="easyui-textbox" name="sampleName" style="width:90%" data-options="label:'样品名称:',required:true ">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="singNum" style="width:90%" data-options="label:'签收数量:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="company" style="width:90%" data-options="label:'供样单位名称:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="address" style="width:90%" data-options="label:'供样单位地址:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="contact" style="width:90%" data-options="label:'供样人:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="phone" style="width:90%" data-options="label:'联系电话:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-datebox" name="signDate" style="width:90%" data-options="label:'供样日期:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="type" style="width:90%" data-options="label:'样品型号:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="status" style="width:90%" data-options="label:'样品状态:',required:true  ">
  </div>
    <div class="field">
    <input class="easyui-filebox" id=upload accept=".docx" name="enclosureFile" style="width:90%" data-options="label:'样品描述上传:',buttonText:'选择',required:true,prompt:'文件上传' ">
  </div>
</form>
<script>
  $("#sign-project-form").combotree();
 
   $("#upload").filebox({
   onChange:function(newValue,oldValue) {
         checkFile();
    }
   }
   );
 
 function checkFile() //检查文件
{
　　var fileTypes = ['.docx'];
　　var filePath = $('#upload').textbox('getValue');
　　if (filePath != '')
　　{
　　　　var flag = false;
　　　　var fileType = filePath.substring(filePath.lastIndexOf("."));
　　　　if(fileTypes && fileTypes.length>0){

　　　　　　for (var i = 0; i < fileTypes.length; i++){

　　　　　　　　if(fileTypes[i]==fileType){
　　　　　　　　　　flag = true;
　　　　　　　　　　break;
　　　　　　　　}
　　　　　　}
　　　　}
　　　　if (!flag) {
　　　　　    $.messager.alert({title:'提示',msg:"只可以上传docx格式文件!",icon:'info'});
　　　　　　$('#upload').textbox('setValue', '');
　　　　　　return;
　　　　}
　　}
};
 
 
  $("#sample-sign-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>);
 
  
</script>