package com.thread.demo.entity;

import lombok.Data;
/**
 * @author YanXingG
 * @version 1.0
 * @date 2023/4/14 16:57
 */
@Data
public class RequestData {
    /**
     * 请求url
     */
    private String url;

    /**
     * 并发数量
     */
    private Integer num;

    /**
     * 请求类型
     */
    private String type;

    /**
     * 请求数据类型
     */
    private String mediaType;

    /**
     * 请求数据
     */
    private String json;

}
