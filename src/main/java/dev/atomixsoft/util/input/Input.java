package dev.atomixsoft.util.input;

import dev.atomixsoft.GameThread;
import dev.atomixsoft.event.interfaces.EventListener;
import dev.atomixsoft.event.types.system.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static dev.atomixsoft.event.types.system.InputEvent.InputType;

/**
 * <p>Handles input interactions for the app, allows us to manage the key/button states after GLFW gives us feedback.</p>
 */
public class Input implements EventListener <InputEvent>{
    private static Input ms_Instance = null;

    private final Map<Integer, Key> m_Keys;
    private final Map<Integer, MButton> m_MButtons;
    private final Map<InputType, Integer> m_Bindings;

    private final Logger m_Log;
    private final Vector2f m_Pos, m_LastPos, m_Offset;

    private boolean m_Init;

    public static Input Instance() {
        if(ms_Instance == null) ms_Instance = new Input();
        return ms_Instance;
    }

    private Input() {
        m_Keys = new HashMap<>();
        m_MButtons = new HashMap<>();
        m_Bindings = new HashMap<>();

        m_Pos = new Vector2f();
        m_LastPos = new Vector2f(GameThread.WinSize().x / 2f, GameThread.WinSize().y / 2f);
        m_Offset = new Vector2f();

        m_Log = LogManager.getLogger();
        init();
    }

    private void init() {
        for(var k = GLFW_KEY_SPACE; k < GLFW_KEY_LAST; ++k)
            m_Keys.putIfAbsent(k, new Key());

        for(var mb = GLFW_MOUSE_BUTTON_1; mb < GLFW_MOUSE_BUTTON_LAST; ++mb)
            m_MButtons.putIfAbsent(mb, new MButton());

        m_Init = true;
    }

    public void loadDefaultBindings() {
        if(!m_Bindings.isEmpty()) m_Bindings.clear();

        m_Bindings.put(InputType.FORWARD,      GLFW_KEY_W);
        m_Bindings.put(InputType.BACKWARD,     GLFW_KEY_S);
        m_Bindings.put(InputType.STRAFE_LEFT,  GLFW_KEY_A);
        m_Bindings.put(InputType.STRAFE_RIGHT, GLFW_KEY_D);

        m_Bindings.put(InputType.JUMP,   GLFW_KEY_SPACE);
        m_Bindings.put(InputType.ACTION, GLFW_KEY_E);
        m_Bindings.put(InputType.CROUCH, GLFW_KEY_LEFT_CONTROL);

    }

    public void process() {
        m_Keys.values().forEach(Key::update);
        m_MButtons.values().forEach(MButton::update);
    }

    @Override
    public void handleEvent(InputEvent event) {
        Key mapped = m_Keys.get(getBindings().get(event.getInputType()));
        if(mapped == null) return;

        mapped.toggle(event.isPressed());
        event.handled = true;
    }

    public Map<InputType, Integer> getBindings() {
        return m_Bindings;
    }

    public Vector2f getPos() {
        return m_Pos;
    }

    public Vector2f getLastPos() {
        return m_LastPos;
    }

    public Vector2f getOffset() {
        return m_Offset;
    }

    public boolean isKeyDown(int key) {
        return m_Keys.get(key).down;
    }

    public boolean mouseButtonJustPressed(int key) {
        return m_MButtons.get(key).justClicked;
    }

    public boolean isMouseButtonDown(int mb) {
        return m_MButtons.get(mb).down;
    }

    public boolean keyJustPressed(int key) {
        return m_Keys.get(key).justPressed;
    }

    public static void key_callback(long window, int key, int scancode, int action, int mods) {
        if(!ms_Instance.m_Bindings.containsValue(key)) {
            Key mapped = Instance().m_Keys.get(key);
            if(mapped == null) return;

            mapped.toggle(action == GLFW_PRESS);
        } else {
            for(InputType type : ms_Instance.m_Bindings.keySet()) {
                if(ms_Instance.m_Bindings.get(type) != key) continue;

                InputEvent event = new InputEvent(type, action != GLFW_RELEASE);
                GameThread.event_bus().post(event);
            }
        }
    }

    public static void mouse_button_callback(long window, int button, int action, int mods) {
        if(!ms_Instance.m_Bindings.containsValue(button)) {
            MButton mapped = Instance().m_MButtons.get(button);
            if(mapped == null) return;

            mapped.toggle(action != GLFW_RELEASE);
        } else {
            for(InputType type : ms_Instance.m_Bindings.keySet()) {
                if(ms_Instance.m_Bindings.get(type) != button) continue;

                InputEvent event = new InputEvent(type, action == GLFW_PRESS);
                GameThread.event_bus().post(event);
                break;
            }
        }
    }

    public static void cursor_pos_callback(long window, double xPos, double yPos) {
        Input input = ms_Instance;
        // If it's the first time initializing the cursor position
        if (input.m_Init) {
            input.m_LastPos.x = (float) xPos;
            input.m_LastPos.y = (float) yPos;
            input.m_Init = false;
        }

        // Assign current position
        input.m_Pos.x = (float) xPos;
        input.m_Pos.y = (float) yPos;

        // Calculate mouse offset
        input.m_Offset.x = input.m_Pos.x - input.m_LastPos.x; // Difference in X
        input.m_Offset.y = input.m_LastPos.y - input.m_Pos.y; // Difference in Y (inverted for 'up' direction)

        // Update last position to current
        input.m_LastPos.x = input.m_Pos.x;
        input.m_LastPos.y = input.m_Pos.y;
    }

    /**
     * <p>Basic class for a Keyboard key, lets us know if its just been pressed or is held.</p>
     */
    private static class Key {
        int presses = 0, absorbs = 0;
        boolean down = false, justPressed = false;

        void toggle(boolean pressed) {
            if(pressed != down)
                down = pressed;

            if(pressed) ++presses;
        }

        void update() {
            if(absorbs < presses) {
                justPressed = true;
                ++absorbs;
            } else {
                justPressed = false;
            }
        }
    }

    /**
     * <p>Basic class for a Mouse Button, lets us know if its just been clicked or is held.</p>
     */
    private static class MButton {
        int presses = 0, absorbs = 0;
        boolean down = false, justClicked = false;

        void toggle(boolean pressed) {
            if(pressed != down)
                down = pressed;

            if(pressed) ++presses;
        }

        void update() {
            if(absorbs < presses) {
                justClicked = true;
                ++absorbs;
            } else {
                justClicked = false;
            }
        }
    }
}
