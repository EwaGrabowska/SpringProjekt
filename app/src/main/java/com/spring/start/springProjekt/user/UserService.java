package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import com.spring.start.springProjekt.user.DTO.UserDTO;
import org.springframework.transaction.annotation.Transactional;

interface UserService {


    UserDTO findUserByEmail(String email);

    void saveNewUser(UserDTO user, String roleName);

    void updateUserPassword(String newPassword, String email);

    void updateUserProfile(String newName, String newLastName, String newEmail, int id);

    void updateUserActivation(int activeCode, String activationCode);

    UserDTO findUserById(int id);

    void addEvent(ArgoFileEvent argoFileEvent);

    @Transactional
    int deleteUser(String email);
}