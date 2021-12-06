$(document).ready(function () {
    socketOpen();
});

//socket
let socketUri = 'http://localhost:9123/ws';
const protocol = window.location.protocol;
const hostname = window.location.hostname;
if (hostname == 'localhost') socketUri = 'http://localhost:9123/ws';
else socketUri = 'https://' + hostname + '/ws';

function socketOpen() {

    var sock = new SockJS(socketUri);
    client = Stomp.over(sock); // 1. SockJS를 내부에 들고 있는 client를 내어준다.
    client.debug = null;

    var username = '강민규';

    client.connect(
        {
            'username': username, //header set
        }, function () {
            console.log('[' + username + '] ws connected..');

            //시세 구독
            client.subscribe('/subscribe/binance/trade',
                function (data) {
                    data = JSON.parse(data.body);
                    console.log(data);
                    updateTradeLog(data);
                });

            //온라인유저 구독
            client.subscribe('/subscribe/onlineUsers',
                function (data) {
                    //console.log(data.body);
                    //console.log(JSON.parse(data.body));
                    updateOnlineUser(JSON.parse(data.body));
                });


            client.send('/app/getOnlineUsers', {}, 'null');
        }
    )
}

function updateTradeLog(data) {
    var symbol = data['symbol'];
    var volume = data['quantity'];
    var price = data['price'];
    $("#txtTradeLog").prepend(symbol + ':' + price + '[' + volume + ']' + '&#10;');
}

function updateOnlineUser(data) {
    //console.log(data);
    if (data.length > 0) {
        for (let i = 0; i < data.length; i++) {
            let tmp = data[i];
            let username = tmp['username'];
            let sessionId = tmp['sessionId'];
            $("#txtOnlineUserLog").prepend(username + '[' + sessionId + '] 접속' + '&#10;');
        }

    }
}