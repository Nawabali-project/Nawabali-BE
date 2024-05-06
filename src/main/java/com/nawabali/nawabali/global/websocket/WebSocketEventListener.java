package com.nawabali.nawabali.global.websocket;

import com.nawabali.nawabali.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class WebSocketEventListener {

    private final WebSocketChatRoomCount chatRoomCount;

    public WebSocketEventListener (WebSocketChatRoomCount chatRoomCount) {
        this.chatRoomCount = chatRoomCount;
    }

    @EventListener
    public void sessionSubscribeEvent (SessionSubscribeEvent subscribeEvent) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(subscribeEvent.getMessage());
        Authentication authentication = (Authentication) subscribeEvent.getMessage().getHeaders().get("simpUser");

        String chatRoomId = null;
        assert  authentication != null;
        String email = authentication.getName();

        String destination = accessor.getDestination();
        if (destination != null) {
            Pattern pattern = Pattern.compile("/sub/chat/room/(\\d+)");
            Matcher matcher = pattern.matcher(destination);
            if (matcher.find()) {
                chatRoomId = matcher.group(1);
            }
        }
        log.info("웹소켓 구독 완료 :" + chatRoomId);
        chatRoomCount.addUser(Long.valueOf(chatRoomId), email);
    }

    @EventListener
    public void sessionUnsubscribeEvent (SessionUnsubscribeEvent unsubscribeEvent) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(unsubscribeEvent.getMessage());
        Authentication authentication = (Authentication) unsubscribeEvent.getMessage().getHeaders().get("simpUser");

        Long chatRoomId = null;
        assert  authentication != null;
        String email = authentication.getName();

        List<String> chatRoomIdList = accessor.getNativeHeader("chatRoomId");
        if (chatRoomIdList != null && !chatRoomIdList.isEmpty()) {
            String chatRoomIdString = chatRoomIdList.get(0);
            chatRoomId = Long.valueOf(chatRoomIdString);
        }
        log.info("웹소켓 구독 끊김!!!! :" + chatRoomId);
        log.info("웹소켓 네이티브 헤더 뭐가 들어있는지 : "+ accessor);
//       chatRoomCount.outUser(Long.valueOf(chatRoomId),email);
        if (chatRoomId != null) {
            chatRoomCount.outUser(chatRoomId, email);
        } else {
            // chatRoomId가 null일 때의 처리
            // 예: 오류 처리, 로깅 등
            log.error("chatRoomId가 null입니다. 처리할 수 없습니다.");
        }
    }
}
