<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="contract-form">

  <input type="hidden" name="id">
 
    <div class="field">
    <input class="easyui-textbox" name="projectName" style="width:100%" data-options="label:'项目名称:',required:true">
   </div>
   <div class="field">
    <input class="easyui-textbox" name="projectVersion" style="width:100%" data-options="label:'项目标识:',required:true ">
   </div>
  <div class="field">
    <input class="easyui-textbox" name="contact" style="width:100%" data-options="label:'联系人:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="contactWay" style="width:100%" data-options="label:'联系方式:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="clientCompany" style="width:100%" data-options="label:'委托单位:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="clientAddress" style="width:100%" data-options="label:'委托单位地址:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="verifyMethod" style="width:100%" data-options="label:'检验方式:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-numberbox" name="sampleNum" style="width:100%" data-options="label:'样品数量:',required:true,min:0,precision:0 ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="cheackType" style="width:100%" data-options="label:'检验类型:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="mfName" style="width:100%" data-options="label:'制造商名称:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="reportType" style="width:100%" data-options="label:'报告类型:',required:true ">
  </div>
  <div class="field">
    <input class="easyui-numberbox" name="testCost" style="width:100%" data-options="label:'测试费用:',required:true,min:0  ">
  </div>
  <div class="field">
    <input class="easyui-numberbox" name="cfCost" style="width:100%" data-options="label:'认证费用:',required:true,min:0  ">
  </div>
  <div class="field">
    <input class="easyui-numberbox" name="totalCost" style="width:100%" data-options="label:'合同总金额:',required:true,min:0  ">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="exCase" style="width:100%" data-options="label:'特殊情况:',required:false ">
  </div>
    <div class="field">
    <input class="easyui-textbox" name="mfAddress" style="width:100%" data-options="label:'制造商地址:',required:true ">
  </div>
   <div class="field">
    <input class="easyui-textbox" name="remarks" style="width:100%" data-options="label:'备注:',required:false ">
  </div>
  <div class="field">
    <input class="easyui-datebox" name="registeDate" style="width:100%" data-options="label:'登记日期:',editable:false">
  </div>
</form>
<script>
	<#if member??>
    $(function () {
      //需要延迟一点执行，等待页面所有组件都初始化好，再执行数据初始化
      setTimeout(function () {
        var member = ${member};
        if (member.roles) {
          var roles = [];
          $.each(member.roles, function () {
            roles.push(this.id);
          });
          member.roles = roles.join(",");
        }
        $("#member-form").form("load", member);
      }, 200);
    });
	</#if>
</script>