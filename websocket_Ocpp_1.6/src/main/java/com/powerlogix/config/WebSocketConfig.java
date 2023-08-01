package com.powerlogix.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.powerlogix.Handler.WebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  
{
	    	    
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new WebSocketHandler(), "/message");
	}
	    
}

