
var data = [];
var t = new Date();
for (var i = 60; i >= 0; i--) {
    var x = new Date(t.getTime() - i * 1000);
    data.push([x, 0, 0, 0]);
}

var g = new Dygraph(document.getElementById("graph"),data,
    {
        drawXGrid: false,
        drawYGrid: true,
        drawXAxis: true,
        drawPoints: false,
        valueRange: [0.0, null],
        labels: ['Time', 'Value', 'Random', 'VxR']
    });

var init_ws = function(wsUrl)
    {
        var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
        var ws = new WS(wsUrl)

        ws.onmessage = function(event)
            {
                var json = JSON.parse(event.data)
                var date = new Date(json.ts)
                console.log(date, json.ts)
                data.push([date, json.val[0], json.val[1], (json.val[0] * json.val[1])]);
                data.shift();
                g.updateOptions( { 'file': data } );
            }
    };