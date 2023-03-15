<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link rel="stylesheet" href="style.css" type="text/css">
<style type="text/css">
	table {
		width: 100%;
	}
</style>
</head>
<body>
	<%-- 
	<h1>List.jsp</h1>
	 Borad 개수 : ${totCnt }<p>
	 --%>
	 <h1>게시판</h1>
	 	<table>
	 		<tr>
	 			<td><a href="writeForm.do">글쓰기</a></td>
	 		</tr>
	 	</table>
	 	<table>
	 		<tr>
	 			<th>번호</th><th>제목</th><th>작성자</th><th>이메일</th>	<th>IP</th><th>작성일</th><th>조회수</th>
	 		</tr>
	 		<c:if test="${totCnt > 0 }">
	 			<c:forEach var="board" items="${list }">
	 				<tr>
	 				<%-- <td>${numbering }</td> --%>
	 					<td>${startNum }</td>
	 					<td class="left" width=200>
	 						<c:if test="${board.readcount > 20 }">
	 							<img src="images/hot.gif" onmouseover="getDeptName(${board.num})">
	 						</c:if>
	 						<c:if test="${board.re_level > 0 }">
	 							<img src="images/level.gif" width="${board.re_level*10 }">
	 							<img src="images/re.gif">
	 						</c:if>
	 						<a href="content.do?num=${board.num}&pageNum=${currentPage}">${board.subject }</a>
	 					</td>
	 					<td>${board.writer}</td>
	 					<td><a href="mailto:${board.email}">${board.email}</a><td>
	 					<td>${board.ip}</td>
	 					<td>${board.reg_date}</td>
	 					<td>${board.readcount}</td>
	 				</tr>
	 				<c:set var="startNum" value="${startNum - 1 }"/>
	 				<%-- <c:set var="numbering" value="${numbering + 1 }"/> --%>
	 			</c:forEach>
	 		</c:if>
	 		<c:if test="${totCnt == 0 }">
	 			<tr>
	 				<td colspan="7">데이터가 없네</td>
	 			</tr>
	 		</c:if>
	 	</table>
	 	
	 	<div style="text-align: center;">
	 		<c:if test="${startPage > blockSize }">
	 			<a href="list.do?pageNum=${startPage-blockSize }">[이전]</a>		 <!-- 10페이지 넘어가면 이전 보여줌 -->
	 		</c:if>
	 		<c:forEach var="i" begin="${startPage}" end="${endPage }">			
				<a href="list.do?pageNum=${i}">[${i}]</a>	 			
	 		</c:forEach>
	 		<c:if test="${endPage < pageCnt}">
	 			<a href="list.do?pageNum=${startPage+blockSize }">[다음]</a>		<!-- 10페이지 뒤에 더 페이지 있으면 다음 보여줘 -->
	 		</c:if>
	 	</div>
</body>
</html>

<%-- 
위 코드 원본
<h1>게시판</h1>
	<table>
		<tr>
			<td><a href="writeForm.do">글쓰기</a></td>
		</tr>
	</table>
	<table>
		<tr>
			<th>번호</th>	<th>제목</th><th>작성자</th><th>이메일</th>	<th>IP</th><th>작성일</th><th>조회수</th>
		</tr>
       <c:if test="${totCnt > 0 }">
			<c:forEach var="board" items="${list }">
				<tr>
					<td>${numbering }</td>
					<td>${startNum }</td>  
					<td class="left" width=200>
					        <c:if test="${board.readcount > 20}">
							    <img src='images/hot.gif' onmouseover="getDeptName(${board.num})">
						   </c:if>
						    <c:if test="${board.re_level > 0}">
						       	<img src='images/level.gif' width="${board.re_level*10}"> 
					        	<img src='images/re.gif'>
						   </c:if>
						 <a href='content.do?num=${board.num}&pageNum=${currentPage}'>
							${board.subject}</a>
					</td>
					<td>${board.writer}</td>
					<td><a href="mailto:${board.email}">${board.email}</a></td>
					<td>${board.ip}</td>
					<td>${board.reg_date}</td>
					<td>${board.readcount}</td>
				</tr>
				<c:set var="startNum" value="${startNum - 1 }" />
				<c:set var="numbering" value="${numbering + 1 }" />
			</c:forEach>
		</c:if>
		<c:if test="${totCnt == 0 }">
			<tr>
				<td colspan=7>데이터가 없네</td>
			</tr>
		</c:if>	
	</table>

	<div style="text-align: center;">
		<c:if test="${startPage > blockSize }">
			<a href='list.do?pageNum=${startPage-blockSize}'>[이전]</a>
		</c:if>
		<c:forEach var="i" begin="${startPage}" end="${endPage}">
			<a href='list.do?pageNum=${i}'>[${i}]</a>
		</c:forEach>
		<c:if test="${endPage < pageCnt }">
			<a href='list.do?pageNum=${startPage+blockSize}'>[다음]</a>
		</c:if>
	</div>
	 --%>
	
