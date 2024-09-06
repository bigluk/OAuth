package com.luciano.oauth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.luciano.oauth.entity.UserAuthentication;


@Repository
public interface UserAuthenticationRepository extends CrudRepository<UserAuthentication, String> {
    
    UserAuthentication findByUsername(String username);

}
