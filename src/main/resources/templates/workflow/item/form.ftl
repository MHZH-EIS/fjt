<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
<form class="app-form" id="workflow-item-form">
  <input type="hidden" name="id">
    <div class="field">
    <input class="easyui-textbox" name="name" style="width:100%" data-options="label:'项目编号:',required:true">
   </div>
   <div class="field">
    <input class="easyui-textbox" name="version" style="width:100%" data-options="label:'项目名称:',required:true ">
   </div> 
    <div class="field">
    <input id="standard-form" class="easyui-combobox" style="width:100%" name="stId" data-options="label:'标准选择：',valueField:'stId',panelMaxHeight:200,panelHeight:'auto',textField:'name',url:'/resource/standard/list',editable:false">
    </div>
     <div class="field">
    <input id="test-items-form" class="easyui-combobox" style="width:100%" name="itemId" data-options="label:'测试项选择：',valueField:'itemId',panelMaxHeight:200,panelHeight:'auto',textField:'testName',url:'/resource/standard/item/list',editable:false">
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
    }
    }
  ); 

  $("#standard-form").combobox({
  	onSelect:function() {
  	      var item = $("#test-items-form");
  	      var id =   $("#standard-form").combobox("getValue")
  		  item.combobox("clear");
  		  var url = "/resource/standard/item/list?"+"stId=" +id;
          item.combobox('reload', url);
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


