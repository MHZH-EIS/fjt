<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Hello World!</title>
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
        <h1 th:inline="text">word展示</h1>
        <div style="width:1000px;height:700px;" th:utext="${pageoffice}"></div>
    </body>
</html>
