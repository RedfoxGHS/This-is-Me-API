package br.inatel.thisismeapi.repositories;

import br.inatel.thisismeapi.entities.Skill;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends MongoRepository<Skill, String> {

    @Aggregation(pipeline = {
            "{ $match: { email: ?0 } }"
    })
    List<Skill> findAllByEmail(String email);

    @Aggregation(pipeline = {
            "{ $match: { email: ?1, name: ?0 } }"
    })
    Optional<Skill> findByNameAndEmail(String name ,String email);
}

