<img height="150" alt="image" src="https://github.com/user-attachments/assets/c40c3e6e-2599-4e52-a5c6-0e2c460a8eb6" />

# Neutron v2 Game Engine

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A lightweight, high-performance 2D game engine built in Java, designed for simplicity and flexibility. Neutron v2 provides a robust foundation for creating 2D games with built-in rendering, collision detection, input handling, and audio management.

## 🎯 Features

### Core Engine
- **Game Loop**: Fixed timestep update loop running at 60 FPS with delta time support
- **Scene Management**: Easy scene organization with `BaseGame` abstract class
- **Object Handler**: Efficient game object lifecycle management with unique ID system
- **Component-Based Architecture**: Interface-driven design for flexible game object behavior

### Rendering System
- **Graphics2D-based Renderer**: Hardware-accelerated 2D rendering
- **Camera System**: Smooth camera movement, zoom, and world/screen coordinate spaces
- **Custom Shaders**: Pixel-level shader support for visual effects
- **Rendering Layers**: Z-depth sorting for proper object layering
- **Graphics Quality Settings**: Configurable anti-aliasing, rendering quality, and interpolation modes

### Physics & Collision
- **Collision Detection**: Both discrete and swept AABB collision detection
- **Multiple Collider Types**: Rectangle and circle colliders
- **Collision Callbacks**: `onEnter()`, `duringCollision()`, and `onExit()` events
- **Collision Tags**: Named colliders for specific collision handling

### Input System
- **Keyboard Input**: Key press/release events with state tracking
- **Mouse Input**: Button press, movement, wheel, and drag support
- **Window Focus**: Focus gain/loss event handling
- **Input Interfaces**: Easy-to-implement input interfaces for game objects

### Audio System
- **Sound Manager**: Overlapping sound playback with volume control
- **Audio Effects**: Extensible audio effect system
- **Sound Tags**: Tag-based sound control for organized audio management
- **Master Volume**: Global volume control

### Resource Management
- **Automatic Loading**: Type detection for images and sounds
- **Resource Caching**: Efficient memory usage with resource deduplication
- **Multiple Formats**: Support for PNG, JPG, BMP, GIF (images) and WAV, MP3, OGG, FLAC, AAC (audio)

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Java Development Kit (JDK)

### Basic Game Setup

```java
package game;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.BaseGame;
import com.neutron.engine.func.GraphicsFidelity;

public class MyGame extends BaseGame {
    public static void main(String[] args) {
        new GameCore(
            new MyGame(),
            "My Game",           // Window title
            800,                 // Width
            600,                 // Height
            "res/icon.png",      // Icon path
            new GraphicsFidelity() {
                public boolean useGlobalAA() { return true; }
                public boolean useQualityRendering() { return true; }
                public boolean useBilinearSampling() { return true; }
                public boolean useSubPixelFontRendering() { return false; }
                public boolean useAAForTextOnly() { return false; }
            }
        );
    }

    @Override
    public void play(GameCore gameCore, Renderer r) {
        // Initialize your game objects here
        new Player();
        new Enemy();
    }

    @Override
    public void update(GameCore gameCore, Renderer r, float delta) {
        // Game-wide update logic here
    }
}
```

### Creating a Game Object

```java
package game;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import java.awt.Color;

public class Player extends GameObject implements ObjectRenderer {
    private int x, y;
    
    @Override
    public void play(GameCore gameCore) {
        // Initialize the player
        x = 100;
        y = 100;
    }
    
    @Override
    public void update(GameCore gameCore, float delta) {
        // Update player logic every frame
        x += 2;
    }
    
    @Override
    public void render(GameCore gameCore, Renderer r) {
        // Render the player
        r.fillRect(0, 0, 50, 50, Color.BLUE);
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

## 📚 Documentation

For detailed documentation, see:
- [**DOCUMENTATION.md**](DOCUMENTATION.md) - Complete engine architecture and systems guide
- [**EXAMPLES.md**](EXAMPLES.md) - Code examples and tutorials
- [**API_REFERENCE.md**](API_REFERENCE.md) - Detailed API reference
- [**CONTRIBUTING.md**](CONTRIBUTING.md) - Development and contribution guidelines

## 🎮 Example Game Included

The repository includes a simple Flappy Bird-style demo game that demonstrates:
- Player movement and physics
- Collision detection
- Score tracking
- Camera controls
- Audio playback

Run the demo by executing `game.Game.java`.

## 📁 Project Structure

```
Neutron-v2/
├── src/
│   ├── com/neutron/engine/          # Core engine code
│   │   ├── base/                    # Base classes and interfaces
│   │   │   ├── interfaces/          # Component interfaces
│   │   │   ├── BaseGame.java        # Base game class
│   │   │   ├── GameObject.java      # Base game object
│   │   │   └── Scene.java           # Scene management
│   │   ├── func/                    # Utility classes
│   │   │   ├── Collider.java        # Collider types
│   │   │   ├── Resource.java        # Resource wrapper
│   │   │   ├── Shader.java          # Shader interface
│   │   │   └── Vector2.java         # 2D vector math
│   │   ├── GameCore.java            # Main game loop
│   │   ├── Renderer.java            # Rendering system
│   │   ├── Window.java              # Window management
│   │   ├── Input.java               # Input handling
│   │   ├── ObjectHandler.java       # Object lifecycle management
│   │   ├── CollisionManager.java    # Collision detection
│   │   ├── SoundManager.java        # Audio system
│   │   └── ResourceManager.java     # Resource loading
│   └── game/                        # Example game
│       ├── Game.java                # Demo game
│       ├── Player.java              # Player object
│       ├── Wall.java                # Obstacle object
│       ├── HUD.java                 # UI elements
│       └── shaders/                 # Custom shaders
└── res/                             # Resources (images, sounds)
```

## 🔧 Building and Running

### Compile
```bash
javac -d out -sourcepath src src/game/Game.java
```

### Run
```bash
java -cp out game.Game
```

### Using an IDE
Import the project into your favorite IDE (IntelliJ IDEA, Eclipse, VS Code) and run `game.Game.main()`.

## 🎨 Key Concepts

### Game Objects
All game entities extend `GameObject` and implement various interfaces for specific behaviors:
- `ObjectRenderer` - For renderable objects
- `UIObjectRenderer` - For UI elements
- `Collidable` - For objects with collision
- `Movable` - For objects with velocity
- `KeyboardInput` - For keyboard controls
- `MouseButtonInput` - For mouse clicks
- `MouseWheelInput` - For scroll wheel
- And more...

### Coordinate Systems
- **World Coordinates**: Affected by camera position and zoom
- **Screen Coordinates**: Fixed to screen, used for UI elements

### Rendering Pipeline
1. Clear the screen
2. Render world objects (sorted by Z-depth)
3. Render UI objects (screen coordinates)
4. Show buffer

## 🤝 Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Built with Java AWT/Swing for graphics
- Uses Java Sound API for audio
- Inspired by modern game engine architectures

## 📬 Contact

For questions, issues, or suggestions, please open an issue on GitHub.

---

**Crafted with passion and with ❤️, from Xander van Pelt*
