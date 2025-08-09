package game;

import com.neutron.engine.*;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.UIObjectRenderer;

import java.awt.*;

public class HUD extends GameObject implements UIObjectRenderer, ObjectRenderer {

    public void play(GameCore gameCore) {

    }

    public void update(GameCore gameCore, float delta) {

    }

    public void render(GameCore gameCore, Renderer r) {
        //r.shade(0, 0, r.WIDTH, r.HEIGHT, 1f, 1f, new FourCornersShader());
        r.drawImage(ResManager.img("res/sunset.jpg"), 0, 0, r.WIDTH, r.HEIGHT);
    }

    public int getZDepth() {
        return 0;
    }

    public void renderUI(GameCore gameCore, Renderer r) {
        r.drawText("Score: " + ((Player) ObjectHandler.get(Player.class).getFirst()).getScore(), 5, 25, Color.BLUE);
        r.drawText("FPS: " + gameCore.getFPS(), 5, 50, Color.BLUE);
    }

    public Integer getX() {
        return 0;
    }
    public Integer getY() {
        return 0;
    }
}
