package com.thread.demo.service;

import com.thread.demo.entity.RequestData;
import com.thread.demo.entity.ThreadResultEntity;
import com.thread.demo.util.RestUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * @author YanXingG
 * @version 1.0
 * @date 2023/4/14 17:40
 */
@Component
public class SendRequest {


    @Async("syncExecutorPool")
    public Future<ThreadResultEntity> send(CountDownLatch countDownLatch, RequestData data) {
        long start = System.currentTimeMillis();
        ThreadResultEntity threadResultEntity = new ThreadResultEntity();

        try {
            String response = null;
            switch (data.getType()) {
                case "GET":
                    response = RestUtils.get(data);
                    break;
                case "POST":
                    response = RestUtils.post(data);
                    break;
                case "PUT":
                    RestUtils.put(data);
                    break;
                case "DELETE":
                    RestUtils.delete(data);
                    break;
                default:
                    threadResultEntity.setStatus(false);
                    return new AsyncResult<>(threadResultEntity);
            }
            threadResultEntity.setStatus(true);
            threadResultEntity.setJson(response);
        } catch (Exception e) {
            threadResultEntity.setStatus(false);
            e.printStackTrace();
        }

        countDownLatch.countDown();
        long end = System.currentTimeMillis();
        long s = end - start;
        threadResultEntity.setTime((int) s);
        System.out.println(Thread.currentThread().getName() + "耗时" + s + "ms");
        return new AsyncResult<>(threadResultEntity);
    }
}
