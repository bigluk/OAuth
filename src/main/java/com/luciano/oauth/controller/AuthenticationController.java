package com.luciano.oauth.controller;

import org.springframework.web.bind.annotation.RestController;
import com.luciano.oauth.DTO.UserCredential;
import com.luciano.oauth.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    

    @PostMapping("/authentication")
    public String createAuthenticationToken(@Valid @RequestBody UserCredential userCredential) {
        
        // authenticate
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                                                                   userCredential.getUsername(), 
                                                                                   userCredential.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.createToken((User) authentication.getPrincipal());
        }
        else {
            throw new UsernameNotFoundException("Bad Credential");
        }

    }
    

}
