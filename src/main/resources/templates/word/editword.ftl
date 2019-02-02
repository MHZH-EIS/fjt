<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <head>
        <title>在线测试报告编辑</title>
        <script type="text/javascript">
          	function Save() {
              	document.getElementById("PageOfficeCtrl1").WebSave();
        	}
        </script>
        <script type="text/javascript">
         function AddSeal() {
			try{
        		  document.getElementById("PageOfficeCtrl1").ZoomSeal.AddSeal();
			}catch (e){ };
        	}
  		</script>
        
    </head>
    <body>
      <div class="easyui-layout" style="width:700px;height:350px;">
       <div data-options="region:'north'" style="height:50px"></div>
        <input type="hidden" id="fileName" name="fileName" value="${fileName}" />
        <h1 th:inline="text">测试项目报告在线编辑</h1>
        <br/>
        <div style="width:1000px;height:700px;"  >${pageoffice}</div>
         </div>
    </body>
</html>
