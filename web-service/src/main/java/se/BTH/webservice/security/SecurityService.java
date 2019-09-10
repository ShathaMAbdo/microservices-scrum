package se.BTH.webservice.security;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}