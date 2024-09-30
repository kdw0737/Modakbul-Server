package com.modakbul.global.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.modakbul.global.websocket.util.StompHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker   // STOMP 사용
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final StompHandler stompHandler;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 해당 경로로 들어오는것을 구독하는것으로 정한다.
		registry.enableSimpleBroker("/sub");
		// @MessageMapping("hello") 라면 경로는 -> /pub/hello
		registry.setApplicationDestinationPrefixes("/pub");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/stomp") // ex ) ws://localhost:8080/stomp
			.setAllowedOriginPatterns("*"); // 일단 모두 허용
	}

	// socket 연결이 성공 후 실제 메세지가 송수신될 때 동작
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		log.info(" 소켓 연결 성공 ");
		registration.interceptors(stompHandler);
	}
}
