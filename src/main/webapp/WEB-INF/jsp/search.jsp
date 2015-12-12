<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${query} - SearchAnswer</title>
<jsp:include page="/WEB-INF/jsp/include/html_header.jsp"></jsp:include>

<link href="./resources/css/search.css" type="text/css" rel="stylesheet" />


</head>
<body>
	<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
	<div id="container" class=".container-fluid">
		<div class="row">

			<%
				for (int i = 0; i < 10; i++) {
			%>
			<div class="col-md-8 col-md-offset-2">
				<div class="col-md-12 title">
					<a href="Input quetion URL here">
						<h3 class="text-primary">
							Question <%=i%></h3>
					</a>
				</div>
				<div class="col-md-12 source">
					<span> <small class="text-info url limitURL">www.stackoverflow.com</small>
					</span> <span> <small class="sitename">Stack Overflow</small>
					</span>
				</div>
				<div class="col-md-12 answer">
					<p class="limitAnswer">Invited. Unfortunately, I am not Li, so I don't have his perspective
						and a strategic height. Just from I personal level to on about: Baidu of search technology
						should not poor, from technology level Shang said, Baidu is not fear 360 of, from user stick
						degrees Shang for, please, both are nothing user stick degrees, but has user habits of
						differences, believes everyone are listening to had that jokes, user open browser, default is
						Google, entered Baidu, then into Baidu for search. Search using Baidu such a habit, ingrained,
						so some users can not be considered. So, I need to think about, why 360 dare play this hand?
						In fact, the problem is on the site navigation. Most people do not understand, then listed for
						Baidu, generates the most traffic, providing maximum contribution, is around hao123,hao123 on
						Baidu's revenue contribution in the tune. And the end of 2011, 360 Web site navigation of PV
						and UV are already beyond the hao123. After all, the 360 is still relying on China's Internet
						40%-50% very basic users (assuming more than 360 of the more than 200 million users are using
						360 Explorer and 360 navigation and other search engine used was not used, here I don't have
						small white) and Baidu compete. Then, we change the thinking. Whether Baidu has allowed more
						than 200 million UV fully eaten by 360? I think we should not allow, then Baidu can do what?
						1, brand and sub-brands. Continued and sustained by Internet users know what Baidu, Baidu
						search can do, why they should be the first choice of search engine Baidu. Hao123 should be
						more focused on the stage, and should firmly seize the Internet such a place basis competing
						for users than any Internet terminal. Suppose that all Internet cafes all the default page is
						hao123, no 360 Web site navigation, what will happen? 2, strengthening cooperation with, other
						browsers such as Maxthon, and even search engine sogou, if cooperation can be reached directly
						with Microsoft directly, IE with Bing and preinstalled system, results won't be bad. 3, to
						forge an alliance with the other navigation station or investment, few navigation Web site
						support, presumably this would also help to 360. 4, and actually, although hao123 of share was
						360 over has, but hao123 of revenue and no received too more of effect, so on Baidu for, may
						need as soon as possible station in hao123 such of main to do some things, and on Baidu main
						site, actually without consider too more, has formed habits of user short-term within is
						unlikely to may change habits, but Web site navigation if lost has, so this field war in I
						seems, on didn't of playing has. These personal views, laughed at.</p>
				</div>
			</div>

			<%
				}
			%>

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
	<script src="./resources/js/search.js"></script>
</body>
</html>