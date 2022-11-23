package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ArgoFileConfiguration {

    @Bean
    ArgoFileService argoFileService(ArgoFileRepository argoFileRepository, ArgoFileDataBaseEventRepository argoFileDataBaseEventRepository, final DomainEventPublisher publisher) {
        return new ArgoFileServiceImp(argoFileRepository, argoFileDataBaseEventRepository, publisher);
    }
}
