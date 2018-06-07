<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><tiles:insertAttribute name="title"/></title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/css/materialize.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/js/materialize.min.js"></script>


    <!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
     -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/custom.css">
    <script src="${pageContext.request.contextPath}/resources/js/init-materialize-forms.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/init-menu.js"></script>
</head>
<body>
<tiles:insertAttribute name="header"/>
<main>
    <div class="container">
        <tiles:insertAttribute name="body" />
    </div>
</main>
<tiles:insertAttribute name="footer"/>
</body>
</html>