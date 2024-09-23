package dev.atomixsoft.util.screen;

import dev.atomixsoft.util.input.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Screens {

    private final Logger m_Log;
    private final Map<String, Screen> m_Registered;

    private Screen m_Active;

    public Screens() {
        m_Log = LogManager.getLogger();
        m_Registered = new HashMap<>();
    }

    public void update(Controller input, float dt) {
        if(m_Active != null) m_Active.update(input, dt);
    }

    public void render() {
        if(m_Active != null) m_Active.render();
    }

    public void dispose() {
        if(m_Active != null) setScreen("nil");
        m_Registered.values().forEach(Screen::dispose);
    }

    public void addScreen(String name, Screen screen) {
        m_Registered.putIfAbsent(name, screen);
    }

    public void setScreen(String name) {
        if(!name.equalsIgnoreCase("nil") && !m_Registered.containsKey(name)) {
            m_Log.warn("There is no Screen registered to the name {}!", name);
            return;
        }

        if(m_Active != null) m_Active.hide();

        if(name.equalsIgnoreCase("nil")) m_Active = null;
        else m_Active = m_Registered.get(name);

        if(m_Active != null) m_Active.show();
    }

}
