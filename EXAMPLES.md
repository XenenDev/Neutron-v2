# Neutron v2 Code Examples

Practical code examples and tutorials for the Neutron v2 Game Engine.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Basic Game Objects](#basic-game-objects)
3. [Movement and Physics](#movement-and-physics)
4. [Collision Detection](#collision-detection)
5. [User Input](#user-input)
6. [Rendering Techniques](#rendering-techniques)
7. [Audio](#audio)
8. [UI and HUD](#ui-and-hud)
9. [Complete Mini-Games](#complete-mini-games)

---

## Getting Started

### Minimal Game Setup

```java
package game;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.BaseGame;
import com.neutron.engine.func.GraphicsFidelity;

public class MinimalGame extends BaseGame {
    public static void main(String[] args) {
        // Create the game with simple graphics settings
        new GameCore(
            new MinimalGame(),
            "My First Game",
            800,
            600,
            "res/icon.png",
            GraphicsFidelity.HIGH  // Use preset: HIGH, MEDIUM, or LOW
        );
    }

    @Override
    public void play(GameCore gameCore, Renderer r) {
        // Game initialization
        System.out.println("Game started!");
    }

    @Override
    public void update(GameCore gameCore, Renderer r, float delta) {
        // Game loop logic
    }
}
```

### Custom Graphics Settings

```java
new GameCore(
    new MyGame(),
    "Advanced Graphics",
    1920,
    1080,
    "res/icon.png",
    new GraphicsFidelity() {
        public boolean useGlobalAA() { return true; }
        public boolean useQualityRendering() { return true; }
        public boolean useBilinearSampling() { return true; }
        public boolean useSubPixelFontRendering() { return false; }
        public boolean useAAForTextOnly() { return false; }
    }
);
```

---

## Basic Game Objects

### Simple Square

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import java.awt.Color;

public class Square extends GameObject implements ObjectRenderer {
    private int x, y;
    private int size;
    private Color color;

    public Square(int x, int y, int size, Color color) {
        super();
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    @Override
    public void play(GameCore gameCore) {
        System.out.println("Square created at " + x + ", " + y);
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Update logic here
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        r.fillSquare(0, 0, size, color);
    }

    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 0; }
}
```

### Sprite Object

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.func.Resource;
import java.awt.Image;

public class Sprite extends GameObject implements ObjectRenderer {
    private static final Resource SPRITE_IMAGE = new Resource("res/sprite.png");
    
    private int x, y;
    private double scale;
    private double rotation;

    public Sprite(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.scale = 1.0;
        this.rotation = 0.0;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Slowly rotate
        rotation += 1.0 * delta;
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        Image img = (Image) SPRITE_IMAGE.get();
        r.drawImage(img, 0, 0);
    }

    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return scale; }
    
    @Override
    public Double getRotation() { return rotation; }
    
    @Override
    public int getZDepth() { return 0; }
    
    // Setters for external control
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setScale(double scale) {
        this.scale = scale;
    }
}
```

---

## Movement and Physics

### Moving Object with Velocity

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.Movable;
import java.awt.Color;

public class MovingBall extends GameObject implements ObjectRenderer, Movable {
    private float x, y;
    private float vx, vy;
    private int radius;
    private Color color;

    public MovingBall(float x, float y) {
        super();
        this.x = x;
        this.y = y;
        this.vx = 5.0f;
        this.vy = 3.0f;
        this.radius = 20;
        this.color = Color.RED;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Update position based on velocity
        x += vx * delta;
        y += vy * delta;

        // Bounce off walls
        if (x < 0 || x > gameCore.WIDTH) {
            vx = -vx;
        }
        if (y < 0 || y > gameCore.HEIGHT) {
            vy = -vy;
        }
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        r.fillCircle(0, 0, radius, color);
    }

    @Override
    public Integer getX() { return (int) x; }
    
    @Override
    public Integer getY() { return (int) y; }
    
    @Override
    public Float getVx() { return vx; }
    
    @Override
    public Float getVy() { return vy; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 0; }
}
```

### Gravity and Jumping

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Input;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.Movable;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class PlatformCharacter extends GameObject implements ObjectRenderer, Movable {
    private float x, y;
    private float vx, vy;
    private final float GRAVITY = 0.5f;
    private final float JUMP_FORCE = -12f;
    private final float MOVE_SPEED = 5f;
    private boolean onGround = false;

    public PlatformCharacter(float x, float y) {
        super();
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Horizontal movement
        vx = 0;
        if (Input.isKeyDown(KeyEvent.VK_LEFT) || Input.isKeyDown(KeyEvent.VK_A)) {
            vx = -MOVE_SPEED;
        }
        if (Input.isKeyDown(KeyEvent.VK_RIGHT) || Input.isKeyDown(KeyEvent.VK_D)) {
            vx = MOVE_SPEED;
        }

        // Jumping
        if ((Input.isKeyDown(KeyEvent.VK_UP) || Input.isKeyDown(KeyEvent.VK_W) 
             || Input.isKeyDown(KeyEvent.VK_SPACE)) && onGround) {
            vy = JUMP_FORCE;
            onGround = false;
        }

        // Apply gravity
        vy += GRAVITY * delta;

        // Update position
        x += vx * delta;
        y += vy * delta;

        // Ground collision (simple)
        if (y >= gameCore.HEIGHT - 50) {
            y = gameCore.HEIGHT - 50;
            vy = 0;
            onGround = true;
        }

        // Keep in bounds
        x = Math.max(0, Math.min(x, gameCore.WIDTH - 40));
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        r.fillRect(0, 0, 40, 50, Color.BLUE);
    }

    @Override
    public Integer getX() { return (int) x; }
    
    @Override
    public Integer getY() { return (int) y; }
    
    @Override
    public Float getVx() { return vx; }
    
    @Override
    public Float getVy() { return vy; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 1; }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
```

---

## Collision Detection

### Collidable Object

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.Transform;
import com.neutron.engine.func.Collider;
import java.awt.Color;
import java.util.List;

public class Coin extends GameObject implements ObjectRenderer, Collidable, Transform {
    private int x, y;
    private boolean collected = false;

    public Coin(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        if (collected) {
            this.delete();
        }
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        if (!collected) {
            r.fillCircle(0, 0, 15, Color.YELLOW);
        }
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(
            new Collider.CircleCollider(0, 0, 15, "coin")
        );
    }

    @Override
    public void onEnter(GameObject other, String colliderId) {
        // Collision started
        if (other instanceof Player) {
            collected = true;
            ((Player) other).addScore(10);
        }
    }

    @Override
    public void duringCollision(GameObject other, float delta) {
        // Collision ongoing
    }

    @Override
    public void onExit(GameObject other) {
        // Collision ended
    }

    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 1; }
}
```

### Enemy with Multiple Colliders

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.Transform;
import com.neutron.engine.func.Collider;
import java.awt.Color;
import java.util.List;

public class Enemy extends GameObject implements ObjectRenderer, Collidable, Transform {
    private int x, y;
    private int health;
    private boolean isStunned = false;

    public Enemy(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.health = 100;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        if (health <= 0) {
            this.delete();
        }
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        // Body
        r.fillRect(0, 20, 50, 60, isStunned ? Color.GRAY : Color.RED);
        // Head
        r.fillCircle(25, 10, 20, Color.PINK);
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(
            new Collider.RectangleCollider(0, 20, 50, 60, "body"),
            new Collider.CircleCollider(25, 10, 20, "head")
        );
    }

    @Override
    public void onEnter(GameObject other, String colliderId) {
        if (other instanceof Bullet) {
            if (colliderId.equals("head")) {
                // Headshot - instant kill
                health = 0;
            } else {
                // Body shot - damage
                health -= 25;
            }
            other.delete();
        } else if (other instanceof Player) {
            if (!isStunned) {
                ((Player) other).takeDamage(10);
            }
        }
    }

    @Override
    public void duringCollision(GameObject other, float delta) {
        // Continuous collision logic
    }

    @Override
    public void onExit(GameObject other) {
        // Collision ended
    }

    public void stun() {
        isStunned = true;
    }

    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 1; }
}
```

---

## User Input

### Keyboard-Controlled Player

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Input;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.KeyboardInput;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class KeyboardPlayer extends GameObject implements ObjectRenderer, KeyboardInput {
    private int x, y;
    private int speed;

    public KeyboardPlayer(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.speed = 5;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Continuous input checking (good for movement)
        if (Input.isKeyDown(KeyEvent.VK_W)) y -= speed;
        if (Input.isKeyDown(KeyEvent.VK_S)) y += speed;
        if (Input.isKeyDown(KeyEvent.VK_A)) x -= speed;
        if (Input.isKeyDown(KeyEvent.VK_D)) x += speed;

        // Keep in bounds
        x = Math.max(0, Math.min(x, gameCore.WIDTH - 40));
        y = Math.max(0, Math.min(y, gameCore.HEIGHT - 40));
    }

    @Override
    public void keyPressed(Input input, KeyEvent e, Integer keyCode) {
        // Event-based input (good for single actions)
        if (keyCode == KeyEvent.VK_SPACE) {
            shoot();
        } else if (keyCode == KeyEvent.VK_E) {
            interact();
        }
    }

    @Override
    public void keyReleased(Input input, KeyEvent e, Integer keyCode) {
        // Handle key release
    }

    private void shoot() {
        new Bullet(x + 20, y);
    }

    private void interact() {
        System.out.println("Interacting...");
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        r.fillRect(0, 0, 40, 40, Color.GREEN);
    }

    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 1; }
}
```

### Mouse-Following Object

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Input;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.MouseButtonInput;
import com.neutron.engine.base.interfaces.MouseMovement;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class MouseFollower extends GameObject 
    implements ObjectRenderer, MouseMovement, MouseButtonInput {
    
    private int x, y;
    private int targetX, targetY;
    private boolean clicked = false;

    public MouseFollower() {
        super();
    }

    @Override
    public void play(GameCore gameCore) {
        x = gameCore.WIDTH / 2;
        y = gameCore.HEIGHT / 2;
        targetX = x;
        targetY = y;
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Smooth following
        int dx = (targetX - x) / 10;
        int dy = (targetY - y) / 10;
        
        x += dx;
        y += dy;
    }

    @Override
    public void mouseMoved(MouseEvent e, Integer mx, Integer my, Boolean isOffWindow) {
        if (!isOffWindow) {
            // Convert screen to world coordinates
            targetX = Input.getMouseXinWorld();
            targetY = Input.getMouseYinWorld();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e, Integer mx, Integer my, Boolean isOffWindow) {
        mouseMoved(e, mx, my, isOffWindow);
    }

    @Override
    public void mousePressed(MouseEvent e, Integer mx, Integer my, Boolean isOffWindow) {
        if (e.getButton() == Input.MOUSE_L_BUTTON) {
            clicked = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e, Integer mx, Integer my, Boolean isOffWindow) {
        clicked = false;
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        Color color = clicked ? Color.RED : Color.BLUE;
        r.fillCircle(0, 0, 20, color);
        
        // Draw line to target
        r.drawLine(0, 0, targetX - x, targetY - y, Color.GRAY);
    }

    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 1; }
}
```

---

## Rendering Techniques

### Animated Sprite

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.func.Resource;
import java.awt.Image;

public class AnimatedSprite extends GameObject implements ObjectRenderer {
    private Resource[] frames;
    private int currentFrame;
    private float animationTimer;
    private float frameDuration; // seconds per frame
    private int x, y;

    public AnimatedSprite(int x, int y, String[] framePaths, float fps) {
        super();
        this.x = x;
        this.y = y;
        this.frameDuration = 1.0f / fps;
        this.frames = new Resource[framePaths.length];
        
        for (int i = 0; i < framePaths.length; i++) {
            frames[i] = new Resource(framePaths[i]);
        }
        
        this.currentFrame = 0;
        this.animationTimer = 0;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        animationTimer += delta / 60.0f; // Convert delta to seconds
        
        if (animationTimer >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.length;
            animationTimer = 0;
        }
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        Image img = (Image) frames[currentFrame].get();
        r.drawImage(img, 0, 0);
    }

    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 0; }
}
```

### Custom Shader Effects

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.func.Shader;
import java.awt.Color;

public class ShaderEffect extends GameObject implements ObjectRenderer {
    private int x, y;
    private float time;

    public ShaderEffect(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.time = 0;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        time += delta / 60.0f;
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        // Animated gradient shader
        Shader gradientShader = (px, py, u, v, w, h) -> {
            float wave = (float) Math.sin(time + u * 5);
            int red = (int) (127 + 127 * Math.sin(u * 3 + time));
            int green = (int) (127 + 127 * wave);
            int blue = (int) (127 + 127 * Math.cos(v * 3 + time));
            return new Color(red, green, blue);
        };
        
        r.shade(0, 0, 200, 200, gradientShader);
    }

    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    @Override
    public int getZDepth() { return 0; }
}
```

---

## Audio

### Sound Effects Manager

```java
package game.managers;

import com.neutron.engine.SoundManager;
import com.neutron.engine.func.Resource;

public class GameSounds {
    // Preload all sounds
    private static final Resource JUMP = new Resource("res/sounds/jump.wav");
    private static final Resource COIN = new Resource("res/sounds/coin.wav");
    private static final Resource HIT = new Resource("res/sounds/hit.wav");
    private static final Resource BGM = new Resource("res/sounds/music.wav");

    public static void playJump() {
        SoundManager.play(JUMP, 0.7f, null, "sfx", "player");
    }

    public static void playCoin() {
        SoundManager.play(COIN, 0.5f, null, "sfx", "item");
    }

    public static void playHit() {
        SoundManager.play(HIT, 0.8f, null, "sfx", "combat");
    }

    public static void playBackgroundMusic() {
        SoundManager.play(BGM, 0.3f, null, "music", "background");
    }

    public static void stopAllSFX() {
        SoundManager.stopByTag("sfx");
    }

    public static void stopMusic() {
        SoundManager.stopByTag("music");
    }

    public static void setVolume(float volume) {
        SoundManager.setMainVolume(volume);
    }
}
```

### Music Player Object

```java
package game.objects;

import com.neutron.engine.GameCore;
import com.neutron.engine.SoundManager;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.func.Resource;

public class MusicPlayer extends GameObject {
    private Resource[] tracks;
    private int currentTrack;
    private boolean isPlaying;

    public MusicPlayer(String... trackPaths) {
        super();
        this.tracks = new Resource[trackPaths.length];
        for (int i = 0; i < trackPaths.length; i++) {
            tracks[i] = new Resource(trackPaths[i]);
        }
        this.currentTrack = 0;
        this.isPlaying = false;
    }

    @Override
    public void play(GameCore gameCore) {
        playTrack(0);
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Music management logic
    }

    public void playTrack(int index) {
        if (index >= 0 && index < tracks.length) {
            SoundManager.stopByTag("music");
            currentTrack = index;
            SoundManager.play(tracks[currentTrack], 0.5f, null, "music");
            isPlaying = true;
        }
    }

    public void nextTrack() {
        playTrack((currentTrack + 1) % tracks.length);
    }

    public void previousTrack() {
        playTrack((currentTrack - 1 + tracks.length) % tracks.length);
    }

    public void stop() {
        SoundManager.stopByTag("music");
        isPlaying = false;
    }

    public void togglePause() {
        if (isPlaying) {
            stop();
        } else {
            playTrack(currentTrack);
        }
    }
}
```

---

## UI and HUD

### Score Display

```java
package game.ui;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.UIObjectRenderer;
import java.awt.Color;
import java.awt.Font;

public class ScoreDisplay extends GameObject implements UIObjectRenderer {
    private int score;
    private Font font;

    public ScoreDisplay() {
        super();
        this.score = 0;
    }

    @Override
    public void play(GameCore gameCore) {
        font = new Font("Arial", Font.BOLD, 32);
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Update logic
    }

    @Override
    public void renderUI(GameCore gameCore, Renderer r) {
        r.setFont(font);
        r.drawText("Score: " + score, 20, 50, Color.WHITE);
        
        // Shadow effect
        r.drawText("Score: " + score, 22, 52, Color.BLACK);
        r.drawText("Score: " + score, 20, 50, Color.YELLOW);
    }

    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }
}
```

### Button

```java
package game.ui;

import com.neutron.engine.GameCore;
import com.neutron.engine.Input;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.MouseButtonInput;
import com.neutron.engine.base.interfaces.MouseMovement;
import com.neutron.engine.base.interfaces.UIObjectRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

public class Button extends GameObject 
    implements UIObjectRenderer, MouseButtonInput, MouseMovement {
    
    private int x, y, width, height;
    private String text;
    private Color normalColor, hoverColor, clickColor;
    private Color currentColor;
    private Runnable onClick;
    private boolean isHovered;
    private Font font;

    public Button(int x, int y, int width, int height, String text, Runnable onClick) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onClick = onClick;
        this.normalColor = new Color(70, 70, 70);
        this.hoverColor = new Color(100, 100, 100);
        this.clickColor = new Color(50, 50, 50);
        this.currentColor = normalColor;
        this.isHovered = false;
    }

    @Override
    public void play(GameCore gameCore) {
        font = new Font("Arial", Font.BOLD, 20);
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Update logic
    }

    @Override
    public void renderUI(GameCore gameCore, Renderer r) {
        // Draw button background
        r.fillRect(x, y, width, height, currentColor);
        
        // Draw border
        r.setLineWidth(2);
        r.drawRect(x, y, width, height, Color.WHITE);
        
        // Draw text (centered)
        r.setFont(font);
        int textX = x + width / 2 - text.length() * 6;
        int textY = y + height / 2 + 7;
        r.drawText(text, textX, textY, Color.WHITE);
    }

    @Override
    public void mouseMoved(MouseEvent e, Integer mx, Integer my, Boolean isOffWindow) {
        if (!isOffWindow && isMouseOver(mx, my)) {
            currentColor = hoverColor;
            isHovered = true;
        } else {
            currentColor = normalColor;
            isHovered = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e, Integer mx, Integer my, Boolean isOffWindow) {
        mouseMoved(e, mx, my, isOffWindow);
    }

    @Override
    public void mousePressed(MouseEvent e, Integer mx, Integer my, Boolean isOffWindow) {
        if (isHovered && e.getButton() == Input.MOUSE_L_BUTTON) {
            currentColor = clickColor;
            if (onClick != null) {
                onClick.run();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e, Integer mx, Integer my, Boolean isOffWindow) {
        if (isHovered) {
            currentColor = hoverColor;
        } else {
            currentColor = normalColor;
        }
    }

    private boolean isMouseOver(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}
```

---

## Complete Mini-Games

### Simple Shooter Game

```java
package game;

import com.neutron.engine.*;
import com.neutron.engine.base.*;
import com.neutron.engine.base.interfaces.*;
import com.neutron.engine.func.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// Main Game Class
public class ShooterGame extends BaseGame {
    public static void main(String[] args) {
        new GameCore(
            new ShooterGame(),
            "Space Shooter",
            800,
            600,
            "res/icon.png",
            GraphicsFidelity.HIGH
        );
    }

    @Override
    public void play(GameCore gameCore, Renderer r) {
        new Player(400, 500);
        new ScoreUI();
        spawnEnemies();
    }

    @Override
    public void update(GameCore gameCore, Renderer r, float delta) {
        // Spawn enemies periodically
        if (Math.random() < 0.02) {
            new Enemy((int)(Math.random() * 750) + 25, 0);
        }
    }

    private void spawnEnemies() {
        for (int i = 0; i < 5; i++) {
            new Enemy((int)(Math.random() * 750) + 25, (int)(Math.random() * 200));
        }
    }
}

// Player Class
class Player extends GameObject implements ObjectRenderer, KeyboardInput, Collidable, Transform {
    private int x, y;
    private int speed = 7;
    private float shootCooldown = 0;

    public Player(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public void play(GameCore gameCore) {
        // Initialization
    }

    @Override
    public void update(GameCore gameCore, float delta) {
        // Movement
        if (Input.isKeyDown(KeyEvent.VK_LEFT)) x -= speed;
        if (Input.isKeyDown(KeyEvent.VK_RIGHT)) x += speed;
        
        x = Math.max(25, Math.min(x, gameCore.WIDTH - 25));

        // Shooting
        if (Input.isKeyDown(KeyEvent.VK_SPACE) && shootCooldown <= 0) {
            new Bullet(x, y - 20, -10);
            shootCooldown = 10;
        }

        if (shootCooldown > 0) shootCooldown -= delta;
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        r.fillRect(-20, -10, 40, 30, Color.GREEN);
        // Gun
        r.fillRect(-5, -25, 10, 15, Color.DARK_GRAY);
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(new Collider.RectangleCollider(-20, -10, 40, 30, "player"));
    }

    @Override
    public void onEnter(GameObject other, String colliderId) {
        if (other instanceof Enemy) {
            // Game over logic
            System.out.println("Game Over!");
        }
    }

    @Override
    public void duringCollision(GameObject other, float delta) {}

    @Override
    public void onExit(GameObject other) {}

    @Override
    public void keyPressed(Input input, KeyEvent e, Integer keyCode) {}

    @Override
    public void keyReleased(Input input, KeyEvent e, Integer keyCode) {}

    @Override
    public Integer getX() { return x; }
    @Override
    public Integer getY() { return y; }
    @Override
    public Double getScale() { return 1.0; }
    @Override
    public Double getRotation() { return 0.0; }
    @Override
    public int getZDepth() { return 1; }
}

// Enemy Class
class Enemy extends GameObject implements ObjectRenderer, Movable, Collidable, Transform {
    private float x, y;
    private float vy = 2;

    public Enemy(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public void play(GameCore gameCore) {}

    @Override
    public void update(GameCore gameCore, float delta) {
        y += vy * delta;

        if (y > gameCore.HEIGHT) {
            this.delete();
        }
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        r.fillRect(-15, -15, 30, 30, Color.RED);
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(new Collider.RectangleCollider(-15, -15, 30, 30, "enemy"));
    }

    @Override
    public void onEnter(GameObject other, String colliderId) {
        if (other instanceof Bullet) {
            this.delete();
            other.delete();
            // Add score
        }
    }

    @Override
    public void duringCollision(GameObject other, float delta) {}

    @Override
    public void onExit(GameObject other) {}

    @Override
    public Integer getX() { return (int)x; }
    @Override
    public Integer getY() { return (int)y; }
    @Override
    public Float getVx() { return 0f; }
    @Override
    public Float getVy() { return vy; }
    @Override
    public Double getScale() { return 1.0; }
    @Override
    public Double getRotation() { return 0.0; }
    @Override
    public int getZDepth() { return 1; }
}

// Bullet Class
class Bullet extends GameObject implements ObjectRenderer, Movable, Collidable, Transform {
    private float x, y;
    private float vy;

    public Bullet(int x, int y, float vy) {
        super();
        this.x = x;
        this.y = y;
        this.vy = vy;
    }

    @Override
    public void play(GameCore gameCore) {}

    @Override
    public void update(GameCore gameCore, float delta) {
        y += vy * delta;

        if (y < -10 || y > gameCore.HEIGHT + 10) {
            this.delete();
        }
    }

    @Override
    public void render(GameCore gameCore, Renderer r) {
        r.fillRect(-3, -8, 6, 16, Color.YELLOW);
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(new Collider.RectangleCollider(-3, -8, 6, 16, "bullet"));
    }

    @Override
    public void onEnter(GameObject other, String colliderId) {}

    @Override
    public void duringCollision(GameObject other, float delta) {}

    @Override
    public void onExit(GameObject other) {}

    @Override
    public Integer getX() { return (int)x; }
    @Override
    public Integer getY() { return (int)y; }
    @Override
    public Float getVx() { return 0f; }
    @Override
    public Float getVy() { return vy; }
    @Override
    public Double getScale() { return 1.0; }
    @Override
    public Double getRotation() { return 0.0; }
    @Override
    public int getZDepth() { return 1; }
}

// Score UI
class ScoreUI extends GameObject implements UIObjectRenderer {
    private int score = 0;

    @Override
    public void play(GameCore gameCore) {}

    @Override
    public void update(GameCore gameCore, float delta) {}

    @Override
    public void renderUI(GameCore gameCore, Renderer r) {
        r.setFont(new Font("Arial", Font.BOLD, 24));
        r.drawText("Score: " + score, 20, 40, Color.WHITE);
    }

    public void addScore(int points) {
        score += points;
    }
}
```

---

For more information, see:
- [README.md](README.md) - Project overview
- [DOCUMENTATION.md](DOCUMENTATION.md) - Complete documentation
- [API_REFERENCE.md](API_REFERENCE.md) - API reference
- [CONTRIBUTING.md](CONTRIBUTING.md) - Contribution guidelines
