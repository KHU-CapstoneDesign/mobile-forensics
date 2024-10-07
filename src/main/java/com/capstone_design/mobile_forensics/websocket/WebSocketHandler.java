package com.capstone_design.mobile_forensics.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    // 웹소켓 연결에 대한 로직을 처리하는 담당
    // 보통 afterConnectionEstablished(WebSocketSession session)으로 웹소켓 연결 이후 처리 메서드 사용함
    private final ObjectMapper objectMapper; //payload를 특정 객체로 만들어주기 위한 매퍼

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload  = message.getPayload();
        log.info("payload = {}", payload);

    }

}
