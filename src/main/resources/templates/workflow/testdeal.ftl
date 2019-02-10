<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script>
/**
 * 使用方法:
 * 开启:MaskUtil.mask();
 * 关闭:MaskUtil.unmask();
 * 
 * MaskUtil.mask('其它提示文字...');
 */
var MaskUtil = (function(){
	
	var $mask,$maskMsg;
	
	var defMsg = '正在处理，请稍待。。。';
	
	function init(){
		if(!$mask){
			$mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");
		}
		if(!$maskMsg){
			$maskMsg = $("<div class=\"datagrid-mask-msg mymask\">"+defMsg+"</div>")
				.appendTo("body").css({'font-size':'12px'});
		}
		
		$mask.css({width:"100%",height:$(document).height()});
		
		var scrollTop = $(document.body).scrollTop();
		
		$maskMsg.css({
			left:( $(document.body).outerWidth(true) - 190 ) / 2
			,top:( ($(window).height() - 45) / 2 ) + scrollTop
		}); 
				
	}
	
	return {
		mask:function(msg){
			init();
			$mask.show();
			$maskMsg.html(msg||defMsg).show();
		}
		,unmask:function(){
			$mask.hide();
			$maskMsg.hide();
		}
	}
	
}());
</script>

 <!--PageOffice.js和jquery.min.js文件一定要引用-->
 <script type="text/javascript" src="pageoffice.js" id="po_js_main"></script>
<div class="easyui-layout" fit="true">
  <div data-options="region:'center',border:false" style="border-top: 1px solid #D3D3D3;height:200px;" title="待测试任务">
    <table id="test_dg"></table>
  </div>
   <div data-options="region:'south',border:false" style="border-top: 1px solid #D3D3D3;height:200px;" title="使用设备信息">
      <table id="device_items_dg"></table>
   </div>
</div>

 