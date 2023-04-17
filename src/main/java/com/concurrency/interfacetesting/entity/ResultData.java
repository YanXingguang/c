package com.concurrency.interfacetesting.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author YanXingG
 * @version 1.0
 * @date 2023/4/14 17:49
 */
@Data
public class ResultData {
    /**
     * 总请求数
     */
    private int totalNum;

    /**
     * 总时间
     */
    private BigDecimal totalTime;

    /**
     * 总成功数
     */
    private int totalSuccess;

    /**
     * 总失败数
     */
    private int totalError;

    /**
     * 每秒请求数
     */
    private BigDecimal qps;

    /**
     * 接口耗时最短
     */
    private int minTime;

    /**
     * 接口耗时最长
     */
    private int maxTime;

    /**
     * 接口返回数据
     */
    private List<ThreadResultEntity> resultEntityList;
}
