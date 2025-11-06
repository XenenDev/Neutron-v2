package com.neutron.engine;

import com.neutron.engine.base.BaseGame;
import com.neutron.engine.func.GraphicsFidelity;
import game.Game;

public class GameCore implements Runnable {

    public final int WIDTH, HEIGHT;
    private boolean running;
    private float frameTimeMs;

    private final BaseGame game;
    private final Renderer renderer;


    public GameCore(Game game, String title, int width, int height, String iconPath, GraphicsFidelity gq) {
        this.game = game;

        this.renderer = new Renderer(new Window(title, width, height, iconPath, new Input(this)), gq);

        this.WIDTH = renderer.WIDTH;
        this.HEIGHT = renderer.HEIGHT;

        this.start();
    }

    public void start() {
        if (running) return;
        new Thread(this).start();
        running = true;
    }

    public void stop() {
        running = false;
    }

    public void run() {
        //core game update and render loop
        double delta = 0;
        double UPDATES_PER_SECOND = 1_000_000_000D / 60F; // 60F MEANS 60 UPDATES PER SECOND
        long last = System.nanoTime(), now = System.nanoTime();

        ObjectHandler.init(this);
        game.play(this, renderer);

        while (running) {
            this.frameTimeMs = (float) (System.nanoTime() - now) / 1_000_000F; //NS conversion to MS div by 1e6
            now = System.nanoTime();
            delta += (double)(now - last) / UPDATES_PER_SECOND;
            last = now;


            while (delta >= 1) {
                game.update(this, renderer, (float) delta);
                ObjectHandler.updateObjects(this, (float) delta);
                CollisionManager.checkCollisions((float) delta);
                delta--;
            }

            renderer.clear();
            ObjectHandler.renderObjects(this, renderer);
            //CollisionManager.renderCollisionBoxes(renderer); //REMOVE ME
            ObjectHandler.renderUIObjects(this, renderer);
            renderer.show();
        }
        System.exit(0);
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public float getFrameTimeMs() {
        return frameTimeMs;
    }
    public float getFPS() {
        return 1000 / frameTimeMs;
    }

}
