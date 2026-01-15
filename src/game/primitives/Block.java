package game.primitives;

import com.neutron.engine.GameCore;
import com.neutron.engine.ObjectHandler;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.Transform;
import com.neutron.engine.func.Collider;
import game.Player;

import java.awt.*;
import java.util.List;

public class Block extends GameObject implements Collidable, ObjectRenderer, Transform {

    private float x, y;
    private float vx;
    private boolean hasAwardedPoints = false;

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
        
        // Award point when block passes the player
        if (!hasAwardedPoints) {
            List<GameObject> players = ObjectHandler.get(Player.class);
            if (!players.isEmpty()) {
                Player player = (Player) players.getFirst();
                // Block has passed player (right edge of block is behind player's left edge)
                if (x + 50 < player.getX()) {
                    player.incrementScore();
                    hasAwardedPoints = true;
                }
            }
        }
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        // Draw with 1px overlap to prevent gaps
        // Base platform color - earthy green
        r.fillRect(0, 0, 51, 51, new Color(100, 180, 100));
        // Top surface highlight
        r.fillRect(0, 0, 51, 25, new Color(120, 200, 120));
        // Border for definition
        r.drawRect(0, 0, 50, 50, new Color(70, 140, 70));
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
