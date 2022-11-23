package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileSourceId;

import java.time.LocalDateTime;

class ArgoFile {

    static ArgoFile restore(ArgoFileSnapshot argoFileSnapshot) {
        return new ArgoFile(argoFileSnapshot.getId(), argoFileSnapshot.getKeyName(),
                argoFileSnapshot.getPlatformNumber(), argoFileSnapshot.getFeatureType(),
                argoFileSnapshot.getCreationDate(), argoFileSnapshot.getProjectName(),
                argoFileSnapshot.getNameOfPrincipalInvestigator(), argoFileSnapshot.getArgoFileSourceId());
    }

    private Long id;
    private String keyName;
    private int platformNumber;
    private String featureType;
    private LocalDateTime creationDate;
    private String projectName;
    private String nameOfPrincipalInvestigator;
    private ArgoFileSourceId argoFileSourceId;

    private ArgoFile(final Long id, final String keyName, final int platformNumber, final String featureType, final LocalDateTime creationDate, final String projectName, final String nameOfPrincipalInvestigator, final ArgoFileSourceId argoFileSourceId) {
        this.id = id;
        this.keyName = keyName;
        this.platformNumber = platformNumber;
        this.featureType = featureType;
        this.creationDate = creationDate;
        this.projectName = projectName;
        this.nameOfPrincipalInvestigator = nameOfPrincipalInvestigator;
        this.argoFileSourceId = argoFileSourceId;
    }

    public ArgoFile() {
    }

    public void setFileAttributes(final int platformNumber, final String featureType, final LocalDateTime creationDate, final String projectName, final String nameOfPrincipalInvestigator) {
        this.platformNumber = platformNumber;
        this.featureType = featureType;
        this.creationDate = creationDate;
        this.projectName = projectName;
        this.nameOfPrincipalInvestigator = nameOfPrincipalInvestigator;
    }

    public void addKeyName(final String keyName) {
        this.keyName = keyName;
    }

    public ArgoFileSnapshot getSnapshot() {
        return new ArgoFileSnapshot(this.id, this.keyName, this.platformNumber, this.featureType, this.creationDate, this.projectName, this.nameOfPrincipalInvestigator, this.argoFileSourceId);
    }

}

