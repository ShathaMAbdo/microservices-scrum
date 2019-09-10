package se.BTH.sprintservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import se.BTH.sprintservice.models.Team;

import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team,String> {
    Optional<Team> findByName(@Param("name") String name);

}
