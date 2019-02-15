<div class="easyui-layout" fit="true" >
	<div id="standard-resource-panel" style="width:200px;border-bottom: none;" data-options="headerCls:'noborder',region:'west',collapsible:false" title="请选择试验项目">
		<!-- 资源树 -->
		<ul id="standard-resource-tree" class="easyui-tree" data-options="url:'/resource/standard/listtree',method:'get',animate:true,checkbox:true"></ul>
	</div>
	<div id="test-item-pannel" data-options="headerCls:'noborder',region:'center',collapsible:false" title="内容">
	</div>
</div>