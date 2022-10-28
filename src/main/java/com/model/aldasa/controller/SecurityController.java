package com.model.aldasa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class SecurityController {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public String currentUserNameSimple() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.getName();
    }
}