package com.spring.start.springProjekt.netcdfFfile;

interface ArgoFileRepository {

    ArgoFile save(ArgoFile argoFile);

    void deleteById(Long id);
}
