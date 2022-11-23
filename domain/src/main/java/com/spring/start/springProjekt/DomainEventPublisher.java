package com.spring.start.springProjekt;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
