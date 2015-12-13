<!-- start header -->
<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header col-sm-2 col-md-2">
			<a class="navbar-brand" href="./index.html">SearchAnswer</a>
		</div>
		<div class="col-sm-6 col-md-6">
			<div class="navbar-form" role="search">
				<div class="input-group col-sm-12 col-md-12">
					<input id="inputBox_header" type="text" class="form-control" placeholder="Search"
						value="${query == null ? '' : query}" />
					<div id="searchTipBox" class="dropdown">
						<ul id="searchTip_header" class="dropdown-menu">
							<li>aaaa</li>
						</ul>
					</div>
					<div class="input-group-btn">
						<button id="searchButton_header" class="btn btn-default">
							<i class="glyphicon glyphicon-search"></i>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</nav>

<!-- end header -->