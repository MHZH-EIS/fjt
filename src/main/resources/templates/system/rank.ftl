<div class="easyui-layout" fit="true">
  <div data-options="region:'north',border:false" style="height: 80px;padding: 10px;overflow: hidden;" title="职级管理">
    <form id="rank_search_from" class="searcher-form">
      <input name="rankId" class="easyui-textbox field" label="职级ID：" labelWidth="80">
      <input name="name" class="easyui-textbox field" label="职级名称：" labelWidth="80">
      <a class="easyui-linkbutton searcher" data-options="iconCls:'fa fa-search'">检索</a>
      <a class="easyui-linkbutton reset" data-options="iconCls:'fa fa-repeat'">重置</a>
    </form>
  </div>
  <div data-options="region:'center',border:false" style="border-top: 1px solid #D3D3D3">
    <table id="rank_dg"></table>
  </div>
</div>