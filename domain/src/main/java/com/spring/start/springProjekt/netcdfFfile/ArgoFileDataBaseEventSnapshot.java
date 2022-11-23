package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileSourceId;
import lombok.Data;

import java.time.Instant;

@Data
public class ArgoFileDataBaseEventSnapshot {
    private Integer id;
    private Instant occurredOn;
    private ArgoFileSourceId sourceId;
    private String eventType;

    public ArgoFileDataBaseEventSnapshot() {
    }

    public ArgoFileDataBaseEventSnapshot(final Integer id, final Instant occurredOn, final ArgoFileSourceId sourceId, final String eventType) {
        this.id = id;
        this.occurredOn = occurredOn;
        this.sourceId = sourceId;
        this.eventType = eventType;
    }
}
