<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<div class="easyui-layout" fit="true">
  <div data-options="region:'north',border:false" style="height: 70px;padding: 10px;overflow: hidden;" title="设备管理">
    <form id="device_search_from" class="searcher-form">
      <input name="name" class="easyui-textbox field" label="设备名称:" labelWidth="80">
      <input name="deviceType" class="easyui-textbox field" label="设备类型:" labelWidth="80">
      <a class="easyui-linkbutton searcher" data-options="iconCls:'fa fa-search'">查询</a>
      <a class="easyui-linkbutton reset" data-options="iconCls:'fa fa-repeat'">刷新</a>
    </form>
  </div>
  <div data-options="region:'center',border:false" style="border-top: 1px solid #D3D3D3;height:200px;" title="设备展示">
    <table id="device_dg"></table>
  </div>
</div>