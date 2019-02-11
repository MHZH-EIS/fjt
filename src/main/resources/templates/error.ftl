<!DOCTYPE html>
<html>

<head lang="en">
   <link rel="stylesheet" href="/css/error.css">
    <meta charset="UTF-8" />
    <title>发生了错误</title>
</head>
<body class="error">
    <div th:text="${url!}"></div>
    <#if exception??>
    <div th:text="${exception}"></div>
    </#if>
</body>
</html>