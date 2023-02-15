package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.netcdfFfile.dto.ArgoFileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

interface SQLArgoFileQueryRepository extends Repository<ArgoFileSnapshot, Long>, ArgoFileQueryRepository {

    ArgoFileDTO findArgoFileSnapshotById(Long id);

    @Query(value = "SELECT * FROM file WHERE platform_number LIKE %:param% OR feature_type LIKE %:param% OR creation_date LIKE %:param% OR project_name LIKE %:param% OR name_of_principal_investigator LIKE %:param%", nativeQuery = true)
    Page<ArgoFileDTO> findallsearchBy(@Param("param") String param, Pageable pageable);

    Page<ArgoFileDTO> findAllBy(Pageable pageable);
}

interface SQLArgoFileRepository extends JpaRepository<ArgoFileSnapshot, Long> {
}

@org.springframework.stereotype.Repository
class ArgoFileRepositoryImp implements ArgoFileRepository {

    private final SQLArgoFileRepository repository;

    ArgoFileRepositoryImp(final SQLArgoFileRepository repository) {
        this.repository = repository;
    }

    @Override
    public ArgoFile save(final ArgoFile argoFile) {
        ArgoFileSnapshot argoFileSnapshot = repository.save(argoFile.getSnapshot());
        return ArgoFile.restore(argoFileSnapshot);
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }

}
