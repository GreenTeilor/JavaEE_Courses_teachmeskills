<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home</title>
    <jsp:include page="dependencies.jsp"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<jsp:include page="header.jsp"/>
<c:if test="${sessionScope.user != null}">
    <jsp:include page="profile.jsp"/>
</c:if>

<div class="row">
    <c:forEach items="${categories}" var="item">
        <div class="col d-flex justify-content-center">
            <div class="card" style="width: 22rem; margin: 20px; background-color: lightgrey">
                <a href="shop?command=category_page&name=${item.name()}"><img src="${item.imagePath()}" class="card-img-top" style="height: 25rem;" alt="..."></a>
                <div class="card-body" style="text-align: center">
                    <h2 class="card-title">${item.name()}</h2>
                    <a href="shop?command=category_page&name=${item.name()}" class="btn btn-primary">Перейти</a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

</body>
</html>
