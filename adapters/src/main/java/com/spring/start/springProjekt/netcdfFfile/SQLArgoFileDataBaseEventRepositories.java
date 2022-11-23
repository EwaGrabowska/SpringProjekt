package com.spring.start.springProjekt.netcdfFfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

interface SQLArgoFileDataBaseEventRepository extends JpaRepository<ArgoFileDataBaseEventSnapshot, Integer> {

}

@Repository
class ArgoFileDataBaseEventRepositoryImp implements ArgoFileDataBaseEventRepository {

    private final SQLArgoFileDataBaseEventRepository repository;

    ArgoFileDataBaseEventRepositoryImp(final SQLArgoFileDataBaseEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public ArgoFileDataBaseEvent save(final ArgoFileDataBaseEvent argoFileDataBaseEvent) {
        var argoFileDataBaseEventSnapshotToSave = argoFileDataBaseEvent.getSnapshot();
        var argoFileDataBaseEventSnapshotSaved = repository.save(argoFileDataBaseEventSnapshotToSave);
        return ArgoFileDataBaseEvent.restore(argoFileDataBaseEventSnapshotSaved);
    }

    @Override
    public ArgoFileDataBaseEvent findByOccurredOn(final Instant instant) {
        return repository.findAll().stream()
                .filter(e -> e.getOccurredOn().equals(instant))
                .findFirst()
                .map(ArgoFileDataBaseEvent::restore)
                .orElseThrow(() -> new IllegalArgumentException("No event found with instant:" + instant));

    }

}
