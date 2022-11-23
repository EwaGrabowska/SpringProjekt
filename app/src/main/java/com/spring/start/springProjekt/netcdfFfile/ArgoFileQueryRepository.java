package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.netcdfFfile.dto.ArgoFileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArgoFileQueryRepository {

    ArgoFileDTO findArgoFileSnapshotById(Long id);

    Page<ArgoFileDTO> findallsearchBy(String param, Pageable pageable);

    Page<ArgoFileDTO> findAllBy(Pageable pageable);

}
