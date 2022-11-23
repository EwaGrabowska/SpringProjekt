package com.spring.start.springProjekt.user.DTO;

import java.util.Set;

public interface UserQueryDTO {

    Integer getId();

    String getEmail();

    String getName();

    String getLastName();

    String getPassword();

    int getActive();

    Set<RoleQueryDTO> getRoles();

    int getRoleNumber();


    void setRoleNumber(Integer numerRoli);
}
