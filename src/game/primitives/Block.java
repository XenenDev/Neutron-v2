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

public class Block extends GameObject implements Collidable, ObjectRenderer, Transform {

    private float x, y;
    private float vx;

    public Block(int gridX, int gridY, float speed) {
        this.x = gridX*50;
        this.y = gridY*50;
        this.vx = speed;
    }

    @Override
    public void play(GameCore gameCore) {

    }

    @Override
    public void update(GameCore gameCore, float delta) {
        x -= vx * delta;
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        r.fillRect(0, 0, 50, 50, r.color(0,255,0));
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(
            new Collider.RectangleCollider(0,0, 50, 25, "ground"),
            new Collider.RectangleCollider(0, 25, 50, 25, "death")
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
        return (int) x;
    }

    @Override
    public Integer getY() {
        return (int) y;
    }
}
