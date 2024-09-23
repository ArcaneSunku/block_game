package dev.atomixsoft.game.screens;

import dev.atomixsoft.GameThread;
import dev.atomixsoft.event.types.game.BlockRegEvent;
import dev.atomixsoft.game.world.Block;
import dev.atomixsoft.render.camera.FPSCamera;
import dev.atomixsoft.render.camera.OrthoCamera;
import dev.atomixsoft.render.font.Font;
import dev.atomixsoft.render.render2D.SpriteBatch;
import dev.atomixsoft.render.Shader;
import dev.atomixsoft.render.Texture;
import dev.atomixsoft.render.render3D.Model;
import dev.atomixsoft.util.game.BlockRegistry;
import dev.atomixsoft.util.input.Controller;
import dev.atomixsoft.util.screen.Screen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.atomixsoft.event.types.system.InputEvent.InputType;
import static dev.atomixsoft.game.world.Block.*;

public class TestScreen implements Screen {

    private static final Logger LOG = LogManager.getLogger();

    private Shader shader, ui;
    private Texture white;
    private Font font;

    private Block dirt;

    private FPSCamera camera;
    private OrthoCamera uiCam;
    private SpriteBatch uiBatch;

    @Override
    public void show() {
        shader = new Shader("/shaders/3d");

        ui = new Shader("/shaders/2d");
        uiBatch = new SpriteBatch(ui);
        uiCam = new OrthoCamera(1280, 720);

        camera = new FPSCamera();
        font = Font.createFont("/fonts/font_w.png", 16, 16);

        byte[] bytes = new byte[3];
        Arrays.fill(bytes, (byte) 255);

        ByteBuffer col = BufferUtils.createByteBuffer(bytes.length);
        col.put(0, bytes);
        white = new Texture(1, 1, col);

        shader.createUniform("uTextures");
        shader.createUniform("uProjectionViewModel");

        int[] sampler = new int[32];
        for(var sample = 0; sample < sampler.length; ++sample)
            sampler[sample] = sample;

        shader.setUniformiv("uTextures", sampler);

        registerBlocks();

        dirt = BlockRegistry.CreateBlock((byte) 1);

        dirt.getPosition().y = -3f;
    }

    @Override
    public void hide() {
        shader.dispose();
        ui.dispose();

        white.dispose();
    }

    private double fps = 0;
    private int frameCount = 0;
    private double fpsTime = 0.0;

    @Override
    public void update(Controller input, float dt) {
        camera.addRotation(input.mouseOffset().x, input.mouseOffset().y);
        input.mouseOffset().set(0);

        float speed = 3f * dt;
        if(input.isPressed(InputType.FORWARD)) camera.moveForward(speed);
        else if(input.isPressed(InputType.BACKWARD)) camera.moveBackward(speed);

        if(input.isPressed(InputType.STRAFE_LEFT)) camera.moveLeft(speed);
        else if(input.isPressed(InputType.STRAFE_RIGHT)) camera.moveRight(speed);

        if(input.isPressed(InputType.JUMP)) camera.moveUp(speed);
        else if(input.isPressed(InputType.CROUCH)) camera.moveDown(speed);

        // Accumulate frame time
        fpsTime += dt;
        frameCount++;

        // Calculate and log FPS every second
        if (fpsTime >= 1.0) {
            fps = frameCount / fpsTime;
            // Reset for the next interval
            frameCount = 0;
            fpsTime = 0.0;
        }
    }

    @Override
    public void render() {
        shader.bind();

        Matrix4f projectionView = camera.getCombined();


        Model model = dirt.getModel();
        Matrix4f mMatrix = new Matrix4f();
        Vector3f rotation = model.getRotation();
        mMatrix.translate(model.getPosition())
               .scale(model.getScale())
               .rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));

        shader.setUniformMat4("uProjectionViewModel", projectionView.mul(mMatrix));

        model.draw();

        uiBatch.begin(uiCam);
        font.drawText(uiBatch, String.format("fps:%.0f", fps), 0, 385);
        font.drawText(uiBatch, "Hello, World!\nHow are you?\nThat's good", 0, 0, 16f);
        uiBatch.end();
    }

    @Override
    public void dispose() {

    }

    private void registerBlocks() {
        List<BlockData> blockData = new ArrayList<>();

        // Air
        BlockData data = new BlockData();
        data.id = 0;
        data.solid = 0;
        data.breakable = 0;
        data.lightSource = 0;
        blockData.add(data);

        // Dirt
        data = new BlockData();
        data.id = 1;
        data.solid = 1;
        data.breakable = 1;
        data.lightSource = 0;
        blockData.add(data);

        // Grass
        data = new BlockData();
        data.id = 2;
        data.solid = 1;
        data.breakable = 1;
        data.lightSource = 0;
        blockData.add(data);

        for(BlockData bData : blockData) {
            BlockRegEvent event = new BlockRegEvent(bData);
            GameThread.event_bus().post(event);
        }
    }

}
