package com.spring.start.springProjekt.admin;

import com.spring.start.springProjekt.user.UserQueryRepository;
import com.spring.start.springProjekt.user.UserServiceForAdminFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AdminConfiguration {

    @Bean
    AdminService adminService(final UserServiceForAdminFacade userServiceAdminFacade,
                              final UserQueryRepository userQueryRepository) {
        return new AdminServiceImpl(userServiceAdminFacade, userQueryRepository);
    }
}
