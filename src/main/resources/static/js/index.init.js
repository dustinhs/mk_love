/*
$('#btnOnStream').click(function () {
    $.ajax({
        type: 'GET',
        url: '/api/binance/binanceStreamOnff',
        data: {'action': 'on'},
        async: false,
        success: function (data) {
            //console.log(data);

            $("#txtRtnMsg").prepend('----------------------------------------------' + '&#10;');
            $("#txtRtnMsg").prepend('[binanceStreamOnff] ' + '&#10; isOpen : ' + data + '&#10;');
            $("#txtRtnMsg").prepend('----------------------------------------------' + '&#10;');
        },
        error: function (e) {
        }
    });
});
*/

function binanceSocketOnff(flag){
    $.ajax({
        type: 'GET',
        url: '/api/binance/binanceStreamOnff',
        data: {'action': flag},
        async: false,
        /*
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
         */
        success: function (data) {
            //console.log(data);

            $("#txtSysLog").prepend('----------------------------------------------' + '&#10;');
            $("#txtSysLog").prepend('[binanceStreamOnff] ' + '&#10; isOpen : ' + data + '&#10;');
            $("#txtSysLog").prepend('----------------------------------------------' + '&#10;');
        },
        error: function (e) {
        }
    });
}