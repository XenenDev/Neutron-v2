package game.primitives;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.Transform;
import com.neutron.engine.func.Collider;

import java.awt.*;
import java.util.List;

public class Spike extends GameObject implements Collidable, ObjectRenderer, Transform {

    private int x, y;
    private float vx;

    private int SIZE = 50;

    public Spike(int gridX, int gridY, float speed) {
        this.x = gridX*50;
        this.y = gridY*50;
        this.vx = speed;
    }

    @Override
    public void play(GameCore gameCore) {
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        x -= (int) (vx * delta);
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        int[] xs = {
                0,
                SIZE / 2,
                SIZE
        };

        int[] ys = {
                SIZE,
                0,
                SIZE
        };
        // Outer dark red
        r.fillPolygon(xs, ys, 3, new Color(180, 30, 30));
        // Inner brighter red for depth
        int[] xsInner = {5, SIZE / 2, SIZE - 5};
        int[] ysInner = {SIZE, 5, SIZE};
        r.fillPolygon(xsInner, ysInner, 3, new Color(220, 50, 50));
    }

    @Override
    public List<Collider> getColliders() {
        // Approximated rectangular hitbox for now
        return List.of(
                new Collider.RectangleCollider(
                        15,
                        20,
                        SIZE - 30,
                        SIZE - 20,
                        "death"
                )
        );
    }

    @Override
    public Float getVx() {
        return vx;
    }

    @Override
    public Float getVy() {
        return 0f;
    }

    @Override
    public int getZDepth() {
        return 3;
    }

    @Override
    public Integer getX() {
        return x;
    }

    @Override
    public Integer getY() {
        return y;
    }
}
