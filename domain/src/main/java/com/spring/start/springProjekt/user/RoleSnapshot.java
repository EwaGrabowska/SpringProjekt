package com.spring.start.springProjekt.user;

import lombok.Data;

@Data
public class RoleSnapshot {

    private Integer id;
    private String role;

    protected RoleSnapshot() {

    }

    RoleSnapshot(final Integer id, final String role) {
        this.id = id;
        this.role = role;
    }
}
