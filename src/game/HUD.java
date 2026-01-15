package game;

import com.neutron.engine.*;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.ui.UIButton;
import com.neutron.engine.base.interfaces.ui.UIGroup;
import com.neutron.engine.base.interfaces.ui.UIObject;
import com.neutron.engine.func.Resource;
import game.shaders.GradientShader;

import java.awt.*;

public class HUD extends GameObject implements UIGroup, ObjectRenderer {

    Resource bg = new Resource("res/sunset.jpg");

    public void play(GameCore gameCore) {

    }

    public void update(GameCore gameCore, float delta) {

    }

    public void render(GameCore gameCore, Renderer r) {
        // Gradient background from deep purple-blue to lighter blue
        r.shade(0, 0, r.WIDTH, r.HEIGHT, 
            new GradientShader(
                new Color(25, 25, 60),    // Deep blue-purple at top
                new Color(60, 100, 180)    // Lighter blue at bottom
            )
        );
    }

    public int getZDepth() {
        return 0;
    }

    public UIObject[] objects() {
        return new UIObject[]{new UIObject(5, 5, 200, 40) {

            @Override
            protected void renderSelf(GameCore gameCore, Renderer r) {
                // Draw text shadow
                r.drawText("Score: " + ((Player) ObjectHandler.get(Player.class).getFirst()).getScore(), x + 2, y + 22, r.color(0,0,0,100));
                r.drawText("FPS: " + gameCore.getFPS(), x + 2, y + 42, r.color(0,0,0,100));
                // Draw text
                r.drawText("Score: " + ((Player) ObjectHandler.get(Player.class).getFirst()).getScore(), x, y + 20, Color.WHITE);
                r.drawText("FPS: " + gameCore.getFPS(), x, y + 40, Color.WHITE);
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
