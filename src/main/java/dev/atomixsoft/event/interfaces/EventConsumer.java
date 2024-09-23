package dev.atomixsoft.event.interfaces;

import dev.atomixsoft.event.Event;

public interface EventConsumer <T extends Event> {
    void accept(T event);
}
