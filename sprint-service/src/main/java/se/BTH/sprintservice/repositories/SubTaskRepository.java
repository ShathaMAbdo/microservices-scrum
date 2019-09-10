package se.BTH.sprintservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import se.BTH.sprintservice.models.SubTask;
import se.BTH.sprintservice.models.User;

import java.util.Optional;

public interface SubTaskRepository extends MongoRepository<SubTask,String> {
    Optional<SubTask> findByName(@Param("name") String name);

    Optional<SubTask> findByUsers(@Param("user") User user);
}
