package dev.lyneapp.backendapplication.messaging.controller;

import dev.lyneapp.backendapplication.messaging.model.ChatMessage;
import dev.lyneapp.backendapplication.messaging.service.WSChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WSChatController {

    private final WSChatService wsChatService;

    @MessageMapping("/messaging/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return wsChatService.sendMessage(chatMessage);
    }

    @GetMapping("/messaging/chat.getMessages")
    public List<ChatMessage> getChatMessages(@RequestParam("chatId") String chatId) {
        return wsChatService.getChatMessages(chatId);
    }

    @MessageMapping("/messaging/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        return wsChatService.addUser(chatMessage, headerAccessor);
    }

    @GetMapping("/messaging/chat.lastMessage")
    public ChatMessage getLastMessage(@RequestParam("chatId") String chatId) {
        return wsChatService.getLastMessage(chatId);
    }
}