package dev.atomixsoft;

import dev.atomixsoft.event.EventBus;
import dev.atomixsoft.event.interfaces.EventListener;
import dev.atomixsoft.event.types.game.BlockRegEvent;
import dev.atomixsoft.event.types.system.InputEvent;
import dev.atomixsoft.event.types.system.ShutDownEvent;
import dev.atomixsoft.game.screens.GameScreen;
import dev.atomixsoft.game.screens.TestScreen;
import dev.atomixsoft.util.game.BlockRegistry;
import dev.atomixsoft.util.render.RenderCmd;
import dev.atomixsoft.util.input.Controller;
import dev.atomixsoft.util.input.Input;
import dev.atomixsoft.util.screen.Screens;
import dev.atomixsoft.util.Window;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameThread implements Runnable, EventListener<ShutDownEvent> {
    private static final Logger LOG = LogManager.getLogger();

    private static GameThread s_Instance = null;

    public static EventBus event_bus() {
        return s_Instance.m_EventBus;
    }

    public static Vector2f WinSize() {
        return new Vector2f(s_Instance.m_Window.getWidth(), s_Instance.m_Window.getHeight());
    }

    private final EventBus m_EventBus;
    private final Screens m_ScreenManager;
    private final Window m_Window;

    private Input m_Input;
    private Controller m_Controller;
    private Thread m_Thread;
    private volatile boolean m_Running;

    public GameThread() {
        m_Running = false;

        m_EventBus = new EventBus();
        m_ScreenManager = new Screens();
        m_Window = new Window("Project W", 1280, 720, false);

        if(s_Instance == null) s_Instance = this;
    }

    public void start() {
        if(m_Running) return;
        if(m_Thread == null) m_Thread = new Thread(this, "Main_Thread");

        m_Running = true;
        m_Thread.start();
    }

    public void stop() {
        if(!m_Running) return;
        m_Running = false;
    }

    @Override
    public void handleEvent(ShutDownEvent event) {
        if(event.handled) return;

        event.handled = true;
        stop();
    }

    @Override
    public void run() {
        if(!glfwInit()) {
            LOG.error("Failed to initialize GLFW!");
            return;
        }

        m_Window.initialize();
        m_Input = Input.Instance();

        RenderCmd.Init();

        event_bus().register(ShutDownEvent.class, this);
        event_bus().register(BlockRegEvent.class, new BlockRegistry());
        event_bus().register(InputEvent.class, m_Input);

        initialize();

        double accumulator = 0.0;
        double newTime, frameTime;
        double optimal = 1.0 / 60.0;
        double currentTime = System.nanoTime() / 1e9;

        RenderCmd.ClearColor(0.075f, 0.075f, 0.1f);
        while(m_Running) {
            if(m_Window.shouldClose()) {
                stop();
                continue;
            }

            newTime = System.nanoTime() / 1e9;
            frameTime = newTime - currentTime;
            currentTime = newTime;
            accumulator += frameTime;

            RenderCmd.Clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            while(accumulator >= optimal) {
                m_Input.process();

                if(m_Input.keyJustPressed(GLFW_KEY_GRAVE_ACCENT))
                    event_bus().post(new ShutDownEvent());

                m_ScreenManager.update(m_Controller, (float) accumulator);
                accumulator -= optimal;
            }

            m_ScreenManager.render();
            m_Window.update();

            if(!m_Window.usingVSync())
                sleep(currentTime);
        }

        dispose();
    }

    protected void initialize() {
        m_ScreenManager.addScreen("test", new TestScreen());
        m_ScreenManager.addScreen("game", new GameScreen());

        m_Controller = new Controller();

        m_Input.loadDefaultBindings();
        m_Controller.updateBindings();

        m_ScreenManager.setScreen("test");
    }

    protected void dispose() {
        try {
            m_ScreenManager.dispose();
            m_Window.dispose();
            glfwTerminate();

            m_EventBus.shutdown();
            m_Thread.join(1);

            System.exit(0);
        } catch (InterruptedException e) {
            LOG.warn("Interruption found while joining Main_Thread!", e);
            System.exit(-1);
        }
    }

    private void sleep(double currentTime) {
        double desiredTime = 1.0 / 60.0;
        long sleepTime = (long) ((currentTime - System.nanoTime() + desiredTime) / 1e9);

        try {
            if(sleepTime > 0)
                Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOG.debug("Interruption while sleeping thread!", e);
        }
    }

}
