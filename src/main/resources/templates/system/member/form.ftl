<form class="app-form" id="member-form">
  <input type="hidden" name="userid">
 
  <div class="field">
    <input class="easyui-textbox" name="name" style="width:80%" data-options="label:'姓名：',required:true">
    <select class="easyui-combobox" editable="false" data-options="panelHeight:'auto'" name="gender" style="width:18%">
      <option value="BOY">男</option>
      <option value="GIRL">女</option>
    </select>
  </div>
   <div class="field">
    <input class="easyui-textbox" name="account" style="width:100%" data-options="label:'账号：',required:true">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="phone" style="width:100%" data-options="label:'电话：',required:true">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="email" style="width:100%" data-options="label:'邮箱：',required:true,validType:'email'">
  </div>
  <div class="field">
    <input class="easyui-datebox" name="hiredate" style="width:100%" data-options="label:'入职日期：',editable:false">
  </div>
  <div class="field">
    <input id="cc" class="easyui-combobox" style="width:100%" name="roles" data-options="label:'用户角色：',valueField:'id',panelMaxHeight:200,panelHeight:'auto',textField:'roleName',url:'/system/member/roles',editable:false,multiple:true">
  </div>
  <div class="field">
    <label class="textbox-label textbox-label-before">状态：</label>
    <input class="easyui-switchbutton" name="status" data-options="onText:'启用',offText:'禁用',checked:true" value="true">
  </div>
  <div class="field">
    新建用户，默认密码：0000
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