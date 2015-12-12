require([ "jquery", "json", "template" ], function($, JSON, template) {

	var searchTip = {
	    bindEvent : function() {
		    // show searchTip event
		    $("#inputBox").on("keyup", function(e) {
			    /*
				 * var text = $(this).val() + String.fromCharCode(e.keyCode ||
				 * e.charCode);
				 */
			    if ($(this).val()) {
				    searchTip.sendRequest($(this).val());
			    } else {
				    $("#searchTip").hide();
			    }
		    });

		    // close searchTip event
		    $(document).on("click", ":not(#inputBox, #searchTip,.searchTipOption)", function(e) {
			    $("#searchTip").hide();
		    }).on("click", "#inputBox, #searchTip,.searchTipOption", function(e) {
			    return false;
		    });

		    // searchTip Option event
		    $("#searchTip").on("click", ".searchTipOption", function(e) {
			    $("#inputBox").val($(this).text());
			    $("#searchTip").hide();
			    $("#searchButton").click();
		    })

		    // search button event
		    $("#searchButton").on("click", function(e) {
			    var query = $("#inputBox").val();

			    var url = "./search.html?query=" + encodeURI(query);
			    location.href = url;
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
			        console.log(data);
			        var html = template('searchTemplate', data);
			        if (html) {
				        $("#searchTip").html(html).show();
			        }
		        }
		    })
	    }
	};

	searchTip.bindEvent();
});