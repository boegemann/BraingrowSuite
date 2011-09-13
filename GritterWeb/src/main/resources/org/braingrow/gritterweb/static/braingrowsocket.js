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
        console.log(data);
        var item =  {dateTime:hours + ":" + minutes + ":" + seconds, data:data} ;

        history.unshift(item);
        if (history.length>10){
            history.pop();
        }
        paint();
    }

    function paint(){
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
                curDiv.addClass(list[i].movement);
                var curWord = list[i].word;
                curDiv.append(document.createTextNode(list[i].word + " (" + list[i].count + ")"));
                divCurList.append(curDiv);
                curDiv.bind("click",{word:curWord, list:list[i].statusEvent},function(evt){
                    var word =  evt.data.word;
                    var tweets = evt.data.list;

                    var newTweetList = $(document.createElement("div"));
                    newTweetList.attr("id", "tweets");
                    newTweetList.addClass("tweets");

                    var tweetsHeader = $(document.createElement("div"));
                    tweetsHeader.addClass("sectiontitle");
                    tweetsHeader.append(document.createTextNode("Recorded tweets for '" + word + "':" ));
                    newTweetList.append(tweetsHeader);

                    for (var cnt=0,cntTweets=tweets.length;cnt<cntTweets;cnt++){
                        var curTweet =  $(document.createElement("div"));
                        curTweet.addClass("tweet");
                        var tweetImageLink =  $(document.createElement("a"));
                        tweetImageLink.attr("href", "http://twitter.com/#!/" + tweets[cnt].screenName);
                        tweetImageLink.attr("target", "_blank");
                        var tweetImage = $(document.createElement("img"));
                        tweetImage.attr("src", tweets[cnt].userImage);
                        tweetImage.addClass("tweetImage");
                        tweetImageLink.append(tweetImage);
                        var tweetText = $(document.createElement("div"));
                        tweetText.addClass("tweetText");
                        tweetText.append(document.createTextNode(tweets[cnt].text));
                        curTweet.append(tweetImageLink);
                        curTweet.append(tweetText);
                        newTweetList.append(curTweet);
                    }
                    $("#tweets").replaceWith(newTweetList);
                })
            }
            newTable.append(divCurList);
        }
        $("#historyTable").replaceWith(newTable);
    }

 	var ws;

    var openAttempts = 0;
    var maxTries = 5;

   	function onWsOpen() {
        console.log("Web socket opened succesfully");
   	    openAttempts = 0;
   	}
    function onClose(){
        console.log("Web socket was closed");
        console.log(arguments);
        // as there might be lengthy pauses we need to reconnect when timed out.
        // the maxTries regulates the attempts we do to avoid madness when the server is down .....
        tryReconnect();
    }

    function onError(){
        console.log("Web socket errored");
        console.log(arguments);
        tryReconnect();
    }

    function tryReconnect(){
        if (openAttempts<maxTries){
            connect();
        }
    }

    function connect(){
        openAttempts++;
		var ws = new WebSocket(location, "braingrowsocket");
		ws.onopen = onWsOpen;
		ws.onmessage = onData;
		ws.onclose = onClose;
		ws.onerror = onError;
    }

	bg.startUp = function() {
       connect();
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
