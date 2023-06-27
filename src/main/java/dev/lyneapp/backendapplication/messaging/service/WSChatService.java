package dev.lyneapp.backendapplication.messaging.service;

import dev.lyneapp.backendapplication.messaging.model.ChatMessage;
import dev.lyneapp.backendapplication.messaging.repository.WSMessageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

//TODO - Implement  the notification service feature so that users can receive notifications when they receive a message
//TODO - Referenced from https://stackabuse.com/spring-cloud-aws-sns/
//TODO - Send push notification only when the recipient is offline, determine how to implement this

@Service
@RequiredArgsConstructor
public class WSChatService {

    private final static Logger LOGGER = LoggerFactory.getLogger(WSChatService.class);

    private final WSMessageRepository chatMessageRepository;

    private static final String MESSAGE_NOTIFICATION = "You have a new message";

//    @Value("${sns.topic.arn}")
//    private String snsTopicArn;


    public ChatMessage sendMessage(ChatMessage chatMessage) {
        LOGGER.info("Entering WSChatService.sendMessage ,Sending message: {}", chatMessage);
        chatMessage.setTimestamp(System.currentTimeMillis());
        chatMessage.setSenderName("Uchenna"); // hard coded for now
        chatMessage.setRecipientName("Phoenixtype"); // hard coded for now
        chatMessage.setSenderPhoneNumber(chatMessage.getSender()); // hard coded for now
        chatMessage.setRecipientPhoneNumber("14164555542"); // hard coded for now
        chatMessage.setChatId("16475706079" + "_" + "14164555542"); // hard coded for now

        /**
         FIXME: use this instead of the hard coded one above
         chatMessage.setChatId(chatMessage.getSenderPhoneNumber() + "_" + chatMessage.getRecipientPhoneNumber());

        // sendNotification(chatMessage.getRecipientPhoneNumber(), MESSAGE_NOTIFICATION);
         */

        chatMessageRepository.save(chatMessage);
        LOGGER.info("Exiting WSChatService.sendMessage ,Sending message: {}", chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> getChatMessages(String chatId) {
        LOGGER.info("WSChatService.getChatMessages ,Getting messages for chatId: {}", chatId);
        return chatMessageRepository.findByChatId(chatId);
    }

    public ChatMessage addUser(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        LOGGER.info("Entering WSChatService.addUser ,Adding user: {}", chatMessage);
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        chatMessageRepository.save(chatMessage);
        LOGGER.info("Exiting WSChatService.addUser ,Adding user: {}", chatMessage);
        return chatMessage;
    }

    public ChatMessage getLastMessage(String chatId) {
        LOGGER.info("WSChatService.getLastMessage ,Getting last message for chatId: {}", chatId);
        return chatMessageRepository.findFirstByChatIdOrderByTimestampDesc(chatId);
    }

    /**
    private void sendNotification(String recipientId, String message) {
        // Construct the notification payload
        PublishRequest publishRequest = new PublishRequest()
                .withMessage(message)
                .withTopicArn(snsTopicArn)  // "arn:aws:sns:ca-central-1:291219373582:lyneapp-dev-sns"
                .addMessageAttributesEntry("recipientId", new MessageAttributeValue()
                        .withDataType("String")
                        .withStringValue(recipientId));

        // Publish the notification
        PublishResult publishResult = amazonSNS.publish(publishRequest);
        System.out.println("Message sent. MessageId: " + publishResult.getMessageId());
    }
     */
}
