package com.pinkcobra.auto.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pinkcobra.auto.util.CommonUtil;
import com.pinkcobra.auto.util.HttpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {

    private static Log log = LogFactory.getLog(TestService.class);

    @Autowired
    HttpUtil httpUtil;


    public Map testMethod(Map params){
        if(params != null){
            return CommonUtil.getRtnObj(false,"파라미터 누락");
     
        }else if(!params.containsKey("a")){
            return CommonUtil.getRtnObj(false,"a 파라미터 누락");
        }
        else if(!params.containsKey("b")){
            return CommonUtil.getRtnObj(false,"b 파라미터 누락");
        }else{
            return CommonUtil.getRtnObj(true,"딩동댕");
        }
    }

    public int add(int a, int b){
        return a+b;
    }


    /**
     * 바이낸스 ohlc데이터 수신
     * @throws UnsupportedEncodingException
     */
    public void getBinanceKlines() throws UnsupportedEncodingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.binance.com/api/v3/klines");


        builder.queryParam("symbol", "BTCUSDT");
        builder.queryParam("interval", "1m");
        builder.queryParam("limit", "1000");
        //JsonObject response = (JsonObject) httpUtil.getHttpRequest(builder);

        JsonObject response;
        response = (JsonObject) httpUtil.getHttpRequest(builder);

        JsonArray klinesArr = response.get("body").getAsJsonArray();
        log.info(klinesArr);
    }
}
