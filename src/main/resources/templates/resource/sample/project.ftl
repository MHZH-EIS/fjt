
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
			.box{
				max-width: 200px;
				height: 200px;
				line-height: 200px;
				text-align: center;
				border: 1px solid black;
			}
			.img{
				max-height: 90%;
				max-width: 90%;
				vertical-align: middle;
			}
</style>
<div class="easyui-layout" fit="true">
  <div data-options="region:'north',border:false" style="height: 70px;padding: 10px;overflow: hidden;" title="项目管理">
    <form id="project_search_from" class="searcher-form">
      <input name="projectNo" class="easyui-textbox field" label="项目编号:" labelWidth="80">
      <input name="projectName" class="easyui-textbox field" label="项目名称:" labelWidth="80">
         <select  class="easyui-combobox" name="status" style="width:200px;" data-options="label:'项目状态:',required:true ">
         <option value="1">未启动项目</option>
         <option value="2">在途项目</option>
         <option value="3">归档项目</option>
           <option value="">所有项目</option>
         </select>
      <a class="easyui-linkbutton searcher" data-options="iconCls:'fa fa-search'">查询</a>
      <a class="easyui-linkbutton reset" data-options="iconCls:'fa fa-repeat'">刷新</a>
    </form>
  </div>
  <div data-options="region:'center',border:false" style="border-top: 1px solid #D3D3D3; height:40%">
    <table id="project_dg"></table>
  </div>
  
   <div  id="display" data-options="region:'south',border:false"  align="center"   style="border-top: 1px solid #D3D3D3;height:60%;" title="流程图展示">
      <table id="display_dg" >
      <td><img id="img1" class="img" src="./images/bg2.jpg"/> </td>
      </table>
   </div>
</div>