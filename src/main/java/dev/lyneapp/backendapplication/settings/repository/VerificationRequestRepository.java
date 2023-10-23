package dev.lyneapp.backendapplication.settings.repository;


import dev.lyneapp.backendapplication.settings.model.VerificationDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRequestRepository extends MongoRepository<VerificationDTO, String> {

}
