package com.spring.start.springProjekt.user;


import com.spring.start.springProjekt.user.DTO.UserQueryDTO;
import com.spring.start.springProjekt.user.DTO.UserSimpleQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserQueryRepository{

    Page<UserQueryDTO> findAllSearchBy(String param, Pageable pageable);

    Page<UserQueryDTO> findAllBy(Pageable pageable);

    UserQueryDTO findUserByEmail(String email);

    List<UserSimpleQueryDTO> findAllBy();


}
