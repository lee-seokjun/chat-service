package com.example.chatservice.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Component
public class EchoHandler extends TextWebSocketHandler implements InitializingBean {
    //중복허용 하지않는 set 클라이언트들의 소켓객체들의 모임
    private Set<WebSocketSession> clients = new HashSet<>();
    @Override	//소켓이 연결된경우
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        //session :  클라이언트의 소켓
        System.out.println("클라이언트 접속 :" + session.getId());
        clients.add(session);
    }
    @Override
    public void afterPropertiesSet() throws Exception {}
    @Override//메세지 수신 받은 경우
    //<?> : 어느객체든 받겠다.
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception{
        //클라이언트에서 보내준 메세지
        String loadMessage = (String)message.getPayload();
        System.out.println(session.getId()+":클라이언트 메세지:"+loadMessage);
        clients.add(session);	//추가된 클라이언트는 추가되지 않음
        for(WebSocketSession s : clients) {
            //모든 클라이언트에게 수신된 메세지 전송
            s.sendMessage(new TextMessage(loadMessage));
        }
    }
    @Override//오류발생경우
    public void handleTransportError(WebSocketSession session,Throwable exception) throws Exception{
        super.handleTransportError(session, exception);
//		exception.printStackTrace();
        System.out.println("오류발생 :" + exception.getMessage());
    }
    @Override//연결종료
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        super.afterConnectionClosed(session, status);
        System.out.println("클라이언트 접속 해제 : "  +status.getReason());
        clients.remove(session);
    }
}