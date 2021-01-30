package com.spring.start.springProjekt.NETCDFfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArgoFile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "YOUR_ENTITY_SEQ_File")
    @SequenceGenerator(name = "YOUR_ENTITY_SEQ_File", sequenceName = "YOUR_ENTITY_SEQ_File", allocationSize = 1)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "keyName")
    @NotNull
    private String keyName;

    @Column(name = "platformNumber")
    @NotNull
    private int platformNumber;

    @Column(name = "featureType")
    @NotNull
    private String featureType;

    @Column(name = "creationDate")
    @NotNull
    private LocalDateTime creationDate;

    @Column(name = "projectName")
    @NotNull
    private String projectName;

    @Column(name = "nameOfPrincipalInvestigator")
    @NotNull
    private String nameOfPrincipalInvestigator;

}

