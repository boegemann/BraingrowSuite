var braingrow = ( function() {
	var bg = {}

	var dataHolder = {};

	function exists(value) {
		var notExists;
		return (value !== notExists && value !== null);
	}

	var location = document.location.toString().replace('http://', 'ws://').replace('https://', 'wss://')

	var ws;

	function onWsOpen() {
	}

	function buildContent(contentDef) {
		var newNode;
		if(contentDef.elementType === "text") {
			var newNode = $(document.createTextNode(contentDef.text));
		} else {
			var newNode = $(document.createElement(contentDef.elementType));
			if(exists(contentDef.cssClass)) {
				newNode.addClass(contentDef.cssClass);
			}
			if(exists(contentDef.id)) {
				newNode.attr("id", contentDef.id);
			}
			if(exists(contentDef.name)) {
				newNode.attr("name", contentDef.name);
			}
			if(exists(contentDef.attributes)) {
				newNode.attr(contentDef.attributes);
			}
			if(exists(contentDef.events)) {
				var ec = executeCommands;
				for(event in contentDef.events) {
					if(contentDef.events.hasOwnProperty(event)) {
						newNode.bind(event, function() {
							ec(contentDef.events[event]);
						});
					}
				}
			}
			if(exists(contentDef.content) && exists(contentDef.content.length)) {
				var children = contentDef.content;
				for(var i = 0, l = children.length; i < l; i++) {
					newNode.append(buildContent(children[i]));
				}
			}
		}
		return newNode;
	}

	function executeCommand(command) {
		switch (command.cmd) {
			case "PlaceContent":
				$(command.location).replaceWith(buildContent(command.content));
				break;
			case "SetData":
			    var ownerQuery = document;
			    if (exists(command.ownerQuery)){
			        owner = $(command.ownerQuery);
			    }
				$.data(ownerQuery,command.slot,command.data);
				break;
			case "ShowDialog":
				var dg = buildContent(command.content).dialog({
					title : command.title,
					close : function(ev, ui) {
						$(this).remove();
					}
				});
				break;
			default:
				console.error(command.cmd);
		}
	}

	function executeCommands(commands) {
		for(var i = 0, l = commands.length; i < l; i++) {
			var command = commands[i];
			executeCommand(command);
		}
	}

	function onCommands(msg) {
		if(msg.data) {
			var commands = $.parseJSON(msg.data)
			console.log(commands)
			executeCommands(commands);
		}
	}


	bg.startUp = function() {
		var ws = new WebSocket(location, "braingrow");
		ws.onopen = onWsOpen;
		ws.onmessage = onCommands;
	};
	return bg;
}());

$(document).ready(function() {
	if(!window.WebSocket) {
		window.WebSocket = window.MozWebSocket;
		if(!window.WebSocket)
			alert("WebSocket not supported by this browser");
	}
	braingrow.startUp();
});
