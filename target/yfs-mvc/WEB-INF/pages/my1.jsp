<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>yfs test</title>
</head>
<body>
${name}

$name

<%
    String name = (String) request.getAttribute("name");
%>

<%
    out.println(name+"aaa");
%>

<%=name+"bbb"%>

</body>
</html>
