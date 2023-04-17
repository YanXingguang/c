package com.thread.demo.controller;

import com.thread.demo.entity.RequestData;
import com.thread.demo.entity.ResultData;
import com.thread.demo.entity.ThreadResultEntity;
import com.thread.demo.service.SendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author YanXingG
 * @version 1.0
 * @date 2023/4/14 15:40
 */
@RestController
@RequestMapping("/interface")
public class InterfaceTestingController {
    @Autowired
    private SendRequest sendRequest;

    @PostMapping("/testing")
    public ResultData test(@RequestBody RequestData data) throws ExecutionException, InterruptedException {
        ResultData resultData = new ResultData();
        CountDownLatch countDownLatch = new CountDownLatch(data.getNum());

        //如果并发大于100，置为100
        if (data.getNum() > 100) {
            data.setNum(100);
        }

        //如果并发大于0，则执行
        if (data.getNum() > 0) {
            long start = System.currentTimeMillis();
            List<ThreadResultEntity> resultList = new ArrayList<>();
            for (int i = 0; i < data.getNum(); i++) {
                resultList.add(sendRequest.send(countDownLatch, data).get());
            }

            try {
                countDownLatch.await();
                long end = System.currentTimeMillis();

                //按成功数量分组
                Map<Boolean, Long> map = resultList.parallelStream().collect(Collectors.groupingBy(ThreadResultEntity::isStatus, Collectors.counting()));
                //返回成功数量
                resultData.setTotalSuccess(Math.toIntExact(map.get(true)==null?0:map.get(true)));
                //返回接口失败数量
                resultData.setTotalError(data.getNum() - resultData.getTotalSuccess());
                //返回接口用时最少时间
                resultData.setMinTime(resultList.stream().map(ThreadResultEntity::getTime).min(Integer::compareTo).get());
                //返回接口用时最多时间
                resultData.setMaxTime(resultList.stream().map(ThreadResultEntity::getTime).max(Integer::compareTo).get());
                //计算总耗时
                BigDecimal totalTime = new BigDecimal((end - start) + "").divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
                //返回qps，判断总时间是否为0
                if (totalTime.compareTo(BigDecimal.ZERO) == 0) {
                    resultData.setQps(new BigDecimal(data.getNum()));
                } else {
                    resultData.setQps(new BigDecimal(data.getNum() + "").divide(totalTime, 2, BigDecimal.ROUND_HALF_UP));

                }
                //返回请求接口总数
                resultData.setTotalNum(data.getNum());
                //返回接口请求总耗时
                resultData.setTotalTime(totalTime);
                //接口返回数据
                resultData.setResultEntityList(resultList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return resultData;
    }
}
