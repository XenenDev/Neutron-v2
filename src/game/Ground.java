package game;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.func.Collider;

import java.util.List;

public class Ground extends GameObject implements Collidable, ObjectRenderer {

    @Override
    public void play(GameCore gameCore) {

    }

    @Override
    public void update(GameCore gameCore, float delta) {

    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        // Dark earthy ground
        r.fillRect(0, 0, 1000, 50, r.color(50, 50, 50));
        // Top surface highlight
        r.fillRect(0, 0, 1000, 10, r.color(80, 80, 80));
    }


    public java.util.List<Collider> getColliders() {
        return List.of(new Collider.RectangleCollider(0, 0, 1000, 50, "ground"));
    }

    @Override
    public Float getVx() {
        return 0f;
    }

    @Override
    public Float getVy() {
        return 0f;
    }

    @Override
    public Integer getX() {
        return 0;
    }

    @Override
    public Integer getY() {
        return 550;
    }

    @Override
    public int getZDepth() {
        return 3;
    }
}
