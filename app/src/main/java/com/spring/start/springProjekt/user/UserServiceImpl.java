package com.spring.start.springProjekt.user;

import com.amazonaws.services.mq.model.NotFoundException;
import com.spring.start.springProjekt.netcdfFfile.ArgoFileDataBaseEventSnapshot;
import com.spring.start.springProjekt.netcdfFfile.ArgoFileService;
import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import com.spring.start.springProjekt.user.DTO.RoleDTO;
import com.spring.start.springProjekt.user.DTO.UserDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Transactional
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final ArgoFileService argoFileService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, final ArgoFileService argoFileService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.argoFileService = argoFileService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(user->UserFactory.userToUserDTO(user)).orElse(null);
    }

    @Override
    public void saveNewUser(UserDTO userDTO, String roleName) {
        Role role = roleRepository.findByRole(roleName);
        RoleDTO roleDto = UserFactory.roleToRoleDTO(role);
        userDTO.setRoles(new HashSet<>(List.of(roleDto)));
        User user = UserFactory.userDTOtoUser(userDTO);
        user.updatePassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
//        TODO usunąć toogle, aby na początu nie było activ
        user.toogleActiv();
        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(String newPassword, String email) {
        userRepository.updateUserPassword(bCryptPasswordEncoder.encode(newPassword), email);
    }

    @Override
    public void updateUserProfile(String newName, String newLastName, String newEmail, int id) {
        userRepository.updateUserProfile(newName, newLastName, newEmail, id);
    }

    @Override
    public void updateUserActivation(int activeCode, String activationCode) {
        userRepository.updateActivation(activeCode, activationCode);
    }

    @Override
    public UserDTO findUserById(final int id) {
        var user =  userRepository.findUserById(id);
        return UserFactory.userToUserDTO(user);
    }

    @Override
    public void addEvent(final ArgoFileEvent argoFileEvent) {
        ArgoFileDataBaseEventSnapshot argoFileDataBaseEvent = argoFileService.findByOccurredOn(argoFileEvent.getOccurredOn());
        userRepository.findByEmail(argoFileEvent.getUserName())
                .map(u->u.addEvent(argoFileDataBaseEvent))
                .ifPresent(userRepository::save);
        }
    @Transactional
    @Override
    public int deleteUser(final String email) {
        return userRepository.findByEmail(email).map(user -> {
            int id = user.getSnapshot().getId();
            userRepository.deleteUserFromUser(id);
            userRepository.deleteUserFromUserRole(id);
            return id;
        }).orElseThrow(()->new NotFoundException("User not found"));
    }
}
