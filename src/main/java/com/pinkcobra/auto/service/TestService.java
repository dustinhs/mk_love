package com.pinkcobra.auto.service;

import com.pinkcobra.auto.util.CommonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {

    private static Log log = LogFactory.getLog(TestService.class);



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
}
