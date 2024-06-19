<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>${boardDTO.id}번 게시글</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-6 ">
            <table class="table table-striped">
                <tr>
                    <th>글 번호</th>
                    <th>${boardDTO.id}</th>
                </tr>

                <tr>
                    <th>글 번호</th>
                    <th>${boardDTO.id}</th>
                </tr>
                <tr>
                    <th>글 제목</th>
                    <th>${boardDTO.title}</th>
                </tr>
                <tr>
                    <th>작성자</th>
                    <th>${boardDTO.nickname}</th>
                </tr>
                <tr>
                    <th>작성 일</th>
                    <th><fmt:formatDate value="${boardDTO.entryDate}" pattern="yyyy년 MM월 dd일 E요일 HH시 MM분"/></th>
                </tr>
                <tr>
                    <th>수정 일</th>
                    <th><fmt:formatDate value="${boardDTO.modifyDate}" pattern="yyyy년 MM월 dd일 E요일 HH시 MM분"/></th>
                </tr>
                <tr>
                    <th colspan="2" class="text-center">글 내용</th>
                </tr>
                <tr>
                    <th colspan="2" class="text-center">${boardDTO.content}</th>
                </tr>
                <c:if test="${boardDTO.writerId eq login.id}">
                    <tr class="text-center">
                        <td class="text-center" colspan="3">
                            <a class="btn btn-outline-success" href="/board/update/${boardDTO.id}">수정하기</a>
                            <a class="btn btn-outline-danger" href="/board/delete/${boardDTO.id}">삭제하기</a>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="3" class="text-center">
                        <a class="btn btn-outline-secondary" href="/board/showAll">목록으로</a>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>