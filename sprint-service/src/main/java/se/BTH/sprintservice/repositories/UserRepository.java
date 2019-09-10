package se.BTH.sprintservice.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import se.BTH.sprintservice.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    List<User> findByName(@Param("name") String Name);

    User findByEmail(@Param("email") String email);

    List<User> findByPhone(@Param("phone") String phone);

    List<User> findAll();


    List<User> findByCity(@Param("city") String city);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
