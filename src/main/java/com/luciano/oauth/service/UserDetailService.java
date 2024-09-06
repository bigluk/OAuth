package com.luciano.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.luciano.oauth.entity.UserAuthentication;
import com.luciano.oauth.repository.UserAuthenticationRepository;



@Component
public class UserDetailService implements UserDetailsService {


    @Autowired
    private UserAuthenticationRepository authenticationRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        UserAuthentication user = authenticationRepository.findByUsername(username);

        if(user == null) {
                throw new UsernameNotFoundException("User " + username + " not found");
        }

        return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .disabled(user.isDisabled())
                        .accountExpired(user.isAccountExpired())
                        .credentialsExpired(user.isCredentialsExpired())
                        .accountLocked(user.isAccountLocked())
                        .authorities(AuthorityUtils.createAuthorityList(user.getAuthorithies()))                            
                    .build();

    }


}
