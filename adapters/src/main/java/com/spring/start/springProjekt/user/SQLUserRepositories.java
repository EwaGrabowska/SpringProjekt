package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.user.DTO.UserQueryDTO;
import com.spring.start.springProjekt.user.DTO.UserSimpleQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface SQLUserQueryRepository extends Repository<UserSnapshot, Integer>,UserQueryRepository{

    @Query(value = "SELECT * from user WHERE user.name LIKE %:param% OR user.last_name LIKE %:param% OR user.email LIKE %:param%", nativeQuery = true)
    Page<UserQueryDTO> findAllSearchBy(@Param("param") String param, Pageable pageable);

    Page<UserQueryDTO> findAllBy(Pageable pageable);

    UserQueryDTO findUserByEmail(String email);

    List<UserSimpleQueryDTO> findAllBy();

}
interface SQLUserRepository extends JpaRepository<UserSnapshot, Integer>{
    Optional<UserSnapshot> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE user SET user.password = :newPassword WHERE user.email= :email", nativeQuery = true)
    void updateUserPassword(@Param("newPassword") String password, @Param("email") String email);

    @Modifying
    @Query(value = "UPDATE user SET user.name = :newName, user.last_name = :newLastName, user.email = :newEmail WHERE user.user_id= :id", nativeQuery = true)
    void updateUserProfile(@Param("newName") String newName, @Param("newLastName") String newLastName,
                           @Param("newEmail") String newEmail, @Param("id") Integer id);
    @Modifying
    @Query(value = "UPDATE user SET user.active = :activeParam WHERE user.activationCode = :activationCode", nativeQuery = true)
    void updateActivation(@Param("activeParam") int activeParam, @Param("activationCode") String activationCode);

    UserSnapshot findUserById(int id);

    @Modifying
    @Query(value = "UPDATE user SET user.active = :intActive WHERE user.user_id= :id", nativeQuery = true)
    void updateActivationUser(@Param("intActive") int active, @Param("id") int id);

    @Modifying
    @Query(value = "UPDATE user_role SET user_role.role_id = :roleId WHERE user_role.user_id= :id", nativeQuery = true)
    void updateRoleUser(@Param("roleId") int nrRoli, @Param("id") int id);

    @Modifying
    @Query(value = "DELETE FROM user_role WHERE user_role.user_id = :id", nativeQuery = true)
    void deleteUserFromUserRole(@Param("id") int id);

    @Modifying
    @Query(value = "DELETE FROM user WHERE user.user_id = :id", nativeQuery = true)
    void deleteUserFromUser(@Param("id") int id);

}
@org.springframework.stereotype.Repository
class UserRepositoryImp implements UserRepository{

    private final SQLUserRepository repository;
    private final SQLRoleRepository roleRepository;

    UserRepositoryImp(final SQLUserRepository repository, final SQLRoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return repository.findByEmail(email).map(User::restore);
    }

    @Override
    public void updateUserPassword(final String password, final String email) {
        repository.updateUserPassword(password, email);

    }

    @Override
    public void updateUserProfile(final String newName, final String newLastName, final String newEmail, final Integer id) {
        repository.updateUserProfile(newName, newLastName, newEmail, id);
    }

    @Override
    public void updateActivation(final int activeParam, final String activationCode) {
        repository.updateActivation(activeParam, activationCode);
    }

    @Override
    public User findUserById(final int id) {
        var user = repository.findUserById(id);
        return User.restore(user);
    }

    @Override
    public void updateActivationUser(final int active, final int id) {
        repository.updateActivationUser(active, id);
    }

    @Override
    public void updateRoleUser(final int nrRoli, final int id) {
        repository.updateRoleUser(nrRoli, id);
    }

    @Override
    public void deleteUserFromUserRole(final int id) {
        repository.deleteUserFromUserRole(id);
    }

    @Override
    public void deleteUserFromUser(final int id) {
        repository.deleteUserFromUser(id);
    }

    @Override
    public User save(final User user) {

        UserSnapshot returned = repository.save(user.getSnapshot());
        return User.restore(returned);
    }

    @Override
    public void updateUser(UserSnapshot userSnapshot) {
        repository.save(userSnapshot);
    }
}
