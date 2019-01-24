<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="sample-sign-form">
     <div class="field">
      <select id="sign-project-form" value="${projectId!''}"  name="projectId" url="/resource/contract/listwithtext" label="项目:"  style="width:100%"></select>
    </div>
 
   <div class="field">
    <input class="easyui-textbox" name="sampleName" style="width:100%" data-options="label:'样品名称:',required:true ">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="singNum" style="width:100%" data-options="label:'签收数量:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="company" style="width:100%" data-options="label:'供样单位名称:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="address" style="width:100%" data-options="label:'供样单位地址:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="contact" style="width:100%" data-options="label:'供样人:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="phone" style="width:100%" data-options="label:'联系电话:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-datebox" name="signDate" style="width:100%" data-options="label:'供样日期:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="type" style="width:100%" data-options="label:'样品型号:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="status" style="width:100%" data-options="label:'样品状态:',required:true  ">
  </div>
</form>
<script>
  $("#sign-project-form").combotree()
</script>