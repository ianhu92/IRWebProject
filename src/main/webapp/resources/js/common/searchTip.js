define(function(require, exports, module) {
	var $ = require("jquery");
	var template = require("template");

	// define searchTip
	var searchTip = {
	    selector : {
	        inputBox : "",
	        searchTip : "",
	        searchOption : "",
	        searchButton : ""
	    },
	    setSelector : function(obj) {
		    this.selector = obj;
	    },
	    bindEvent : function() {
		    // show searchTip event
		    $(this.selector.inputBox).on("keyup", function(e) {
			    if ($(this).val()) {
				    searchTip.sendRequest($(this).val());
			    } else {
				    $(searchTip.selector.searchTip).hide();
			    }
		    });

		    // close searchTip event
		    $(document).on(
		            "click",
		            ":not(" + this.selector.inputBox + ", " + this.selector.searchTip + ","
		                    + this.selector.searchOption + ")", function(e) {
			            $(searchTip.selector.searchTip).hide();
		            }).on("click",
		            this.selector.inputBox + ", " + this.selector.searchTip + "," + this.selector.searchOption,
		            function(e) {
			            return false;// equivalent to e.stopPropagation
		            });

		    // searchTip Option event
		    $(this.selector.searchTip).on("click", ".searchTipOption", function(e) {
			    $(searchTip.selector.inputBox).val($(this).text());
			    $(searchTip.selector.searchTip).hide();
			    $(searchTip.selector.searchButton).click();
		    });

		    // search button event
		    $(this.selector.searchButton).on("click", function(e) {
			    var query = $(searchTip.selector.inputBox).val();

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
		        	// data type must be json
			        
			        var html = template('searchTemplate', data);
			        if (html) {
				        $(searchTip.selector.searchTip).html(html).show();
			        }
		        }
		    })
	    }
	};
	module.exports = searchTip;
});