package com.pinkcobra.auto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinkcobra.auto.service.TestService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private static Log log = LogFactory.getLog(MainController.class);

    @Autowired
    TestService testService;

    /**
     * index get
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(Model model,
                        HttpServletRequest request) throws Exception {

        //바이낸스 ohlc 수신
        testService.getBinanceKlines();
        return "index";
    }


    /**
     * 테스트그냥
     * @param model
     * @param request
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String page(Model model,
                        HttpServletRequest request,
                        Principal principal) throws Exception {

        //밍규찡 그냥이거 샘플

        Map params = new HashMap<>();
        params.put("a",4);
        params.put("b",5);
        
        Map resultMap = testService.testMethod(params);
        log.info("resultMap : " + resultMap);

        //json string으로 변환... 이거 웹으로 넘겨줄때 편함요
        ObjectMapper om = new ObjectMapper();
        log.info(om.writeValueAsString(resultMap));

        log.info(testService.add(1,4));

        return "page";
    }
}
