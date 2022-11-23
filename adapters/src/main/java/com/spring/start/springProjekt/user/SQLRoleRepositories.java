package com.spring.start.springProjekt.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface SQLRoleRepository extends JpaRepository<RoleSnapshot, Integer> {
    RoleSnapshot findByRole(String role);
}

@Repository
class RoleRepositoryImp implements RoleRepository {

    private final SQLRoleRepository repository;

    RoleRepositoryImp(final SQLRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role findByRole(final String role) {
        return Role.restore(repository.findByRole(role));
    }
}
