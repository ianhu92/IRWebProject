require([ "jquery", "json" ], function($, JSON) {

	var searchTip = {
	    bindEvent : function() {
		    // show dropdownTip event
		    $("#inputBox").on("keypress", function(e) {
			    var text = $(this).val() + String.fromCharCode(e.keyCode || e.charCode);
			    searchTip.sendRequest(text);
		    });

		    // close dropdownTip event
		    $(window).on("click", ":not(#inputBox, #dropdownTip)", function() {
			    $("#dropdownTip").hide();
		    })
	    },
	    sendRequest : function(text) {
		    $.ajax({
		        url : "/searchTip.json",
		        dataType : "json",
		        data : {
			        query : text
		        },
		        success : function(data) {
			        try {
				        data = JSON.parse(data);
				        console.log(data);
			        } catch (e) {
				        console.error("wrong data format.")
			        }
		        }
		    })
	    }
	};

	searchTip.bindEvent();
});