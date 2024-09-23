package dev.atomixsoft.event.interfaces;

import dev.atomixsoft.event.Event;

public interface EventListener <T extends Event> {
    void handleEvent(T event);
}
