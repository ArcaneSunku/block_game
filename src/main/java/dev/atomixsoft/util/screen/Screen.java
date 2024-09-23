package dev.atomixsoft.util.screen;

import dev.atomixsoft.util.input.Controller;

public interface Screen {

    void show();
    void hide();

    void update(Controller input, float dt);
    void render();

    void dispose();

}
