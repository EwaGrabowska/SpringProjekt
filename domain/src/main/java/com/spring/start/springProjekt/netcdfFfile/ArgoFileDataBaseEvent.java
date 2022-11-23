package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileSourceId;

import java.time.Instant;

class ArgoFileDataBaseEvent {

    static ArgoFileDataBaseEvent restore(ArgoFileDataBaseEventSnapshot source) {
        return new ArgoFileDataBaseEvent(source.getId(), source.getOccurredOn(), source.getSourceId(), source.getEventType());
    }

    private Integer id;
    private final Instant occurredOn;
    private final ArgoFileSourceId sourceId;
    private final String eventType;

    ArgoFileDataBaseEvent(final Integer id, final Instant occurredOn, final ArgoFileSourceId sourceId, final String eventType) {
        this.id = id;
        this.occurredOn = occurredOn;
        this.sourceId = sourceId;
        this.eventType = eventType;
    }

    ArgoFileDataBaseEvent(final Instant occurredOn, final ArgoFileSourceId sourceId, final String eventType) {
        this.occurredOn = occurredOn;
        this.sourceId = sourceId;
        this.eventType = eventType;
    }

    ArgoFileDataBaseEventSnapshot getSnapshot() {
        return new ArgoFileDataBaseEventSnapshot(this.id, this.occurredOn, this.sourceId, this.eventType);
    }
}
