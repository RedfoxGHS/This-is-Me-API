package br.inatel.thisismeapi.repositories;

import br.inatel.thisismeapi.entities.SubQuest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubQuestRepository extends MongoRepository<SubQuest, String> {

    @Aggregation(pipeline = {
            "{'$match': {" +
                    "'email': ?0, " +
                    "'start': {$gte: ?1}, " +
                    "'end': {$lte: ?2} " +
                    "}}"
    })
    List<SubQuest> findAllSubQuestsByRangeDate(String email, LocalDateTime start, LocalDateTime end);

    @Aggregation(pipeline = {
            "{'$match': {" +
                    "'email': ?0, " +
                    "'end': {$lte: ?1}, " +
                    "'check': false" +
                    "}}"
    })
    List<SubQuest> findAllSubQuestNotCheckUntilDate(String email, LocalDateTime start);

    void deleteAllByEmail(String email);

    @Aggregation(pipeline = {
            "{'$match': {" +
                    "'_id': ?0, " +
                    "'email': ?1, " +
                    "}}"
    })
    Optional<SubQuest> findByIdAndEmail(String id, String email);
}
