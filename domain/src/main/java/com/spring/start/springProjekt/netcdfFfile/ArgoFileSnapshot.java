package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileSourceId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArgoFileSnapshot {

    private Long id;
    private String keyName;
    private int platformNumber;
    private String featureType;
    private LocalDateTime creationDate;
    private String projectName;
    private String nameOfPrincipalInvestigator;
    private ArgoFileSourceId argoFileSourceId;

    ArgoFileSnapshot(final Long id, final String keyName, final int platformNumber, final String featureType, final LocalDateTime creationDate, final String projectName, final String nameOfPrincipalInvestigator, final ArgoFileSourceId argoFileSourceId) {
        this.id = id;
        this.keyName = keyName;
        this.platformNumber = platformNumber;
        this.featureType = featureType;
        this.creationDate = creationDate;
        this.projectName = projectName;
        this.nameOfPrincipalInvestigator = nameOfPrincipalInvestigator;
        this.argoFileSourceId = argoFileSourceId;
    }

    protected ArgoFileSnapshot(){
    }
}
