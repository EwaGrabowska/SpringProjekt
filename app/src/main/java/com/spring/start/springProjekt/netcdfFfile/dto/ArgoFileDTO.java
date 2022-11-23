package com.spring.start.springProjekt.netcdfFfile.dto;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileSourceId;

import java.time.LocalDateTime;

public interface ArgoFileDTO {

    Long getId();

    String getKeyName();

    int getPlatformNumber();

    String getFeatureType();

    LocalDateTime getCreationDate();

    String getProjectName();

    String getNameOfPrincipalInvestigator();

    ArgoFileSourceId getArgoFileSourceId();
}
