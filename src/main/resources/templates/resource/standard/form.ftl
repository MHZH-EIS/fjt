<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="standard-form">
   <div class="field">
    <input class="easyui-textbox" name="stId" style="width:100%" data-options="label:'标准号:',required:true ">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="name" style="width:100%" data-options="label:'标准名称:',required:true">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="switcher" style="width:100%" data-options="label:'格式转换:',required:false ">
  </div>
   <div class="field">
    <input class="easyui-datebox" name="releaseDate" style="width:100%" data-options="label:'发布日期:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="status" style="width:100%" data-options="label:'标准状态:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="cnas" style="width:100%" data-options="label:'CNAS授权:',required:true ">
  </div>
  
   <div class="field">
    <input class="easyui-datebox" name="cnasDate" style="width:100%" data-options="label:'CNAS授权日期:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="cb" style="width:100%" data-options="label:'CB授权:',required:true,min:0,precision:0 ">
  </div>
     <div class="field">
    <input class="easyui-datebox" name="cbDate" style="width:100%" data-options="label:'CB授权日期:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="remark" style="width:100%" data-options="label:'备注信息:',required:true ">
  </div>
   
  <div class="field">
    <input class="easyui-filebox" id="fileImport" name="enclosureFile"   style="width:100%" data-options="label:'标准上传:'">
  </div>
</form>
 