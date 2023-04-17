package com.thread.demo.entity;

import lombok.Data;

/**
 * @author YanXingG
 * @version 1.0
 * @date 2023/4/15 17:56
 */
@Data
public class ThreadResultEntity {

    /**
     * 请求状态
     */
    private boolean status;

    /**
     * 接口返回数据
     */
    private String json;

    /**
     * 接口耗时
     */
    private int time;
}
