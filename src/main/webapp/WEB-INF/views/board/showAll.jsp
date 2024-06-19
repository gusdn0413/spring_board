<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>게시판</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous">
    </script>
    <style>
        .clickTableTR {
            cursor: pointer;
        }
        .clickTableTR:hover>td{
            color: blue;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="main h-100">
        <%--    taglib C jsp 에서 자바 코드 대신 marked-up 형식으로 출력할 수 있도록 해주는 태그--%>
        <%--    대표 for each 반복 items--%>
        <div class="row justify-content-center">
            <div class="col-8 text-center">
                <table class="table table-striped">
                    <tr>
                        <td>글 번호</td>
                        <td colspan="3">제목</td>
                        <td>작성자</td>
                        <td>작성일</td>
                    </tr>
                    <c:forEach items="${list}" var="b">
                    <tr class="clickTableTR" onclick="javascript:location.href='/board/showOne/${b.id}'">
                        <td>${b.id}</td>
                        <td colspan="3">${b.title}</td>
                        <td>${b.nickname}</td>
                        <td><fmt:formatDate value="${b.entryDate}" pattern="yy.MM.dd HH:mm:ss"/> </td>
                    </tr>
                        </c:forEach>
                </table>
            </div>
        </div>
            <div class="row justify-content-end">
                <div class="col-3">
                    <a class="btn btn-outline-success" href="/board/write">글 작성하기</a>
                </div>
            </div>
    </div>
</div>
</body>
</html>