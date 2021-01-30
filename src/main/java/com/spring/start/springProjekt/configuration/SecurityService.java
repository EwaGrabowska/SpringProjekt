package com.spring.start.springProjekt.configuration;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String email, String password);
}
