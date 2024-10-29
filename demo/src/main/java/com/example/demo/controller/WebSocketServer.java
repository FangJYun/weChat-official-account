package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * websocket服务
 *
 * @author fangjy
 * @date 2022-07-29 15:36
 **/
@Component
@Slf4j
@ServerEndpoint("/sendMessage/{employeeId}")
public class WebSocketServer {

    /**用来记录当前在线连接数。设计成线程安全的。*/
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /**用来存放每个客户端对应的WebSocket对象。*/
    private static final ConcurrentHashMap<String,WebSocketServer> WEB_SOCKET_MAP = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收消息的employeeId*/
    private String employeeId = "";

    /**
     * 创建连接
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("employeeId") String employeeId) {
        this.session = session;
        this.employeeId=employeeId;
        if(!WEB_SOCKET_MAP.containsKey(employeeId)){
            //创建存储websocket服务
            WEB_SOCKET_MAP.put(employeeId,this);
            //在线数加1
            addOnlineCount();
            log.info("用户"+employeeId+"登录:,当前在线人数为:" + getOnlineCount());
            MessageContext systemMessage = MessageContext.builder().type(0).contentText("系统："+employeeId + "上线,当前在线人数为:" + getOnlineCount()).build();
            sendAllMessage(JSON.toJSONString(systemMessage));
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        if(WEB_SOCKET_MAP.containsKey(employeeId)){
            WEB_SOCKET_MAP.remove(employeeId);
            subOnlineCount();
        }
        log.info("用户"+employeeId+"退出,当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户:"+employeeId+",消息:"+message);
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                MessageContext context = JSON.parseObject(message,MessageContext.class);
                //追加发送人(防止串改)
                context.setFromEmployeeId(this.employeeId);
                context.setType(1);
                String toEmployeeId=context.getToEmployeeId();
                //传送给对应toEmployeeId用户的websocket。可以把消息存储到数据库
                if(StringUtils.isNotBlank(toEmployeeId)&& WEB_SOCKET_MAP.containsKey(toEmployeeId)){
                    WEB_SOCKET_MAP.get(toEmployeeId).sendMessage(JSON.toJSONString(context));
                }else{
                    //如果对方未登录，可以把数据存储到数据库，待对方登录后再把离线消息拉出来
                    log.error("发送对象employeeId:"+toEmployeeId+"未登录");
                    MessageContext systemMessage = MessageContext.builder().type(0).contentText("系统："+employeeId + "上线,当前在线人数为:" + getOnlineCount()).build();
                    WEB_SOCKET_MAP.get(employeeId).sendMessage(JSON.toJSONString(systemMessage));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session session对象
     * @param error 错误对象
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+employeeId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 发送消息
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     */
    public void sendAllMessage(String message) {
        try {
            WEB_SOCKET_MAP.forEach((k,v)-> v.sendMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得此时的在线人数
     */
    public static synchronized int getOnlineCount() {
        return WebSocketServer.ONLINE_COUNT.get();
    }

    /**
     * 在线人数加1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.ONLINE_COUNT.incrementAndGet();
    }

    /**
     * 在线人数减1
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.ONLINE_COUNT.decrementAndGet();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MessageContext{
        /**
         * 0：系统，1消息
         */
        private Integer type;
        /**
         * 消息内容
         */
        private String contentText;
        /**
         * 消息的对方
         */
        private String toEmployeeId;
        /**
         * 发送消息的人
         */
        private String fromEmployeeId;

    }

}


