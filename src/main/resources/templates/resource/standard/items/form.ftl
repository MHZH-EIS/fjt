<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="standard-item-form" enctype="multipart/form-data"  method="post" >
  <input type="hidden" name="itemId">
  <div class="field">
    <input class="easyui-textbox" name="testName" style="width:90%" data-options="label:'测试项名:',required:true">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="clause" style="width:90%" data-options="label:'测试项条款:',required:false ">
  </div>
  <div class="field">
    <input class="easyui-filebox" id="upload" name="templateFile" accept=".docx" style="width:90%" data-options="label:'模板文件上传:',buttonText:'选择',required:true,prompt:'文件上传' ">
  </div>
</form>
 
 <script>	
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

  $("#standard-item-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>)
</script>	