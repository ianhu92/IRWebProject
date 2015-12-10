<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="./resources/bootstrap-3.3.6-dist/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!-- <script src="./resources/js/utils/jquery-1.11.3.min.js"></script>
<script src="./resources/bootstrap-3.3.6-dist/js/bootstrap.min.js"></script> -->
<script src="./resources/js/utils/require.js"></script>

<link href="./resources/css/utils/header.css" type="text/css" rel="stylesheet" />
<script>
	require.config({
		paths : {
			"jquery" : "./resources/js/utils/jquery-1.11.3.min",
			"json" : "./resources/js/utils/json2",
			"bootstrap" : "./resources/bootstrap-3.3.6-dist/js/bootstrap.min.js"
		},
		shim : {
			"bootstrap" : {
				"deps" : [ 'jquery' ]
			}
		}
	})
</script>
