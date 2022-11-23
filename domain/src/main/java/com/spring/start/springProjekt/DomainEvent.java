package com.spring.start.springProjekt;

import java.time.Instant;

public interface DomainEvent {
    Instant getOccurredOn();
}
