package se.BTH.webservice.services;


import se.BTH.webservice.models.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
    Boolean isAdmin(String userName);
}