package dev.lyneapp.backendapplication.messaging.controller;

import dev.lyneapp.backendapplication.messaging.model.ChatMessage;
import dev.lyneapp.backendapplication.messaging.service.WSChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WSChatController {

    private final static Logger LOGGER = LoggerFactory.getLogger(WSChatController.class);

    private final WSChatService wsChatService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/messaging/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        LOGGER.info("WSChatController.addUser ,Adding user: {}", chatMessage);
        return wsChatService.addUser(chatMessage, headerAccessor);
    }

    @MessageMapping("/messaging/chat.directMessage")
    public void sendToSpecificUser(@Payload ChatMessage chatMessage) {
        LOGGER.info("WSChatController.sendToSpecificUser, Sending message: {}", chatMessage);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getRecipientPhoneNumber(), "/specific", chatMessage);
    }

    @MessageMapping("/messaging/chat.sendBroadcastMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        LOGGER.info("WSChatController.sendMessage ,Sending message: {}", chatMessage);
        return wsChatService.sendMessage(chatMessage);
    }

    @GetMapping("/messaging/chat.getMessages")
    public List<ChatMessage> getChatMessages(@RequestParam("chatId") String chatId) {
        LOGGER.info("WSChatController.getChatMessages ,Getting messages for chat: {}", chatId);
        return wsChatService.getChatMessages(chatId);
    }

    @GetMapping("/messaging/chat.lastMessage")
    public ChatMessage getLastMessage(@RequestParam("chatId") String chatId) {
        LOGGER.info("WSChatController.getLastMessage, Getting last message for chat: {}", chatId);
        return wsChatService.getLastMessage(chatId);
    }
}