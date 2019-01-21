<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<div class="easyui-layout" fit="true">
  <div data-options="region:'north',border:false" style="height: 70px;padding: 10px;overflow: hidden;" title="标准管理">
    <form id="standard_search_from" class="searcher-form">
      <input name="stNo" class="easyui-textbox field" label="标准编号:" labelWidth="80">
      <input name="name" class="easyui-textbox field" label="标准名称:" labelWidth="80">
      <a class="easyui-linkbutton searcher" data-options="iconCls:'fa fa-search'">查询</a>
      <a class="easyui-linkbutton reset" data-options="iconCls:'fa fa-repeat'">刷新</a>
    </form>
  </div>
  <div data-options="region:'center',border:false" style="border-top: 1px solid #D3D3D3;height:200px;" title="标准文件管理">
    <table id="standard_dg"></table>
  </div>
   <div data-options="region:'south',border:false" style="border-top: 1px solid #D3D3D3;height:200px;" title="测试项管理">
      <table id="items_dg"></table>
   </div>
</div>