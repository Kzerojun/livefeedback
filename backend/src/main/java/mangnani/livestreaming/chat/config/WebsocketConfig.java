package mangnani.livestreaming.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 클라이언트가 WebSocket 서버에 연결하는 데 사용할 엔드포인트를 설정합니다.
		// "/ws" 경로로 WebSocket 연결을 수락합니다.
		// setAllowedOrigins("*")는 모든 도메인에서의 연결을 허용합니다.
		// withSockJS()는 WebSocket을 지원하지 않는 브라우저를 위해 SockJS 폴백 옵션을 활성화합니다.
		registry.addEndpoint("/ws")
				.setAllowedOriginPatterns("*")
				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 클라이언트가 메시지를 보낼 때 사용하는 경로를 설정합니다.
		// "/app"로 시작하는 메시지는 @Controller 클래스의 @MessageMapping 메서드로 라우팅됩니다.
		registry.setApplicationDestinationPrefixes("/app");

		// 클라이언트가 구독하는 경로를 설정합니다.
		// "/topic"로 시작하는 메시지는 메시지 브로커로 라우팅됩니다.
		// 브로커는 메시지를 적절한 구독자에게 전달합니다.
		registry.enableSimpleBroker("/topic");
	}
}
