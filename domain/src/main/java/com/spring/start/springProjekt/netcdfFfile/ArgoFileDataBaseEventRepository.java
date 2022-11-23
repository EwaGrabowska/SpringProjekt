package com.spring.start.springProjekt.netcdfFfile;

import java.time.Instant;

interface ArgoFileDataBaseEventRepository {

    ArgoFileDataBaseEvent save(ArgoFileDataBaseEvent argoFileDataBaseEvent);

    ArgoFileDataBaseEvent findByOccurredOn(Instant instant);
}
