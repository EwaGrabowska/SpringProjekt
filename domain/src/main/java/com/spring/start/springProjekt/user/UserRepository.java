package com.spring.start.springProjekt.user;

import java.util.Optional;

interface UserRepository {
    Optional<User> findByEmail(String email);

    void updateUserPassword(String password, String email);

    void updateUserProfile(String newName, String newLastName, String newEmail, Integer id);

    void updateActivation(int activeParam, String activationCode);

    User findUserById(int id);

    void updateActivationUser(int active, int id);

    void updateRoleUser(int nrRoli, int id);

    void deleteUserFromUserRole(int id);

    void deleteUserFromUser(int id);

    User save(User user);

    void updateUser(UserSnapshot userSnapshot);
}
