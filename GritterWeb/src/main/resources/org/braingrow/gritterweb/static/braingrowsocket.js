var wordListSocket = ( function() {
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

                        var tweetNameLink =  $(document.createElement("a"));
                        tweetNameLink.attr("href", "http://twitter.com/#!/" + tweets[cnt].screenName);
                        tweetNameLink.attr("target", "_blank");
                        tweetNameLink.append(document.createTextNode(tweets[cnt].screenName  + " says:"));
                        tweetText.append(tweetNameLink);
                        tweetText.append(document.createElement("br"));


                        var tweetNameLink =  $(document.createElement("a"));
                        tweetNameLink.attr("href", "http://twitter.com/#!/" + tweets[cnt].screenName + "/status/" + tweets[cnt].id);
                        tweetNameLink.attr("target", "_blank");
                        tweetNameLink.append(document.createTextNode(tweets[cnt].text));
                        tweetText.append(tweetNameLink);
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

	var history =[];
    function onData(msg) {
        var data = $.parseJSON(msg.data) ;
        var currentTime = new Date(data.dateTime);
        var hours = currentTime.getHours()
        var minutes = currentTime.getMinutes()
        var seconds = currentTime.getSeconds()
        if (minutes < 10)
            minutes = "0" + minutes ;
        if (seconds < 10)
            seconds = "0" + seconds ;
        var item =  {"dt":data.dateTime, dateTime:hours + ":" + minutes + ":" + seconds, data:data.list} ;

        history.unshift(item);
        if (history.length>10){
            history.pop();
        }
        paint();
    }

	socket = $().createReconnectingWebSocket({
	    onData:onData,
	    relativePath:"ws/words"
	})
	return socket;
}());

var timeZoneSocket = ( function() {
    function paint(){
        var newTable = $(document.createElement("div"));
        newTable.attr("id", "timeZoneHistoryTable");
        newTable.addClass("historyTable");
        for (var cntHistory=0,lHistory=history.length;cntHistory<lHistory;cntHistory++){
            var curHistoryItem = history[cntHistory];
            var divCurList = $(document.createElement("div"));
            divCurList.addClass("historyColumn");
            var divHeader = $(document.createElement("div"));
            divHeader.addClass("columnItem columnHeader");
            divHeader.append(document.createTextNode(curHistoryItem.dateTime + " (" + curHistoryItem.total +")"))
            divCurList.append(divHeader);
            var list = curHistoryItem.data;
            for (var i=0,l=list.length;i<l;i++){
                var curDiv = $(document.createElement("div"));
                curDiv.addClass("columnItem");
                curDiv.addClass(list[i].movement);
                var curWord = list[i].word;
                curDiv.append(document.createTextNode(list[i].word + " (" + list[i].count + " - " + Math.round((list[i].count/curHistoryItem.total)*100) + "%)"));
                divCurList.append(curDiv);
            }
            newTable.append(divCurList);
        }
        $("#timeZoneHistoryTable").replaceWith(newTable);
    }

	var history =[];
    function onData(msg) {
        var data = $.parseJSON(msg.data) ;
        var currentTime = new Date(data.dateTime);
        var hours = currentTime.getHours()
        var minutes = currentTime.getMinutes()
        var seconds = currentTime.getSeconds()
        if (minutes < 10)
            minutes = "0" + minutes ;
        if (seconds < 10)
            seconds = "0" + seconds ;
        var item =  {"dt":data.dateTime,"total":data.total, dateTime:hours + ":" + minutes + ":" + seconds, data:data.list} ;


        history.unshift(item);
        if (history.length>10){
            history.pop();
        }
        paint();
    }

	socket = $().createReconnectingWebSocket({
	    onData:onData,
	    relativePath:"ws/timezones"
	})
	return socket;
}());

$(document).ready(function() {
	$( "#tabs" ).tabs();

	if(!window.WebSocket) {
		window.WebSocket = window.MozWebSocket;
		if(!window.WebSocket)
			alert("WebSocket not supported by this browser");
	}
	wordListSocket.startUp();
	timeZoneSocket.startUp();
});
