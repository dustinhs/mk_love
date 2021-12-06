package com.pinkcobra.auto.binance;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class BinanceClientController {

    private static Log log = LogFactory.getLog(BinanceClientController.class);


    @Autowired
    BinanceParser binanceParser;
    
    private static final int TIMEOUT = 5000; // 타임아웃 시간
    final String SERVER = "wss://stream.binance.com:9443/ws"; // 커넥션 주소 변경되거나 하면 이 부분 수정
    public static WebSocket ws;


    @ResponseBody
    @RequestMapping(value = "api/binance/binanceStreamOnff", method = RequestMethod.GET)
    public Boolean binanceStreamOnff(@RequestParam(value = "action") String action) throws Exception {


        log.info("[binanceStreamOnff] action : " + action);

        if(action.equals("on")){
            onConnect();
        }else if(action.equals("off")){
            onClose();
        }else{
            return false;
        }
        return isOpen();
    }

    @ResponseBody
    @RequestMapping(value = "api/binance/isOpen", method = RequestMethod.GET)
    public Boolean isOpen() throws Exception {
        //is open : true / false
        //boolean isOpen = false;
        try {
            //isOpen =  ws.isOpen();
            return ws.isOpen();
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }


    /**
     * onConnect 소켓오픈
     * @throws IOException
     * @throws WebSocketException
     */
    public void onConnect() throws IOException, WebSocketException {

        ws = connect();
        log.info("[BinanceClientController] onConnect : " + ws.getSocket().toString());

        //BTCUSDT
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonObject.addProperty("method", "SUBSCRIBE");
        //jsonArray.add("btcusdt@miniTicker");
        //jsonArray.add("btcusdt@ticker");
        jsonArray.add("btcusdt@kline_1m");
        jsonObject.add("params", jsonArray);
        jsonObject.addProperty("id", 1);
        ws.sendText(jsonObject.toString());

        //ETHUSDT
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        jsonObject.addProperty("method", "SUBSCRIBE");
        jsonArray.add("ethusdt@kline_1m");
        jsonObject.add("params", jsonArray);
        jsonObject.addProperty("id", 2);
        ws.sendText(jsonObject.toString());

        //XRPUSDT
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        jsonObject.addProperty("method", "SUBSCRIBE");
        jsonArray.add("xrpusdt@kline_1m");
        jsonObject.add("params", jsonArray);
        jsonObject.addProperty("id", 3);
        ws.sendText(jsonObject.toString());

    }

    /**
     * onClose 소켓종료
     * @throws IOException
     * @throws WebSocketException
     */
    public void onClose() throws IOException, WebSocketException {

        //when socket open
        if (ws != null && ws.isOpen()) {
            ws.disconnect();
            log.info("[BinanceClientController] onClose : " + ws.getSocket().toString());
        } else {
            log.info("[BinanceClientController] onClose(Socket is Not Connected)");
        }
    }


    /**
     * 실제 커넥션 맺는 부분(메시지 수신)
     * @return
     * @throws IOException
     * @throws WebSocketException
     */
    public WebSocket connect() throws IOException, WebSocketException {
        return new WebSocketFactory()
                .setConnectionTimeout(TIMEOUT)
                .createSocket(SERVER)
                .addListener(new WebSocketAdapter() {

                    // binary message arrived from the server
                    @Override
                    public void onBinaryMessage(WebSocket websocket, byte[] binary) {
                        String str = new String(binary);
                        //System.out.println(str);
                    }

                    // A text message arrived from the server.
                    @Override
                    public void onTextMessage(WebSocket websocket, String message) throws Exception {

                        //여기서 메세지 받음 메세지 받고 후 처리
                        //log.info("message : " + message);

                        //데이터 파싱 시작
                        binanceParser.binanceParser(message);

                    }

                    @Override
                    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                        //super.onError(websocket, cause);
                        log.info("[BinanceClientController] onError : " + cause);
                    }
                })
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .connect();
    }

}
