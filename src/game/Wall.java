package game;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.func.Collider;
import com.neutron.engine.func.Random;

import java.awt.*;
import java.util.List;

public class Wall extends GameObject implements ObjectRenderer, Collidable {
    public int height;
    public final int width;
    public int x;
    public float vx;

    public Wall(int x) {
        this.x = x;
        width = 60;
        height = Random.randInt(100, 500);
    }

    public void play(GameCore gameCore) {

    }

    public void update(GameCore gameCore, float delta) {
        x = (int) ((float) x - vx);

        if (x + width < 0) {
            x = 1000;
            height = Random.randInt(100, 500);
        }
    }

    public void render(GameCore gameCore, Renderer r) {
        r.fillRect(0, height, width, 600, Color.WHITE);
        r.fillRect(0, 0, width, height - 150, Color.WHITE);
    }

    public int getZDepth() {
        return 2;
    }

    public Integer getX() { return x; }
    public Integer getY() { return 0; }

    public Float getVx() { return vx; }

    public Float getVy() { return 0f; }

    @Override
    public List<Collider> getColliders() {
        return List.of(
                new Collider.RectangleCollider(0, height, width, 600, "wall"),
                new Collider.RectangleCollider(0, 0, width, height - 150, "wall"),
                new Collider.RectangleCollider((width-10), height-150, 10, 150, "score")
        );
    }
}
