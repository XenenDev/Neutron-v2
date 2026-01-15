package game;

import com.neutron.engine.*;
import com.neutron.engine.base.*;
import com.neutron.engine.base.interfaces.*;
import com.neutron.engine.func.Collider;
import com.neutron.engine.func.Resource;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class Player extends GameObject implements ObjectRenderer, MouseButtonInput, MouseMovement, KeyboardInput, Collidable {
    private float x, y;
    private int jumpHeight;
    private float vy;
    private float r;
    private float scale;
    private int score;
    private boolean onGround;  // Whether we're touching ground this frame
    private int queueJump;
    private int groundHeight;
    private int frameGroundHeight;  // Tracks ground height during current frame's collisions
    private final Game game;
    private GameCore gc;

    Resource pop = new Resource("res/sound.wav");

    public Player(Game game) {
        super();
        this.game = game;
    }

    public void play(GameCore gameCore) {
        this.x = 300;
        this.y = 100;
        this.r = 0;
        this.vy = 0;
        this.scale = 1f;
        this.score = 0;
        this.groundHeight = 500;
        this.frameGroundHeight = 500;

        this.onGround = false;
        this.queueJump = 0;
        
        this.jumpHeight = 16;
        this.gc = gameCore;
    }

    public void update(GameCore gameCore, float delta) {
        // Apply ground state from last frame's collisions
        boolean wasOnGround = onGround;
        
        if (onGround) {
            groundHeight = frameGroundHeight;
            
            // Check if player has actually fallen off (walked off edge)
            // If they're too far below the ground, they're not really on it anymore
            float bottomY = y + 50;  // Bottom of player collider
            boolean actuallyOnGround = (bottomY <= groundHeight);  // 5px tolerance
            
            if (actuallyOnGround) {
                vy = 0;
                // Account for player collider offset (starts at y=2) - bottom of collider at groundHeight
                y = groundHeight - 50;
                r = 0;

                if (queueJump > 0) {
                    wasOnGround = false;  // Allow gravity this frame since we're jumping
                    vy = -jumpHeight;
                    SoundManager.play(pop, 1.0f, null);
                    queueJump = 0;
                }
            } else {
                // Player has fallen off - not actually on ground despite collision from last frame
                wasOnGround = false;
                r += 4.4f * delta;
            }
        } else {
            r += 4.4f * delta;
        }

        // Reset frame-based ground detection (will be set by duringCollision)
        onGround = false;
        frameGroundHeight = 600;  // Reset to below screen

        // Only apply gravity if not standing on ground
        if (!wasOnGround) {
            y += vy * delta;
            vy += 0.8f * delta;
            if (vy > 20) vy = 20;
        }

        queueJump = Math.max(0, (int) (queueJump - 10f*delta));

        if (Input.isKeyDown(Input.SPACE) || Input.isMouseDown(Input.MOUSE_L_BUTTON)) queueJump = 100;
    }

    private void die() {
        play(gc);
        game.restart();
    }

    public void render(GameCore gameCore, Renderer r) {
        // Outer border - dark
        r.fillSquare(0, 0, 50, r.color(10, 10, 10));
        // Inner glow - vibrant cyan
        r.fillSquare(2, 2, 46, r.color(0, 220, 255));
        // Center highlight
        r.fillSquare(10, 10, 30, r.color(100, 240, 255));
    }

    public void mousePressed(MouseEvent event, Integer x, Integer y, Boolean isOffWindow) {
        if (event.getButton() == Input.MOUSE_L_BUTTON) queueJump = 100;
    }

    public void keyPressed(Input input, KeyEvent event, Integer key) {
        if (key == Input.SPACE) queueJump = 100;
        if (key == Input.W) this.die();
        if (key == Input.S) game.restart();
    }

    public Integer getX() {
        return (int) x;
    }
    public Integer getY() {
        return (int) y;
    }
    public Double getScale() {
        return (double) scale;
    }
    public Double getRotation() {
        return (double) r;
    }
    public int getZDepth() {
        return 1;
    }

    @Override
    public Float getVx() {
        return 0f;
    }

    @Override
    public Float getVy() {
        return vy;
    }

    @Override
    public Double getPivotX() { return 25d; }

    @Override
    public Double getPivotY() { return 25d; }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public List<Collider> getColliders() {
        return List.of(new Collider.RectangleCollider(0, 0, 50, 50, "player"));
    }

    @Override
    public void onEnter(GameObject other, String id) {
        if (id.equals("death")) this.die();
    }

    @Override
    public void duringCollision(GameObject other, float delta) {
        if (other instanceof Collidable collidable) {
            for (Collider collider : collidable.getColliders()) {
                if (collider.getId().equals("ground")) {
                    int otherY = ((Transform) other).getY();
                    // Track the highest ground we're touching this frame
                    if (otherY < frameGroundHeight) {
                        frameGroundHeight = otherY;
                    }
                    onGround = true;
                }
            }
        }
    }
}
