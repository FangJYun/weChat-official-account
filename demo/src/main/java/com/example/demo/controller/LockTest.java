package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.util.StringUtils;

/**
 * lock
 *
 * @author fangjy
 * @date 2021-11-16 13:31
 **/
public class LockTest {
    private final static ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock(true);
    private final static Lock readLock = READ_WRITE_LOCK.readLock();
    private final static Lock writeLock = READ_WRITE_LOCK.writeLock();
    private Map<String,String> data = new HashMap<>();
    public static void main(String[] args) {
        LockTest lockTest = new LockTest();
        for (int i=0;i<4;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"启动");
                lockTest.writeAndRead();
            }).start();
        }
    }

    public void writeAndRead(){
        //readLock.lock();
        String readData = data.get("a");
        if (StringUtils.isEmpty(readData)){
            System.out.println(Thread.currentThread().getName()+"空数据，先写");
            //readLock.unlock();
            writeLock.lock();
            data.put("a","a");
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName()+"写完，释放锁");
            readLock.lock();
            System.out.println(Thread.currentThread().getName()+"锁降级成读锁");
        }
        System.out.println(Thread.currentThread().getName()+"读取的数据："+readData);
        readLock.unlock();
    }

}
