package ru.kpfu.itis.androidlab.Join.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/chat-socket")
                .enableSimpleBroker("/startChat", "/chatGroup", "/endChat");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket-end-point").setAllowedOrigins("*").withSockJS();
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpSubscribeDestMatchers("/*").permitAll()
                .simpDestMatchers("/**").permitAll()
//            .simpSubscribeDestMatchers("/user/**", "/topic/friends/*").hasRole("USER")
//            .simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll()
                .anyMessage().permitAll();

    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}
