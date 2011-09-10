var braingrowsocket = ( function() {
	var bg = {}

	var dataHolder = {};

	function exists(value) {
		var notExists;
		return (value !== notExists && value !== null);
	}

	var location = document.location.toString()
	    .replace('http://', 'ws://')
	    .replace('https://', 'wss://')
	    .replace("index.html","")
	    + "ws"


	var ws;

   	function onWsOpen() {
   	}

	var colors = [
	    "#7FFF00",
	    "#6495ED",
	    "#DC143C",
	    "#00008B",
	    "#00008B",
	    "#00BFFF",
	    "#9400D3",
	    "#ADFF2F",
	    "#ADD8E6",
	    "#000000"
	];
	var chartData = {
        type : "line",
        defaultAxis : {
          labels : true
         }
    };
    var values = {};
    var series = {};

	function onData(msg) {
        if(msg.data) {
			var dataSeries = $.parseJSON(msg.data)

            if (dataSeries.length>0) {
                // get the newest series for labels and to analyse the rest
                var newest = dataSeries[0] ;

                var legendHolder = $(document.createElement("div"));
                legendHolder.attr("id", "legendholder");
                i=0;
                for (key in newest) {
                    if (newest.hasOwnProperty(key))  {
                        i++; // need it for the series number;
                        var curSeries = [];
                        for (var j=0,l2=dataSeries.length;j<l2;j++){
                            var value = dataSeries[j][key];
                            curSeries.push (exists(value)?value:null);
                        }
                        values["serie"+i] = curSeries;

                        series["serie"+i] = {color:colors[i-1]};
                    }
                    var legendEntry = $(document.createElement("li"));
                    legendEntry.css('color', colors[i-1]);
                    legendEntry.append($(document.createTextNode(key + " (" + newest[key] +")") ));
                    legendHolder.append(legendEntry);
                }
                $("#legendholder").replaceWith(legendHolder);
                chartData.values=values;
                chartData.series=series;

                $("#chart").chart(chartData);
                console.log(chartData);
			}
		}
	}


	bg.startUp = function() {
		var ws = new WebSocket(location, "braingrowsocket");
		ws.onopen = onWsOpen;
		ws.onmessage = onData;
	};
	return bg;
}());

$(document).ready(function() {
	if(!window.WebSocket) {
		window.WebSocket = window.MozWebSocket;
		if(!window.WebSocket)
			alert("WebSocket not supported by this browser");
	}
	braingrowsocket.startUp();
});
