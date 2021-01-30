package com.spring.start.springProjekt.NETCDFfile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArgoFileService {
    List<ArgoFile> findAllFiles();

    void addFile(MultipartFile mFile, String keyName);

    void deleteFile(Long id);

    ArgoFile getFileById(Long id);

    Page<ArgoFile> findAll(Pageable pageable);

    Page<ArgoFile> findAllSearch(String param, Pageable pageable);
}
