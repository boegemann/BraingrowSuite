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

	var history =[]
    function onData(msg) {
        var currentTime = new Date()
        var hours = currentTime.getHours()
        var minutes = currentTime.getMinutes()
        var seconds = currentTime.getSeconds()
        if (minutes < 10)
            minutes = "0" + minutes ;
        if (seconds < 10)
            seconds = "0" + seconds ;
        var data = $.parseJSON(msg.data) ;
        var item =  {dateTime:hours + ":" + minutes + ":" + seconds, data:data} ;

        history.unshift(item);
        if (history.length>10){
            history.pop();
        }
        paint();
    }

    function paint(){
        console.log(history);
        var newTable = $(document.createElement("div"));
        newTable.attr("id", "historyTable");
        newTable.addClass("historyTable");
        for (var cntHistory=0,lHistory=history.length;cntHistory<lHistory;cntHistory++){
            var curHistoryItem = history[cntHistory];
            var divCurList = $(document.createElement("div"));
            divCurList.addClass("historyColumn");
            var divHeader = $(document.createElement("div"));
            divHeader.addClass("columnItem columnHeader");
            divHeader.append(document.createTextNode(curHistoryItem.dateTime))
            divCurList.append(divHeader);
            var list = curHistoryItem.data;
            for (var i=0,l=list.length;i<l;i++){
                var curDiv = $(document.createElement("div"));
                curDiv.addClass("columnItem");
                curDiv.append(document.createTextNode(list[i].word + " (" + list[i].count + ")"));
                divCurList.append(curDiv);
            }
            newTable.append(divCurList);
        }
        $("#historyTable").replaceWith(newTable);
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
