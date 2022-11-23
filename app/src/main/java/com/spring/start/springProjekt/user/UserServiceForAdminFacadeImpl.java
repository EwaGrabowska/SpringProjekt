package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import com.spring.start.springProjekt.user.DTO.UserDTO;


class UserServiceForAdminFacadeImpl implements UserServiceForAdminFacade {

    private final UserRepository userRepository;

    private final UserService userService;

    UserServiceForAdminFacadeImpl(final UserRepository userRepository, final UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public UserDTO findUserById(final int id) {
        return userService.findUserById(id);
    }

    @Override
    public UserDTO findByEmail(final String email) {
        return userService.findUserByEmail(email);
    }

    @Override
    public void updateActivationUser(final int active, final int id) {
        userRepository.updateActivationUser(active, id);
    }

    @Override
    public void updateRoleUser(final int roleId, final int userId) {
        userRepository.updateRoleUser(roleId, userId);
    }

    @Override
    public void deleteUserFromUserRole(final int userId) {
        userRepository.deleteUserFromUserRole(userId);
    }

    @Override
    public void deleteUserFromUser(final int userId) {
        userRepository.deleteUserFromUser(userId);
    }

    @Override
    public void handle(final ArgoFileEvent argoFileEvent) {
        userService.addEvent(argoFileEvent);
    }

}
