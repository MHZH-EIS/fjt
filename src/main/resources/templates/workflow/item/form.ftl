<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="workflow-item-form">
  <input type="hidden" id="exName" name="exName" class="easyui-textbox" >
  <div class="field">
    <input class="easyui-textbox" id="projectId" name="projectId" style="width:100%" data-options="label:'项目ID:',required:true,readonly:true">
   </div>
    <div class="field">
    <input class="easyui-textbox" id="projectNo" name="projectNo" style="width:100%" data-options="label:'项目编号:',required:true,readonly:true">
   </div>
   <div class="field">
    <input class="easyui-textbox" id="projectName"  name="projectName" style="width:100%" data-options="label:'项目名称:',required:true,readonly:true ">
   </div> 
    <div class="field">
    <input id="standard-form" class="easyui-combobox" style="width:100%" name="stId" data-options="label:'标准选择：',valueField:'stId',panelMaxHeight:200,panelHeight:'auto',textField:'name',url:'/resource/standard/listall',editable:false">
    </div>
     <div class="field">
    <input id="test-items-form" class="easyui-combobox" style="width:100%" name="itemId" data-options="label:'测试项选择：',valueField:'itemId',panelMaxHeight:200,panelHeight:'auto',textField:'testName',url:'/resource/standard/item/listall',editable:false">
    </div>
</form>
<script>	
 
  $('#test-items-form').combobox({
  	 onLoadSuccess:function(data){
        var array=$(this).combobox("getData");
       for(var item in array[0])
        {
                  if(item=="itemId")
                  {
                    $(this).combobox('select',array[0][item]);
                  }
        }
    },
    onChange:function(newValue,oldValue) {
          var item = $("#test-items-form");
          $("#exName").textbox('setValue',item.combobox("getText")); 
    }
    }
  ); 

/*此处有个bug 选择标准和实际对应的ID老是对应不上*/
  $("#standard-form").combobox({
  	onChange:function(newValue, oldValue) {
      var item = $("#test-items-form");
      item.combobox({
          disable:false,
          url :'/resource/standard/item/list?stId='+$("#standard-form").combobox("getValue"),
          valueField:'itemId',
          textField:'testName',
          onLoadSuccess:function() {
            var va = $(this).combobox("getData");
            for (var item in va[0]) {
              if (item == "itemId") {
                $(this).combobox("select",va[0][item]);
              }
            }
          }
      }).combobox("clear");
  	},
  	 onLoadSuccess:function(data) {
               //默认选中第一个
          var array=$(this).combobox("getData");
          for(var item in array[0]){
                  if(item=="stId"){
                    $(this).combobox('select',array[0][item]);
              }
      }
     }
  }
  )
</script>


