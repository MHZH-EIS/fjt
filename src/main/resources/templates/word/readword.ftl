<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <head>
        <title>在线测试报告打印</title>
        <script type="text/javascript">
          	function Save() {
              	document.getElementById("PageOfficeCtrl1").WebSave();
        	}
        	//设置 word 文档全屏
    		function fullScreen() {
      			  document.getElementById("PageOfficeCtrl1").FullScreen = true;
   			 }

   			 //取消全屏
   		    function cancelFullScreen() {
       			 document.getElementById("PageOfficeCtrl1").FullScreen = false;
    		}
    		//签章
            function AddSeal() {
			try{
        		  document.getElementById("PageOfficeCtrl1").ZoomSeal.AddSeal();
			}catch (e){ };
        	}
        	
        	function Print() {
				document.getElementById("PageOfficeCtrl1").ShowDialog(4); 
   			}
        	function modifyPassword() {
        		document.getElementById("PageOfficeCtrl1").ZoomSeal.ShowSettingsBox();
        	}
  		</script>  		 
    </head>
    <body>
      <div class="easyui-layout" style="width:700px;height:350px;">
       <div data-options="region:'north'" style="height:50px"></div>
        <input type="hidden" id="fileName" name="fileName" value="${fileName}" />
        <h1 th:inline="text">试验报告在线打印</h1>
        <br/>
        <div style="width:1000px;height:700px;"  >${pageoffice}</div>
         </div>
    </body>
</html>
