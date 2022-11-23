package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.netcdfFfile.ArgoFileDataBaseEventSnapshot;
import lombok.Data;

import java.util.Set;

@Data
public class UserSnapshot {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private int active;
    private Set<RoleSnapshot> roles;
    private Set<ArgoFileDataBaseEventSnapshot> events;
    private String activationCode;
    private int roleNumber;
    private String newPassword;
    private String passwordConfirm;
    private String oldPasswordCheck;

    UserSnapshot(final Integer id, final String email, final String password, final String name, final String lastName, final int active, final Set<RoleSnapshot> roles, final String activationCode, final Set<ArgoFileDataBaseEventSnapshot> events) {
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

    UserSnapshot() {
    }
}
