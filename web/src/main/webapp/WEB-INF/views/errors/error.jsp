<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Something has gone wrong</title>
</head>
<style>
    #errorcustompage {
        background: url(https://cdn.dribbble.com/users/1078347/screenshots/2799566/oops.png);
        background-repeat: no-repeat;
        background-size: auto;
        background-position: center;
    }

    #errordiv {
        text-align: center;
    }
</style>
<body id="errorcustompage">
<div id="errordiv">
    <p>Request from ${pageContext.errorData.requestURI} is failed</p>
    <p>Status code: ${pageContext.errorData.statusCode}</p>
    <p>Message from exception: ${pageContext.exception.message}</p>
</div>
</body>
</html>
