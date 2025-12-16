package game;

import com.neutron.engine.*;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.ui.UIButton;
import com.neutron.engine.base.interfaces.ui.UIGroup;
import com.neutron.engine.base.interfaces.ui.UIObject;
import com.neutron.engine.func.Resource;

import java.awt.*;

public class HUD extends GameObject implements UIGroup, ObjectRenderer {

    Resource bg = new Resource("res/sunset.jpg");

    public void play(GameCore gameCore) {

    }

    public void update(GameCore gameCore, float delta) {

    }

    public void render(GameCore gameCore, Renderer r) {
        //r.shade(0, 0, r.WIDTH, r.HEIGHT, 1f, 1f, new FourCornersShader());
        r.drawImage((Image) bg.get(), 0, 0, r.WIDTH, r.HEIGHT);
    }

    public int getZDepth() {
        return 0;
    }

    public UIObject[] objects() {
        return new UIObject[]{new UIObject(5, 5, 200, 40) {

            @Override
            protected void renderSelf(GameCore gameCore, Renderer r) {
                r.drawText("Score: " + ((Player) ObjectHandler.get(Player.class).getFirst()).getScore(), x, y + 20, Color.BLUE);
                r.drawText("FPS: " + gameCore.getFPS(), x, y + 40, Color.BLUE);
                r.fillRect(5, 5, 200, 40, new Color(1f, 1f, 1f, 0.6f));
            }

            @Override
            public void onPress() {
                System.out.println(10);
            }
        }, new UIButton(50, 50, 100, 40, "Press me!", () -> System.out.println("pressed!"))};
    }

    public Integer getX() {
        return 0;
    }
    public Integer getY() {
        return 0;
    }
}
