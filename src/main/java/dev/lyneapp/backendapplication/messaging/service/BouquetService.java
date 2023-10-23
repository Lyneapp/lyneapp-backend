package dev.lyneapp.backendapplication.messaging.service;

import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.common.repository.UserRepository;
import dev.lyneapp.backendapplication.messaging.model.Bouquet;
import dev.lyneapp.backendapplication.messaging.model.BouquetDTO;
import dev.lyneapp.backendapplication.messaging.repository.BouquetRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BouquetService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BouquetService.class);
    private final static int MAX_DAILY_BOUQUETS = 2;
    private static final int DELTA_BOUQUET_COUNT = 1;

    private final UserRepository userRepository;
    private final BouquetRepository bouquetRepository;


    public boolean sendBouquet(BouquetDTO bouquetRequest) {
        LOGGER.info("Entering BouquetService.sendBouquet ,Sending bouquet: {}", bouquetRequest);

        Optional<User> sender = userRepository.findByUserPhoneNumber(bouquetRequest.getSenderPhoneNumber());
        Optional<User> receiver = userRepository.findByUserPhoneNumber(bouquetRequest.getRecipientPhoneNumber());

        if (sender.isPresent() && receiver.isPresent()) {
            User senderUser = sender.get();
            User receiverUser = receiver.get();

            if (senderUser.getRemainingBouquets() > 0 && senderUser.getRemainingBouquets() >= bouquetRequest.getNumberOfBouquetsSent()) {
                senderUser.setRemainingBouquets(senderUser.getRemainingBouquets() - bouquetRequest.getNumberOfBouquetsSent());
                userRepository.save(senderUser);

                Bouquet bouquet = new Bouquet();
                bouquet.setBouquetCreatedAt(System.currentTimeMillis());
                bouquet.setSenderPhoneNumber(bouquetRequest.getSenderPhoneNumber());
                bouquet.setRecipientPhoneNumber(bouquetRequest.getRecipientPhoneNumber());
                bouquet.setRemainingBouquets(senderUser.getRemainingBouquets());
                bouquetRepository.save(bouquet);

                return true;
            } else {
                throw new RuntimeException("Error sending bouquet, the sender does not have enough bouquets left.");
            }
        } else {
            throw new RuntimeException("Error sending bouquet, invalid sender or receiver.");
        }
    }

    public BouquetDTO getBouquetCount(BouquetDTO bouquetDTO) {
        LOGGER.info("Entering BouquetService.getBouquetCount ,Getting bouquet count for user: {}", bouquetDTO.getSenderPhoneNumber());
        Optional<User> user = userRepository.findByUserPhoneNumber(bouquetDTO.getSenderPhoneNumber());
        Optional<Bouquet> bouquet = bouquetRepository.findBySenderPhoneNumber(bouquetDTO.getSenderPhoneNumber());
        if (user.isPresent() || bouquet.isPresent()) {
            bouquetDTO.setRemainingBouquets(user.get().getRemainingBouquets());
            bouquet.get().setRemainingBouquets(user.get().getRemainingBouquets());
            bouquetRepository.save(bouquet.get());
            return bouquetDTO;
        } else {
            throw new RuntimeException("Error getting bouquet count, the user may not exist");
        }
    }

    public List<BouquetDTO> getBouquetHistory(BouquetDTO bouquetRequest) {
        return bouquetRepository.findBySenderPhoneNumberAndRecipientPhoneNumberOrderByBouquetCreatedAtAsc(bouquetRequest.getSenderPhoneNumber(), bouquetRequest.getRecipientPhoneNumber());
    }

    public List<BouquetDTO> getSentBouquets(BouquetDTO bouquetRequest) {
        LOGGER.info("Entering BouquetService.getSentBouquets ,Getting sent bouquets for sender: {}", bouquetRequest.getSenderPhoneNumber());
        Optional<User> sender = userRepository.findByUserPhoneNumber(bouquetRequest.getSenderPhoneNumber());
        if (sender.isPresent())
            return bouquetRepository.findBySenderPhoneNumberOrderByBouquetCreatedAtAsc(bouquetRequest.getSenderPhoneNumber());
        else {
            throw new RuntimeException("Error getting sent bouquets, the sender may not exist");
        }
    }

    public List<BouquetDTO> getReceivedBouquets(BouquetDTO bouquetRequest) {
        Optional<User> receiver = userRepository.findByUserPhoneNumber(bouquetRequest.getRecipientPhoneNumber());
        if (receiver.isPresent()) {
            return bouquetRepository.findByRecipientPhoneNumberOrderByBouquetCreatedAtAsc(bouquetRequest.getRecipientPhoneNumber());
        }
        else {
            throw new RuntimeException("Error getting received bouquets, the receiver may not exist");
        }
    }

    // Run a cron job every day at 3am to give each user 3 bouquets -  Eastern Standard Time (EST) timezone.
    // In the future we will only give bouquets to newly  created users and premium users.
    // @Scheduled(cron = "0 * * * * *", zone = "America/New_York") - /* run every minute for testing */
    @Scheduled(cron = "0 0 3 * * *", zone = "America/New_York")
    private void giveDailyBouquetsToUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            int remainingBouquets = user.getRemainingBouquets();

            if (remainingBouquets < MAX_DAILY_BOUQUETS) {
                user.setRemainingBouquets(Math.min(MAX_DAILY_BOUQUETS, remainingBouquets + 1));
                userRepository.save(user);
            }
        }
    }

    public List<BouquetDTO> buyBouquets(BouquetDTO bouquetDTO) {
        // TODO: Add logic to check if user is premium and has paid for bouquets
        Optional<User> user = userRepository.findByUserPhoneNumber(bouquetDTO.getSenderPhoneNumber());
        if (user.isPresent()) {
            User verifiedUser = user.get();
            verifiedUser.setRemainingBouquets(verifiedUser.getRemainingBouquets() + bouquetDTO.getNumberOfBouquetsBought());
            userRepository.save(verifiedUser);
            return bouquetRepository.findBySenderPhoneNumberOrderByBouquetCreatedAtAsc(bouquetDTO.getSenderPhoneNumber());
        } else {
            throw new RuntimeException("Error buying bouquets, the user may not exist");
        }
    }
}
