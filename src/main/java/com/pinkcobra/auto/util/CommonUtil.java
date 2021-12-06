package com.pinkcobra.auto.util;

import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Component
public class CommonUtil {


    /**
     * 유닉스타임 -> 데이트스트링
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String getTimestampToDate(long timestamp, String pattern) {
        Date date = new Date(timestamp);
        //SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


    /**
     * 리턴 오브젝트 생성
     * @param isSuccess
     * @param msg
     * @return
     */
    public static Map getRtnObj(boolean isSuccess, String msg){
        Map rtnObj = new HashMap();
        rtnObj.put("isSuccess", isSuccess);
        rtnObj.put("msg", msg);

        return rtnObj;
    }

}
