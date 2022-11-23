package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.user.DTO.RoleDTO;
import com.spring.start.springProjekt.user.DTO.RoleQueryDTO;
import com.spring.start.springProjekt.user.DTO.UserDTO;
import com.spring.start.springProjekt.user.DTO.UserQueryDTO;
import com.spring.start.springProjekt.user.DTO.UserSimpleQueryDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class UserFactory {
    static UserDTO userToUserDTO(final User user) {
        UserSnapshot source = user.getSnapshot();
        return UserDTO.builder()
                .withId(source.getId())
                .withName(source.getName())
                .withLastName(source.getLastName())
                .withEmail(source.getEmail())
                .withRoles(source.getRoles().stream().map(UserFactory::roleSnapshotToRoleDTO).collect(Collectors.toSet()))
                .withActive(source.getActive())
                .withActivationCode(source.getActivationCode())
                .withPassword(source.getPassword())
                .build();
    }

    static RoleDTO roleToRoleDTO(Role role){
        RoleSnapshot source = role.getSnapshot();
        return roleSnapshotToRoleDTO(source);
    }
    static RoleDTO roleSnapshotToRoleDTO(RoleSnapshot source){
        return RoleDTO.builder()
                .withRole(source.getRole())
                .withId(source.getId())
                .build();
    }
    static User userDTOtoUser(UserDTO source){
        UserSnapshot userSnapshot = new UserSnapshot();
        userSnapshot.setEmail(source.getEmail());
        userSnapshot.setName(source.getName());
        userSnapshot.setLastName(source.getLastName());
        userSnapshot.setPassword(source.getPassword());
        userSnapshot.setActivationCode(source.getActivationCode());
        userSnapshot.setRoles(source.getRoles().stream().map(UserFactory::roleDTOtoRoleSnapshot).collect(Collectors.toSet()));
        return User.restore(userSnapshot);
    }

    static UserQueryDTO userSnapshotToUserQueryDTO(final UserSnapshot source){
        UserQueryDTO result = new UserQueryDTO() {
            @Override
            public Integer getId() {
                return source.getId();
            }

            @Override
            public String getEmail() {
                return source.getEmail();
            }

            @Override
            public String getName() {
                return source.getName();
            }

            @Override
            public String getLastName() {
                return source.getLastName();
            }

            @Override
            public String getPassword() {
                return source.getPassword();
            }

            @Override
            public int getActive() {
                return source.getActive();
            }

            @Override
            public Set<RoleQueryDTO> getRoles() {
                return source.getRoles().stream().map(UserFactory::roleSnapshotToRoleQueryDTO).collect(Collectors.toSet());
            }

            @Override
            public int getRoleNumber() {
                return source.getRoleNumber();
            }

            @Override
            public void setRoleNumber(final Integer numerRoli) {
                source.setRoleNumber(numerRoli);
            }
        };
        return result;
    }
    private static RoleQueryDTO roleSnapshotToRoleQueryDTO(final RoleSnapshot source){
        return new RoleQueryDTO() {
            @Override
            public Integer getId() {
                return source.getId();
            }

            @Override
            public String getRole() {
                return source.getRole();
            }
        };
    }

    static UserSimpleQueryDTO userSnapshotToUserSimpleQueryDTO(final UserSnapshot source) {
        UserSimpleQueryDTO result = new UserSimpleQueryDTO(source.getId(), source.getName(), source.getLastName(), source.getEmail());
        return result;
    }

    static RoleSnapshot roleDTOtoRoleSnapshot(RoleDTO source){
        RoleSnapshot roleSnapshot = new RoleSnapshot(source.getId(), source.getRole());
        return roleSnapshot;
    }
}
