package com.spring.start.springProjekt.configuration;

interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String email, String password);
}
