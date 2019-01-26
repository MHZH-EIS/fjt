<form class="app-form" id="member-form">
  <input type="hidden" name="userid">
 
  <div class="field">
    <input class="easyui-textbox" name="name" style="width:80%" data-options="label:'姓名：',required:true">
    <select class="easyui-combobox" editable="false" data-options="panelHeight:'auto'" name="sex" style="width:18%">
      <option value="BOY">男</option>
      <option value="GIRL">女</option>
    </select>
  </div>
   <div class="field">
    <input class="easyui-textbox" id="member_account" name="account" style="width:100%" data-options="label:'账号：',required:true">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="phone" style="width:100%" data-options="label:'电话：',required:true">
  </div>
  <div class="field">
    <input class="easyui-textbox" name="email" style="width:100%" data-options="label:'邮箱：',required:true,validType:'email'">
  </div>
  <div class="field">
    <input class="easyui-datebox" name="signature" style="width:100%" data-options="label:'入职日期：',editable:false">
  </div>
  <div class="field">
    <input id="post" class="easyui-combobox" style="width:100%" name="rankId" data-options="label:'职级：',valueField:'rankId',panelMaxHeight:200,panelHeight:'auto',textField:'name',url:'/system/rank/list',editable:false">
  </div>
  <div class="field">
    <input id="rank" class="easyui-combobox" style="width:100%" name="postId" data-options="label:'岗位：',valueField:'postId',panelMaxHeight:200,panelHeight:'auto',textField:'name',url:'/system/post/list',editable:false">
  </div>
  <div class="field">
    <input id="cc" class="easyui-combobox" style="width:100%" name="roleId" data-options="label:'用户角色：',valueField:'roleId',panelMaxHeight:200,panelHeight:'auto',textField:'name',url:'/system/member/roles',editable:false">
  </div>
  <div class="field">
    <label class="textbox-label textbox-label-before">状态：</label>
    <input class="easyui-switchbutton" name="status" data-options="onText:'启用',offText:'禁用',checked:true" value="true">
  </div>
    <div class="field">
    <input class="easyui-datebox" name="remarks" style="width:100%" data-options="label:'备注：',editable:false">
  </div>
  <div class="field">
    新建用户，默认密码：0000
  </div>
</form>
<script>
  $("#member-form").form("load",
   <#if resource??> 
    ${resource}
	</#if>)
</script>