package com.pinkcobra.auto.binance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pinkcobra.auto.util.CommonUtil;
import com.pinkcobra.auto.vo.BinanaceTrade;
import com.pinkcobra.auto.websocket.controller.BaseSocketController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BinanceParser {

    private static Log log = LogFactory.getLog(BinanceParser.class);

    @Autowired
    BaseSocketController baseSocketController;

    /**
     * 바이낸스 파서
     * 데이터를 파싱합니닷
     * @param jsonStr
     * @throws Exception
     */
    public void binanceParser(String jsonStr) throws Exception {

        JsonObject obj = JsonParser.parseString(jsonStr).getAsJsonObject();

        String evertType = null;
        if (obj.getAsJsonObject().get("e") != null) {
            if (!obj.getAsJsonObject().get("e").isJsonNull()) {
                evertType = obj.get("e").getAsString();  // Event type
            } else {
            }
        }

        if (evertType != null) {
            //if (evertType.equals("24hrMiniTicker")) {
            if (evertType.equals("kline")) {

                long eventTime = obj.get("E").getAsLong();  // Event time
                obj = obj.get("k").getAsJsonObject();

                String symbol = obj.get("s").getAsString();  // Symbol
                Float price = obj.get("c").getAsFloat();  // Price
                String quantity = obj.get("v").getAsString();  // Quantity

                String dateStr = CommonUtil.getTimestampToDate(eventTime, "yyyyMMddHHmmss");
                String tradeYmd = dateStr.substring(0, 8); //yyyyMMdd
                String tradeTm = dateStr.substring(8); //HHmmss



                BinanaceTrade binanaceTrade = new BinanaceTrade();
                binanaceTrade.setSymbol(symbol);
                binanaceTrade.setTimestamp(eventTime);
                binanaceTrade.setTradeYmd(tradeYmd);
                binanaceTrade.setTradeTm(tradeTm);
                binanaceTrade.setPrice(price);
                binanaceTrade.setQuantity(Float.parseFloat(quantity));
                //log.info(binanaceTrade);


                //json string 으로 변환하기위한 오브젝트 매퍼
                ObjectMapper om = new ObjectMapper();
                //log.info(om.writeValueAsString(binanaceTrade));
                //시세 전송
                baseSocketController.sendTrade(om.writeValueAsString(binanaceTrade));

            }
        }
    }
}
