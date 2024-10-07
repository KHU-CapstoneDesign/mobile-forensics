package com.capstone_design.mobile_forensics.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket // WebSocket 활성화
public class WebSocketConfig implements WebSocketConfigurer {
    /*
     * WebConfigurer : 웹소켓 활성화, 핸들러 등록
     * */

    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 핸들러 등록 함수
        // 웹소켓 엔드포인트 지정 가능 (현재는 일단 기본 "/"으로 설정)
        // setAllowedOrigin - 지정한 Origin으로부터 오는 요청만 허용함 (*은 모든 요청 허용)
        registry.addHandler(webSocketHandler, "/").setAllowedOrigins("*");
    }
}

