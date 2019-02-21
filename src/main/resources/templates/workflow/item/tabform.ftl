 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 

 <div class="easyui-layout" fit="true">
  <div data-options="region:'center',border:false" style="border-top: 1px solid #D3D3D3;height:200px;" title="填写表格">
    <table id="testtab_dg"></table>
  </div>
</div>


<script>
 <#if data??> 
	 var $datagrid = {};
   	 var columns = new Array();　　 
   <#if result??> 
   	 if ('${result}' == 'failed') {
   	 	 $.messager.alert({title:'提示',msg:'${data}',icon:'info'});
   	 	 $('#tabdig').dialog().panel('close');
   	 	// this.close();
   	 
   	 }else {
   	 
   	 var jsonObj =${data};
  	 var arr = Object.keys(jsonObj[0]);
   
      $datagrid.rownum=true;
      $datagrid.fitColumns=true;
      $datagrid.border = false;
      $datagrid.pagination = true;
      $datagrid.fit = true;
      $datagrid.rownumbers =true;
      $datagrid.resizeHandle ='right';
      
	 
	  $.each(arr, function (i, item) {
	        if(arr[i] != null && typeof(arr[i])!=undefined) {
	           if (i != 0) {
	            columns.push({ "field":arr[i] , "title": arr[i] , "width": 100,align:'center',editor:'text'});
	           }else {
	            columns.push({ "field":arr[i] , "title": arr[i] , "width": 100,align:'center'});
	           }
	          
	        }
	     
	  });
   
    $datagrid.columns = new Array(columns);

   
    $('#testtab_dg').edatagrid($datagrid);
    
    $('#testtab_dg').edatagrid({
	data: jsonObj
    });
   	 
   	 }
   
   </#if>



  </#if>
   
</script>