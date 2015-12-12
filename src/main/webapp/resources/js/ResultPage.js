require([ "jquery"], function($, JSON, template) {
	$(document).ready(function() {
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
});
