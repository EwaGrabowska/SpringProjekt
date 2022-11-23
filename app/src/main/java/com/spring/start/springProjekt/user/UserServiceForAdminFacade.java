package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import com.spring.start.springProjekt.user.DTO.UserDTO;

public interface UserServiceForAdminFacade {

    UserDTO findUserById(int id);

    UserDTO findByEmail(String email);

    void updateActivationUser(int active, int id);

    void updateRoleUser(int roleId, int userId);

    void deleteUserFromUserRole(int userId);

    void deleteUserFromUser(int userId);

    void handle(ArgoFileEvent argoFileEvent);
}
