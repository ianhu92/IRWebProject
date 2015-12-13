define(function(require, exports) {
	var $ = require("jquery");
	var searchTip = require("searchTip");

	searchTip.setSelector({
	    inputBox : "#inputBox_header",
	    searchTip : "#searchTip_header",
	    searchOption : ".searchOption",
	    searchButton : "#searchButton_header"
	});
	searchTip.bindEvent();

	/**
	 * deprecated!!!! Need to do this in back end.
	 */
	$(".limitURL").each(function(i) {
		len = $(this).text().length;
		if (len > 30) {
			$(this).text($(this).text().substr(0, 30) + '...');
		}
	});
	$(".limitAnswer").each(function(i) {
		len = $(this).text().length;
		if (len > 500) {
			$(this).text($(this).text().substr(0, 500) + '...');
		}
	});
});
