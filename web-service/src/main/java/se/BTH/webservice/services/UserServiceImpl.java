package se.BTH.webservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.BTH.webservice.models.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        List list=new ArrayList();
        Role role=restTemplate.getForObject("http://Sprintmanag-SERVICE/rolebyname/{RoleName.ROLE_USER}", Role.class);
        list.add(role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new ArrayList<>(list));
        restTemplate.postForObject("http://Sprintmanag-SERVICE/user",user, User.class);
    }

    @Override
    public User findByUsername(String username) {
        User user=restTemplate.getForObject("http://Sprintmanag-SERVICE/userbyname/{username}", User.class);
        return user;
    }
    @Override
    public Boolean isAdmin(String username){
        User currentUser=restTemplate.getForObject("http://Sprintmanag-SERVICE/userbyname/{username}", User.class);
        Optional<Role> optiona=currentUser.getRoles().stream().filter(r->r.getName().equals(RoleName.ROLE_ADMIN)).findFirst();
        Boolean isAdmin=(optiona).isPresent();
        return isAdmin;
    }
}