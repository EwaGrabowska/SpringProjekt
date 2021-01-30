package com.spring.start.springProjekt.Admin;

import com.spring.start.springProjekt.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {

    Page<User> findAll(Pageable pageable);

    User findUserById(int id);

    void updateUser(int id, int nrRoli, int activity);

    Page<User> findAllSearch(String param, Pageable pageable);

    void insertInBatch(List<User> userList);

    void saveAll(List<User> userList);

    void deleteUserById(int id);

    List<User> findAll();
}
