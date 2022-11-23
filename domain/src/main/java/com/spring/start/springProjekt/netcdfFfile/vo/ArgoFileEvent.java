package com.spring.start.springProjekt.netcdfFfile.vo;

import com.spring.start.springProjekt.DomainEvent;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ArgoFileEvent implements DomainEvent {

    @Override
    public Instant getOccurredOn() {
        return this.occurredOn;
    }

    public enum EventType {
        DOWNLOADED, DELETED, SAVED
    }

    private final Instant occurredOn;
    private ArgoFileSourceId sourceId;
    private final EventType eventType;
    private final String userName;

    public ArgoFileEvent(final EventType eventType, final ArgoFileSourceId sourceId, final String userName) {
        this.userName = userName;
        this.occurredOn = Instant.now();
        this.eventType = eventType;
        this.sourceId = sourceId;
    }


}
