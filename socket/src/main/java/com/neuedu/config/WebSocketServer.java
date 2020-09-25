package com.neuedu.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuedu.util.ResultCode;
import com.neuedu.util.ResultData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {
    ObjectMapper objectMapper = new ObjectMapper();
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();
    //发送消息
    public void sendMessage(Session session, String message) throws IOException {
        if(session != null){
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        }
    }
    //给指定用户发送信息
    public void sendInfo(String userName, String message){
        Session session = sessionPools.get(userName);
        try {
            sendMessage(session, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sid") String userName) throws IOException {
        Session oldsession = sessionPools.get(userName);
        System.out.println("连接 ------" + userName);
        if(null != oldsession) {
            System.out.println(session == oldsession);
            sendMessage(oldsession,objectMapper.writeValueAsString(ResultData.failed(ResultCode.UNAUTHENTICATION,"已有其他人登录")));
        } else {
            System.out.println("不存在");
            addOnlineCount();
        }
        sessionPools.put(userName, session);
    }
    //关闭连接时调用
    @OnClose
    public void onClose(Session session,@PathParam(value = "sid") String userName){
        System.out.println("关闭----" + userName);
        if(session == sessionPools.get(userName)) {
            sessionPools.remove(userName);
        }
        subOnlineCount();
    }
    //收到客户端信息
    @OnMessage
    public void onMessage(String message) throws IOException{
        message = "客户端：" + message + ",已收到";
        System.out.println(message);
    }
    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    public static void addOnlineCount(){
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }

}
