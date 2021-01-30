package com.spring.start.springProjekt.NETCDFfile;

import com.spring.start.springProjekt.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("argoFileRepository")
public interface ArgoFileRepository extends JpaRepository<ArgoFile, Long> {

    @Query(value = "SELECT * FROM file f WHERE u.buoyNr LIKE %:param%", nativeQuery = true)
    Page<ArgoFile> findAllSearch(@Param("param") String param, Pageable pageable);
}
