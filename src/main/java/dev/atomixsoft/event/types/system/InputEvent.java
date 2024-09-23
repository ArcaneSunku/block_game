package dev.atomixsoft.event.types.system;

import dev.atomixsoft.event.Event;

public class InputEvent extends Event {
    public enum InputType {
        CROUCH,
        JUMP, ACTION,

        FORWARD, BACKWARD,
        STRAFE_LEFT, STRAFE_RIGHT,
    }

    private final InputType m_InputType;
    private final boolean m_Pressed;

    public InputEvent(InputType inputType, boolean pressed) {
        super("input_event");
        m_InputType = inputType;
        m_Pressed = pressed;
    }

    public InputType getInputType() {
        return m_InputType;
    }

    public boolean isPressed() {
        return m_Pressed;
    }
}
