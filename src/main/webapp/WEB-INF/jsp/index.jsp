<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SearchAnswer</title>
<jsp:include page="/WEB-INF/jsp/include/html_header.jsp"></jsp:include>

<link href="./resources/css/index.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>

	<div id="container" class=".container-fluid">
		<%-- <jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include> --%>

		<div class="row">
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
			<div class="col-xs-10 col-sm-8 col-md-6">
				<h1 class="text-primary text-center">SearchAnswer</h1>
			</div>
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
		</div>
		<div class="row">
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
			<div class="col-xs-10 col-sm-8 col-md-6">
				<input id="inputBox" type="text" class="form-control input-lg" id="usr" />
				<button id="searchButton" type="button" class="btn btn-primary btn-lg">Search</button>
			</div>
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
		</div>
		<div class="row">
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
			<div class="col-xs-10 col-sm-8 col-md-6 dropdown">
				<ul id="dropdownTip" class="dropdown-menu">
					<li><a href="#">HTML</a></li>
					<li><a href="#">CSS</a></li>
					<li><a href="#">JavaScript</a></li>
				</ul>
			</div>
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
		</div>
	</div>
	<script src="./resources/js/index.js"></script>
</body>
</html>
