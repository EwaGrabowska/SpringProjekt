package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.netcdfFfile.ArgoFileDataBaseEventSnapshot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class User {

    static User restore(UserSnapshot userSnapshot) {
        return new User(userSnapshot.getId(), userSnapshot.getEmail(),
                userSnapshot.getPassword(), userSnapshot.getName(), userSnapshot.getLastName(), userSnapshot.getActive(),
                userSnapshot.getRoles().stream().map(Role::restore).collect(Collectors.toSet()),
                userSnapshot.getActivationCode(), userSnapshot.getEvents());
    }

    private final Integer id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private int active;
    private Set<Role> roles;
    private final String activationCode;

    private final Set<ArgoFileDataBaseEventSnapshot> events;
    private int roleNumber;
    private String newPassword;
    private String passwordConfirm;
    private String oldPasswordCheck;

    private User(final Integer id, final String email, final String password, final String name, final String lastName, final int active, final Set<Role> roles, final String activationCode, final Set<ArgoFileDataBaseEventSnapshot> events) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.active = active;
        this.roles = roles;
        this.activationCode = activationCode;
        this.events = events;
    }

    void toogleActiv() {
        if (this.active == 1) {
            active = 0;
        } else {
            active = 1;
        }
    }

    void updateRole(Role role) {
        this.roles = new HashSet<>(List.of(role));
        this.roleNumber = role.getUserRoleNumber();
    }

    void updateDate(final String email, final String name, final String lastName) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }

    void updatePassword(String password) {
        this.password = password;
    }


    UserSnapshot getSnapshot() {
        return new UserSnapshot(this.id, this.email, this.password, this.name, this.lastName, this.active,
                this.roles.stream().map(Role::getSnapshot).collect(Collectors.toSet()), this.activationCode, this.events);
    }


    User addEvent(final ArgoFileDataBaseEventSnapshot argoFileEvent) {
        events.add(argoFileEvent);
        return this;
    }
}
