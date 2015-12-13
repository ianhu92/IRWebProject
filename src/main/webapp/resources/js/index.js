define(function(require, exports) {
	var searchTip = require("searchTip");

	searchTip.setSelector({
	    inputBox : "#inputBox",
	    searchTip : "#searchTip",
	    searchOption : ".searchOption",
	    searchButton : "#searchButton"
	});
	searchTip.bindEvent();
});