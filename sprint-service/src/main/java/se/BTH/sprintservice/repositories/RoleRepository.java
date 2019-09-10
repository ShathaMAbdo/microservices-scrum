package se.BTH.sprintservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import se.BTH.sprintservice.models.Role;
import se.BTH.sprintservice.models.RoleName;

@Transactional
public interface RoleRepository extends MongoRepository<Role,String> {
    Role findByName(RoleName name);
}
