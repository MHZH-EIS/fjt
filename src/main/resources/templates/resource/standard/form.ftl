<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="standard-form" enctype="multipart/form-data"  method="post" >
  <div class="field">
    <input class="easyui-textbox" name="stNo" style="width:90%" data-options="label:'标准号:',required:true ">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="name" style="width:90%" data-options="label:'标准名称:',required:true">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="switcher" style="width:90%" data-options="label:'格式转换:',required:false ">
  </div>
   <div class="field">
    <input class="easyui-datebox" name="releaseDate" style="width:90%" data-options="label:'发布日期:',required:true ">
  </div>
   <div class="field">
   <select  class="easyui-combobox" name="status" style="width:90%" data-options="label:'标准状态:',required:true ">
    <option value="在用">在用</option>
    <option value="作废">作废</option>
  </select>
  </div>
   <div class="field">
    <input class="easyui-textbox" name="cnas" style="width:90%" data-options="label:'CNAS授权:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-datebox" name="cnasDate" style="width:90%" data-options="label:'CNAS授权日期:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="cb" style="width:90%" data-options="label:'CB授权:',required:true,min:0,precision:0 ">
  </div>
     <div class="field">
    <input class="easyui-datebox" name="cbDate" style="width:90%" data-options="label:'CB授权日期:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="remarks" style="width:90%" data-options="label:'备注信息:'  ">
  </div> 
  <div class="field">
    <input class="easyui-filebox" name="enclosureFile" style="width:90%" data-options="label:'标准上传:',required:true,prompt:'文件上传' ">
  </div>
</form>
 