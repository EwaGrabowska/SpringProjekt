package com.spring.start.springProjekt.NETCDFfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service("argoFileService")
@Transactional
public class ArgoFileServiceImp implements ArgoFileService {

    @Autowired
    private ArgoFileRepository argoFileRepository;


    @Override
    public List<ArgoFile> findAllFiles() {
        return argoFileRepository.findAll();
    }

    @Override
    public void addFile(MultipartFile mFile, String keyName) {
        ArgoFile file = null;
        try {
            file = NetcdfReader.setArgoFileAtribute(mFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.setKeyName(keyName);
        argoFileRepository.save(file);

    }

    @Override
    public Page<ArgoFile> findAll(Pageable pageable) {
        Page<ArgoFile> userList = argoFileRepository.findAll(pageable);
        return userList;
    }

    @Override
    public Page<ArgoFile> findAllSearch(String param, Pageable pageable) {
        Page<ArgoFile> fileList = argoFileRepository.findAllSearch(param, pageable);
        return fileList;
    }

    @Override
    public void deleteFile(Long id) {
        argoFileRepository.deleteById(id);
    }

    @Override
    public ArgoFile getFileById(Long id) {
        return argoFileRepository.findById(id).get();
    }
}
