package com.sky.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sharkCode
 * @date 2025/5/19 20:44
 */
@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {
    // 会话对象
    private static final Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 添加连接处理
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("sid: {} ", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 处理从客户端接收到的消息
     * @param message
     * @param sid
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        // 处理收到的消息
        System.out.println("Received message from " + sid + ": " + message);
    }
    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid) {
        sessionMap.remove(sid);
    }
    @OnError
    public void onError(Session session, Throwable error, @PathParam("sid") String sid) {
        error.printStackTrace();
    }

    /**
     * 发消息给指定客户端
     * @param sid
     * @param message
     */
    public static void sendMessage(String sid, String message) {
        Session session = sessionMap.get(sid);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 发消息给全部客户端
     */
    public static void sendAllMessage(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
