package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.netcdfFfile.ArgoFileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserConfiguration {

    @Bean
    UserServiceForAdminFacade userServiceForAdminFacade(final UserRepository userRepository, final UserService userService) {
        return new UserServiceForAdminFacadeImpl(userRepository, userService);
    }

    @Bean
    UserService userService(final UserRepository userRepository, final RoleRepository roleRepository, final ArgoFileService argoFileService) {
        return new UserServiceImpl(userRepository, roleRepository, argoFileService, new BCryptPasswordEncoder());
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

}
