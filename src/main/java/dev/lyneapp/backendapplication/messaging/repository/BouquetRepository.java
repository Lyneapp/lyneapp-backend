package dev.lyneapp.backendapplication.messaging.repository;

import dev.lyneapp.backendapplication.messaging.model.Bouquet;
import dev.lyneapp.backendapplication.messaging.model.BouquetDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BouquetRepository extends MongoRepository<Bouquet, String> {
    Optional<Bouquet> findBySenderPhoneNumber(String senderPhoneNumber);
    List<BouquetDTO> findBySenderPhoneNumberAndRecipientPhoneNumberOrderByBouquetCreatedAtAsc(String senderPhoneNumber, String recipientPhoneNumber);
    List<BouquetDTO> findBySenderPhoneNumberOrderByBouquetCreatedAtAsc(String senderPhoneNumber);
    List<BouquetDTO> findByRecipientPhoneNumberOrderByBouquetCreatedAtAsc(String recipientPhoneNumber);
}
