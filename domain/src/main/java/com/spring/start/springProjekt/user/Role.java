package com.spring.start.springProjekt.user;

class Role {

    static Role restore(RoleSnapshot source) {
        return new Role(source.getId(), source.getRole());
    }

    private final Integer id;
    private final String role;

    private Role(final Integer id, final String role) {
        this.id = id;
        this.role = role;
    }

    public int getUserRoleNumber() {
        return this.id;
    }

    RoleSnapshot getSnapshot() {
        return new RoleSnapshot(this.id, this.role);
    }
}
