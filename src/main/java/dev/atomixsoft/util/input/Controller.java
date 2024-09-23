package dev.atomixsoft.util.input;

import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

import static dev.atomixsoft.event.types.system.InputEvent.InputType;

/**
 * <p>Lets us map Mouse/Keyboard to predefined key functions. Think of it as the average keybinding situation.</p>
 */
public class Controller {
    private final Input m_In;
    private final Map<String, Integer> m_InputMap;

    public Controller() {
        m_In = Input.Instance();
        m_InputMap = new HashMap<>();
    }

    public void updateBindings() {
        for(InputType type : m_In.getBindings().keySet()) {
            int inBind = m_In.getBindings().get(type);
            boolean contains = m_InputMap.containsKey(type.name());
            if(contains && inBind == m_InputMap.get(type.name()))
                continue;

            addBinding(type, m_In.getBindings().get(type));
        }
    }

    public Vector2f mousePos() {
        return m_In.getPos();
    }

    public Vector2f lastMousePos() {
        return m_In.getLastPos();
    }

    public Vector2f mouseOffset() {
        return m_In.getOffset();
    }

    public boolean isPressed(InputType controlName) {
        if(!m_InputMap.containsKey(controlName.name())) return false;
        return m_In.isKeyDown(m_InputMap.get(controlName.name()));
    }

    public boolean isClicked(InputType controlName) {
        if(!m_InputMap.containsKey(controlName.name())) return false;
        return m_In.isMouseButtonDown(m_InputMap.get(controlName.name()));
    }

    public boolean justPressed(InputType controlName) {
        if(!m_InputMap.containsKey(controlName.name())) return false;
        return m_In.keyJustPressed(m_InputMap.get(controlName.name()));
    }

    public boolean justClicked(InputType controlName) {
        if(!m_InputMap.containsKey(controlName.name())) return false;
        return m_In.mouseButtonJustPressed(m_InputMap.get(controlName.name()));
    }

    private void addBinding(InputType controlName, int input) {
        m_InputMap.put(controlName.name(), input);
    }
}