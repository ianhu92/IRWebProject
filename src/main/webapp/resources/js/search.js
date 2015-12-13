require([ "jquery" ], function($, JSON, template) {
	
	
	
	
	
	
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
