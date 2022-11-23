package com.spring.start.springProjekt.admin;

import com.spring.start.springProjekt.user.DTO.UserDTO;
import com.spring.start.springProjekt.user.DTO.UserQueryDTO;
import com.spring.start.springProjekt.user.DTO.UserSimpleQueryDTO;
import com.spring.start.springProjekt.user.UserQueryRepository;
import com.spring.start.springProjekt.user.UserServiceForAdminFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;


@Transactional
class AdminServiceImpl implements AdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserServiceForAdminFacade userServiceAdminFacade;
    private final UserQueryRepository userQueryRepository;

    AdminServiceImpl(final UserServiceForAdminFacade userServiceAdminFacade, final UserQueryRepository userQueryRepository) {
        this.userServiceAdminFacade = userServiceAdminFacade;
        this.userQueryRepository = userQueryRepository;
    }


    @Override
    public Page<UserQueryDTO> findAllPage(final Pageable pageable) {
        LOG.debug("[INVOKED >>> AdminServiceImpl.findAllPage >");
        return userQueryRepository.findAllBy(pageable);
    }

    @Override
    public Page<UserQueryDTO> findAllSearchPage(final String param, final Pageable pageable) {
        LOG.debug("[INVOKED >>> AdminServiceImpl.findAllSearchPage >");
        return userQueryRepository.findAllSearchBy(param, pageable);
    }

    @Override
    public void deleteUserById(int id) {
        LOG.debug("[INVOKED >>> AdminServiceImpl.deleteUserById > id: " + id);
        userServiceAdminFacade.deleteUserFromUserRole(id);
        userServiceAdminFacade.deleteUserFromUser(id);
    }

    @Override
    public List<UserSimpleQueryDTO> findAll() {
        LOG.debug("[INVOKED >>> AdminServiceImpl.findAll() >");
        return userQueryRepository.findAllBy();
    }

    @Override
    public File generatePDF() {
        LOG.debug("[INVOKED >>> AdminServiceImpl.generatePDF() >");
        UsersListGenerator usersListGenerator = new UsersListGenerator();
        var userList = findAll();
        return usersListGenerator.generatePDF(userList);
    }

    @Override
    public File generateXML() {
        LOG.debug("[INVOKED >>> AdminServiceImpl.generateXML() >");
        UsersListGenerator usersListGenerator = new UsersListGenerator();
        var userList = findAll();
        return usersListGenerator.generateXML(userList);
    }

    @Override
    public File generateJSON() {
        LOG.debug("[INVOKED >>> AdminServiceImpl.generateJSON() >");
        UsersListGenerator usersListGenerator = new UsersListGenerator();
        var userList = findAll();
        return usersListGenerator.generateJSON(userList);
    }

    @Override
    public UserDTO findUserByIdWitchRole(final int id) {
        LOG.debug("[INVOKED >>> AdminServiceImpl.findUserByIdWitchRole > id: " + id);
        var user = userServiceAdminFacade.findUserById(id);
        var role = user.getRoles().iterator().next().getId();
        user.setRoleNumber(role);
        return user;
    }

    @Override
    public void updateUser(final int id, int roleNumber, int isActive) {
        LOG.debug("[INVOKED >>> AdminServiceImpl.updateUser > id: " + id + ", role number: " + roleNumber + ", active: " + isActive);
        userServiceAdminFacade.updateActivationUser(isActive, id);
        userServiceAdminFacade.updateRoleUser(roleNumber, id);

    }


}
