package com.spring.start.springProjekt.admin;

import com.spring.start.springProjekt.user.DTO.UserDTO;
import com.spring.start.springProjekt.user.DTO.UserQueryDTO;
import com.spring.start.springProjekt.user.DTO.UserSimpleQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

interface AdminService {

    Page<UserQueryDTO> findAllPage(Pageable pageable);

    Page<UserQueryDTO> findAllSearchPage(String param, Pageable pageable);

    void deleteUserById(int id);

    List<UserSimpleQueryDTO> findAll();

    File generatePDF();

    File generateXML();

    File generateJSON();

    UserDTO findUserByIdWitchRole(int id);

    void updateUser(final int id, int roleNumber, int isActive);

}
