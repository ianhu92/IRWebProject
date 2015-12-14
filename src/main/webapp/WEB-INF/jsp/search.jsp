<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${query}-SearchAnswer</title>
<jsp:include page="/WEB-INF/jsp/include/html_head.jsp"></jsp:include>

<link href="./resources/css/search.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/include/result_header.jsp"></jsp:include>
	<div class=".container-fluid" style="margin-top: 120px">
		<div class="row">
			<div class="col-md-8 col-md-offset-2 label">
				<p class="text-left">Top Results For You</p>
			</div>

			<c:forEach var="result" items="${resultList}">
				<div class="col-md-8 col-md-offset-2 question">
					<div class="col-md-12 title">
						<a href="${result.url}" class="text-capitalize" target="_blank">${result.title}</a>
					</div>
					<div class="col-md-12 vote" style="margin-top: 20px">
						<span>Votes:</span><span>${result.votes}</span>
					</div>
					<div class="col-md-12 answer" style="margin-top: 5px">
						<p class="limitAnswer text-capitalize">
							<c:out value="${result.answer}" escapeXml="false" />
						</p>
					</div>

					<div class="col-md-12 source">
						<span class="sitename">From ${result.source}</span>
					</div>
				</div>
			</c:forEach>
			<div class="col-md-8 col-md-offset-2 question"></div>
		</div>

		<div class="row footer">
			<div class="col-md-4 col-md-offset-4">
				<nav>
					<ul class="pagination">
						<li><a href="#" aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
						</a></li>
						<li><a href="#">1</a></li>
						<li><a href="#">2</a></li>
						<li><a href="#">3</a></li>
						<li><a href="#">4</a></li>
						<li><a href="#">5</a></li>
						<li><a href="#">6</a></li>
						<li><a href="#">7</a></li>
						<li><a href="#">8</a></li>
						<li><a href="#">9</a></li>
						<li><a href="#">10</a></li>
						<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
						</a></li>
					</ul>
				</nav>
			</div>
		</div>

	</div>

	<!-- researchTip template -->
	<%@ include file="/resources/template/searchTip.tpl"%>

	<script>
		require([ "./resources/js/search.js" ]);
	</script>
</body>
</html>