//You need an anonymous function to wrap around your function to avoid conflict
(function($){
 
    //Attach this new method to jQuery
    $.fn.extend({
         
        //This is where you write your plugin's name
        createReconnectingWebSocket: function(options) {
            var defaults = {
                onData: function(msg){console.log(msg)},
                relativePath: "ws"
            }
            options = $.extend(defaults,options)

            // always handy correct test for existing value ...
            function exists(value) {
                var notExists;
                return (value !== notExists && value !== null);
            }

            var location = document.location.toString()
                .replace('http://', 'ws://')
                .replace('https://', 'wss://')
                .replace("index.html","")
                + options.relativePath

            var ws;

            var openAttempts = 0;
            var maxTries = 5;

            function onWsOpen() {
                console.log("Web socket opened succesfully");
                status="CONNECTED";
                openAttempts = 0;
            }
            function onClose(){
                console.log("Web socket was closed");
                status="CLOSED";
                console.log(arguments);
                if (status=="CONNECTED"){
                    openAttempts = 0;
                }
                // as there might be lengthy pauses we need to reconnect when timed out.
                // the maxTries regulates the attempts we do to avoid madness when the server is down .....
                window.setTimeout(connect,1000*openAttempts)
            }

            function onError(e){
                console.log(["Web socket errored!",e]);
            }

            var ws;
            var status = "CLOSED";

            function connect(){
                if (openAttempts<maxTries){
                    openAttempts++;
                    status = "CONNECTING";
                    try {
                        ws = new WebSocket(location, "braingrowsocket")
                        ws.onopen = onWsOpen;
                        ws.onmessage = options.onData;
                        ws.onclose = onClose;
                        ws.onerror = onError;
                    }catch (e){
                        console.log("Error caught during connection attempt - Server is probably down");
                    };
                }
            }


            var socket = {}
            socket.startUp = function() {
               connect();
            };
            return socket;
        }
    });
     
//pass jQuery to the function,
//So that we will able to use any valid Javascript variable name
//to replace "$" SIGN. But, we'll stick to $ (I like dollar sign: ) )      
})(jQuery);