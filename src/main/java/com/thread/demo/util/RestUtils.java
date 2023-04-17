package com.thread.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.thread.demo.entity.RequestData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YanXingG
 * @version 1.0
 * @date 2023/4/16 11:56
 */
public class RestUtils {

    private static final RestTemplate restTemplate = new RestTemplate();


    public static String get(RequestData requestData) throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(urlToUri(requestData.getUrl(), (Map<String, String>) parse(requestData)), String.class);
        serverIsRight(responseEntity);   //判断服务器返回状态码
        return responseEntity.getBody();
    }


    public static String post(RequestData requestData) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        if (MediaType.APPLICATION_JSON_VALUE.equals(requestData.getMediaType())) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        } else {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }

        HttpEntity<Object> request = new HttpEntity<>(parse(requestData),headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestData.getUrl(), request, String.class);
        serverIsRight(responseEntity);  //判断服务器返回状态码
        return responseEntity.getBody();
    }

    public static void put(RequestData requestData) {
        restTemplate.put(requestData.getUrl(), new HttpEntity<>(parse(requestData)));
    }

    public static void delete(RequestData requestData) {
        restTemplate.delete(requestData.getUrl(), parse(requestData));
    }


    private static Object parse(RequestData requestData) {
        if (null != requestData.getJson() && !requestData.getJson().equals("none")) {
            switch (requestData.getMediaType()){
                case MediaType.APPLICATION_JSON_VALUE:
                    return requestData.getJson();
                case MediaType.APPLICATION_FORM_URLENCODED_VALUE:
                    Map<String,Object> map=JSONObject.parseObject(requestData.getJson());
                    MultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
                    for (Map.Entry<String, Object> s: map.entrySet()) {
                        valueMap.add(s.getKey(),s.getValue());
                    }
                return valueMap;
                default:
                    return JSONObject.parseObject(requestData.getJson(), Map.class);

            }
        } else {
            return new HashMap<>();

        }
    }

    private static String urlToUri(String url, Map<String, String> urlParams) {
        if (null == urlParams || urlParams.isEmpty()) {
            return url;
        }
        //设置提交json格式数据
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            uriBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        return uriBuilder.build(true).toUriString();
    }


    private static void serverIsRight(ResponseEntity responseEntity) throws Exception {
        if (responseEntity.getStatusCodeValue() == 200) {

        } else {
            System.out.println("服务器请求异常：{}" + responseEntity.getStatusCodeValue());
            throw new Exception();
        }
    }


}
