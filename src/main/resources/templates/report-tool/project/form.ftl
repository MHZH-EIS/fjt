<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <!-- link rel="stylesheet" href="/css/progressbar.css" -->

<form class="app-form" id="report-tool-form" enctype="multipart/form-data"  method="post" >
    <input type="hidden" name="projectNo">
 
    <div class="field">
        <input class="easyui-textbox" name="projectName" style="width:90%" data-options="label:'项目名称:',required:true">
    </div>
    <div class="field">
        <input class="easyui-textbox" name="reportNo" style="width:90%" data-options="label:'报告编号:',required:false ">
    </div>
   <div class="field">
        <input class="easyui-numberbox" name="ratePower" style="width:90%" data-options="label:'RatePower:',required:false "> W
    </div>
    <div class="field">
        <input class="easyui-textbox" name="dataSpeed" style="width:90%" data-options="label:'数据记录速度:',required:true,min:0,precision: 0">
    </div>
    <div class="field">
        <input class="easyui-textbox" name="trfNo" style="width:90%" data-options="label:'TRF-NO:',required:false ">
    </div>
    <div class="field">
        <input class="easyui-filebox" id="qzoneFile" name="qzoneFile" accept=".csv" style="width:90%" data-options="label:'Q=0:',buttonText:'选择',required:true,prompt:'文件上传' ">
    </div>
    <div class="field">
        <input class="easyui-filebox" id="qplusMaxFile" name="qplusMaxFile" accept=".csv" style="width:90%" data-options="label:'Q=+Max:',buttonText:'选择',required:true,prompt:'文件上传' ">
    </div>
    <div class="field">
        <input class="easyui-filebox" id="qminusMaxFile" name="qminusMaxFile" accept=".csv" style="width:90%" data-options="label:'Q=-Max:',buttonText:'选择',required:true,prompt:'文件上传' ">
    </div>
    <div class="field">
        <input class="easyui-textbox" name="remark" style="width:90%" data-options="label:'备注:',required:false ">
    </div>
</form>

<div id="prog"   style="width:400px;" type="hidden"></div>
 


<script>
    $("#qzoneFilePath").filebox({
            onChange:function(newValue,oldValue) {
                checkFile("#qzoneFilePath");
            }
        }
    );

    $("#qminusMaxFilePath").filebox({
            onChange:function(newValue,oldValue) {
                checkFile("#qminusMaxFilePath");
            }
        }
    );

    $("#qplusMaxFilePath").filebox({
            onChange: function (newValue, oldValue) {
                checkFile("#qplusMaxFilePath");
            }
        }
    );

    function checkFile(idName) //检查文件
    {
        var fileTypes = ['.xls','.csv'];
        var filePath = $(idName).textbox('getValue');
        if (filePath != '')
        {
            var flag = false;
            var fileType = filePath.substring(filePath.lastIndexOf("."));
            if(fileTypes && fileTypes.length>0){

                for (var i = 0; i < fileTypes.length; i++){

                    if(fileTypes[i]==fileType){
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                $.messager.alert({title:'提示',msg:"只可以上传.xls和.csv格式文件!",icon:'info'});
                $(idName).textbox('setValue', '');
                return;
            }
        }
    };

    $("#report-tool-form").form("load",
        <#if resource??>
        ${resource}
        </#if>);

</script>