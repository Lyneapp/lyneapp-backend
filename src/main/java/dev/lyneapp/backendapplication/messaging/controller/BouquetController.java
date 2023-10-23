package dev.lyneapp.backendapplication.messaging.controller;

import dev.lyneapp.backendapplication.messaging.model.BouquetDTO;
import dev.lyneapp.backendapplication.messaging.model.MessageDTO;
import dev.lyneapp.backendapplication.messaging.service.BouquetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO https://www.youtube.com/watch?v=TywlS9iAZCM

/**
 * <br>How to determine if the user is online?<br>
 * <br>1. User login<br>
 * <br>2. User Logout<br>
 * <br>3. LastActiveAt<br>
 * <br>4. User disconnection - Use heartbeats for better user experience (periodically send a heartbeat event to the presence server e.g. every 5 seconds
 * and after sending 3 heartbeat events, the client is disconnected and does not reconnect within x = 30 seconds then the online status is changed to offline)<br>
 *
 * <br>
 *
 * <br>NB<br>
 * <br>- Add push notifications for chat messages and matches<br>
 * <br>- Add a boolean for online presence an implement front end and backend logic for that feature.<br>
 * <br>- Should we start with disappearing messages<br>
 */

@RestController
@RequiredArgsConstructor
public class BouquetController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BouquetController.class);

    private final BouquetService bouquetService;

    public static final String BOUQUET_SENT_SUCCESSFULLY = "Bouquet sent successfully";

    @MessageMapping("/messaging/chat.sendMessage")
    @SendTo("/topic/public")
    public ResponseEntity<Boolean> sendBouquet(@Valid @RequestBody BouquetDTO bouquetRequest) {
        LOGGER.info("Sending bouquet from user with id: {} to user with id: {}", bouquetRequest.getSenderPhoneNumber(), bouquetRequest.getRecipientPhoneNumber());
        boolean bouquetSent = bouquetService.sendBouquet(bouquetRequest);
        LOGGER.info("Sent bouquet from user with id: {} to user with id: {}", bouquetRequest.getSenderPhoneNumber(), bouquetRequest.getRecipientPhoneNumber());
        return ResponseEntity.ok(bouquetSent);
    }

    @GetMapping(path = "/messaging/chat.getBouquetCount")
    public BouquetDTO getBouquetCount(@Valid @RequestBody BouquetDTO bouquetDTO) {
        LOGGER.info("Getting the number of bouquet left for user with id: {} and user with id: {}", bouquetDTO.getSenderPhoneNumber(), bouquetDTO.getRecipientPhoneNumber());
        BouquetDTO bouquetCount = bouquetService.getBouquetCount(bouquetDTO);
        LOGGER.info("Got the number of bouquet left for user with id: {} and user with id: {}", bouquetDTO.getSenderPhoneNumber(), bouquetDTO.getRecipientPhoneNumber());
        return ResponseEntity.ok(bouquetCount).getBody();
    }

    @GetMapping(path = "/messaging/chat.getBouquetHistory")
    public ResponseEntity<List<BouquetDTO>> getBouquetHistory(@Valid @RequestBody BouquetDTO bouquetDTO) {
        LOGGER.info("Getting bouquet history for user with id: {} and user with id: {}", bouquetDTO.getSenderPhoneNumber(), bouquetDTO.getRecipientPhoneNumber());
        List<BouquetDTO> bouquetHistory = bouquetService.getBouquetHistory(bouquetDTO);
        LOGGER.info("Got bouquet history for user with id: {} and user with id: {}", bouquetDTO.getSenderPhoneNumber(), bouquetDTO.getRecipientPhoneNumber());
        return ResponseEntity.ok(bouquetHistory);
    }

    @GetMapping("/messaging/chat.sentBouquets")
    public List<BouquetDTO> getSentBouquets(@Valid @RequestBody BouquetDTO bouquetDTO) {
        LOGGER.info("Getting sent bouquets for user with id: {}", bouquetDTO.getSenderPhoneNumber());
        List<BouquetDTO> getSentBouquetHistory = bouquetService.getSentBouquets(bouquetDTO);
        LOGGER.info("Got sent bouquets for user with id: {}", bouquetDTO.getSenderPhoneNumber());
        return getSentBouquetHistory;
    }

    @GetMapping("/messaging/chat.receivedBouquets")
    public List<BouquetDTO> getReceivedBouquets(@Valid @RequestBody BouquetDTO bouquetDTO) {
        LOGGER.info("Getting received bouquets for user with id: {}", bouquetDTO.getRecipientPhoneNumber());
        List<BouquetDTO> getReceivedBouquetHistory = bouquetService.getReceivedBouquets(bouquetDTO);
        LOGGER.info("Got received bouquets for user with id: {}", bouquetDTO.getRecipientPhoneNumber());
        return getReceivedBouquetHistory;
    }

    @GetMapping("/messaging/buyBouquets")
    public List<BouquetDTO> buyBouquets(@Valid @RequestBody BouquetDTO bouquetDTO) {
        LOGGER.info("Buying bouquets for user with id: {}", bouquetDTO.getSenderPhoneNumber());
        List<BouquetDTO> buyBouquets = bouquetService.buyBouquets(bouquetDTO);
        LOGGER.info("Bought bouquets for user with id: {}", bouquetDTO.getSenderPhoneNumber());
        return buyBouquets;
    }
}