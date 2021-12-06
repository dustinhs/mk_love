package com.pinkcobra.auto.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Component
public class HttpUtil {

    private static Log log = LogFactory.getLog(HttpUtil.class);

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    public Object getHttpRequest(UriComponentsBuilder builder) throws UnsupportedEncodingException {

        String restUri = URLDecoder.decode(builder.toUriString(), "UTF-8");
        //String restUri = builder.toUriString();
        //log.info("restUri : " + restUri);
        RestTemplate restTemplate = restTemplateBuilder.build();

        JsonObject returnObj = new JsonObject();

        ResponseEntity<Object> response = null;
        Integer statusCode = null;
        String body = null;
        try {
            response = restTemplate.getForEntity(restUri, Object.class);
            statusCode = getStatusCode(response);
            body = getBody(response).toString();

        } catch (HttpClientErrorException e) {
            //e.printStackTrace();
            statusCode = e.getStatusCode().value();
            body = e.getResponseBodyAsString();
        } catch (HttpServerErrorException e) {
            log.info("HttpServerErrorException========================================");
            statusCode = e.getStatusCode().value();
            body = null;
            log.debug("statusCode : " + statusCode);
            log.info("/HttpServerErrorException========================================");
        } finally {
            log.debug("statusCode : " + statusCode);
            log.debug("body : " + body);

            returnObj.addProperty("statusCode", statusCode);
            if(body != null) returnObj.add("body", JsonParser.parseString(body));
            //if(body == null) returnObj.addProperty("body","");
        }

        return returnObj;
    }


    public int getStatusCode(ResponseEntity<Object> object) {
        return object.getStatusCode().value();
    }

    public HttpHeaders getHeaders(ResponseEntity<Object> object) {
        return object.getHeaders();
    }

    public Object getBody(ResponseEntity<Object> object) {

        Gson gson = new Gson();

        return gson.toJson(object.getBody());
    }
}
