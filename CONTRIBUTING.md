# Contributing to Neutron v2

Thank you for your interest in contributing to Neutron v2! This document provides guidelines and information for contributors.

## Table of Contents

1. [Code of Conduct](#code-of-conduct)
2. [Getting Started](#getting-started)
3. [Development Setup](#development-setup)
4. [Project Structure](#project-structure)
5. [Coding Standards](#coding-standards)
6. [Making Changes](#making-changes)
7. [Testing](#testing)
8. [Submitting Changes](#submitting-changes)
9. [Feature Requests](#feature-requests)
10. [Bug Reports](#bug-reports)

---

## Code of Conduct

### Our Pledge

We are committed to providing a welcoming and inclusive environment for everyone. Please be respectful, considerate, and professional in all interactions.

### Expected Behavior

- Be respectful of differing viewpoints and experiences
- Accept constructive criticism gracefully
- Focus on what is best for the community and the project
- Show empathy towards other community members

### Unacceptable Behavior

- Harassment, trolling, or inflammatory comments
- Personal attacks or insults
- Publishing others' private information
- Any conduct that would be considered inappropriate in a professional setting

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Git
- A Java IDE (IntelliJ IDEA, Eclipse, VS Code, or similar)
- Basic understanding of Java and game development concepts

### Fork and Clone

1. Fork the repository on GitHub
2. Clone your fork locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/Neutron-v2.git
   cd Neutron-v2
   ```
3. Add the upstream repository:
   ```bash
   git remote add upstream https://github.com/XenenDev/Neutron-v2.git
   ```

### Stay Updated

Keep your fork synchronized with the upstream repository:
```bash
git fetch upstream
git checkout main
git merge upstream/main
```

---

## Development Setup

### IDE Setup

#### IntelliJ IDEA
1. Open the project folder
2. IntelliJ should automatically detect it as a Java project
3. Set the JDK to version 17 or higher
4. Mark `src` as the source root
5. Run `game.Game.main()` to test

#### Eclipse
1. Import as an existing Java project
2. Set the JDK to version 17 or higher
3. Configure build path to include `src`
4. Run `game.Game.main()` to test

#### VS Code
1. Open the project folder
2. Install the Java Extension Pack
3. Configure Java runtime to version 17 or higher
4. Run `game.Game.main()` to test

### Building

#### Command Line
```bash
# Compile
javac -d out -sourcepath src src/game/Game.java

# Run
java -cp out game.Game
```

---

## Project Structure

```
Neutron-v2/
├── src/
│   ├── com/neutron/engine/          # Core engine
│   │   ├── base/                    # Base classes
│   │   │   ├── interfaces/          # Component interfaces
│   │   │   ├── BaseGame.java
│   │   │   ├── GameObject.java
│   │   │   └── Scene.java
│   │   ├── func/                    # Utility classes
│   │   ├── GameCore.java            # Main loop
│   │   ├── Renderer.java            # Graphics
│   │   ├── Window.java
│   │   ├── Input.java
│   │   ├── ObjectHandler.java
│   │   ├── CollisionManager.java
│   │   ├── SoundManager.java
│   │   └── ResourceManager.java
│   └── game/                        # Example game
├── res/                             # Resources
├── DOCUMENTATION.md
├── EXAMPLES.md
├── API_REFERENCE.md
├── CONTRIBUTING.md
└── README.md
```

### Key Directories

- **`src/com/neutron/engine/`** - Core engine code
- **`src/com/neutron/engine/base/`** - Base classes and interfaces
- **`src/com/neutron/engine/func/`** - Utility classes
- **`src/game/`** - Example game (for testing)
- **`res/`** - Resource files (images, sounds)

---

## Coding Standards

### Java Style Guidelines

#### Naming Conventions

```java
// Classes: PascalCase
public class GameCore { }
public class ObjectHandler { }

// Methods: camelCase
public void updateObjects() { }
public int getScore() { }

// Variables: camelCase
private int playerHealth;
private float velocityX;

// Constants: UPPER_SNAKE_CASE
public static final int MAX_PLAYERS = 4;
public static final float GRAVITY = 9.8f;

// Interfaces: PascalCase (descriptive)
public interface ObjectRenderer { }
public interface Collidable { }
```

#### Code Formatting

```java
// Braces on same line
public void myMethod() {
    if (condition) {
        doSomething();
    } else {
        doSomethingElse();
    }
}

// Indentation: 4 spaces (no tabs)
public class MyClass {
    private int value;
    
    public void method() {
        if (true) {
            System.out.println("Hello");
        }
    }
}

// Line length: Prefer 100 characters max
// Break long lines at logical points
public void longMethod(int param1, int param2, 
                      int param3, int param4) {
    // method body
}
```

#### Comments

```java
/**
 * JavaDoc for public APIs
 * 
 * @param gameCore The game core instance
 * @param delta Delta time multiplier
 */
public void update(GameCore gameCore, float delta) {
    // Single-line comments for explanations
    health -= damage;
    
    /* Multi-line comments for larger
       explanations or temporary code
       removal during development */
}
```

### Best Practices

#### Performance
- Avoid creating objects in update/render loops
- Cache frequently accessed values
- Use primitive types when possible
- Minimize garbage collection pressure

```java
// ✓ Good
private static final Resource SPRITE = new Resource("sprite.png");

public void render(GameCore gc, Renderer r) {
    r.drawImage((Image) SPRITE.get(), x, y);
}

// ✗ Bad
public void render(GameCore gc, Renderer r) {
    Resource sprite = new Resource("sprite.png"); // Creates new object every frame!
    r.drawImage((Image) sprite.get(), x, y);
}
```

#### Null Safety
```java
// Check for null before use
if (gameObject != null) {
    gameObject.update(gc, delta);
}

// Use Objects.requireNonNull for required parameters
public void setPlayer(Player player) {
    this.player = Objects.requireNonNull(player, "Player cannot be null");
}
```

#### Error Handling
```java
// Provide meaningful error messages
if (health < 0) {
    throw new IllegalStateException("Health cannot be negative: " + health);
}

// Log errors appropriately
try {
    loadResource(path);
} catch (IOException e) {
    System.err.println("Failed to load resource: " + path);
    e.printStackTrace();
}
```

---

## Making Changes

### Branch Strategy

Create a feature branch for your changes:
```bash
git checkout -b feature/my-new-feature
# or
git checkout -b bugfix/fix-collision-bug
```

Branch naming conventions:
- `feature/description` - New features
- `bugfix/description` - Bug fixes
- `docs/description` - Documentation changes
- `refactor/description` - Code refactoring
- `perf/description` - Performance improvements

### Commit Messages

Write clear, descriptive commit messages:

```
[type] Brief summary (50 characters or less)

More detailed explanation if needed. Wrap at 72 characters.
Explain what and why, not how.

- Bullet points are okay
- Use present tense ("Add feature" not "Added feature")
- Reference issues: Fixes #123
```

**Types:**
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `refactor:` Code refactoring
- `perf:` Performance improvement
- `test:` Adding tests
- `chore:` Maintenance tasks

**Examples:**
```
feat: Add swept AABB collision detection

Implements swept AABB for fast-moving objects to prevent tunneling.
Objects implementing Movable now use continuous collision detection.

Fixes #42
```

```
fix: Correct camera zoom calculation

Camera zoom was inverting incorrectly when set to values below 1.
Fixed the scaling math in setCameraZoom().

Fixes #56
```

### Code Review Checklist

Before submitting, verify:

- [ ] Code follows the style guidelines
- [ ] All methods have appropriate JavaDoc comments
- [ ] No compiler warnings
- [ ] Changes are tested with the example game
- [ ] No unnecessary files are included (check .gitignore)
- [ ] Commit messages are clear and descriptive
- [ ] Code is efficient (no performance regressions)
- [ ] Documentation is updated if needed

---

## Testing

### Manual Testing

1. Run the example game:
   ```bash
   java -cp out game.Game
   ```

2. Test your changes:
   - Create test scenarios in the example game
   - Verify expected behavior
   - Test edge cases
   - Check for exceptions/errors

3. Performance testing:
   - Monitor FPS with `gameCore.getFPS()`
   - Check for memory leaks (long running sessions)
   - Profile if adding computationally intensive features

### Example Test Scenarios

Create temporary test objects in `game.Game.play()`:

```java
@Override
public void play(GameCore gameCore, Renderer r) {
    // Test your new feature
    new MyNewObject(100, 100);
    
    // Existing game objects
    new Player();
    // ...
}
```

### Regression Testing

Ensure existing functionality still works:
- Player movement
- Collision detection
- Sound playback
- Camera controls
- UI rendering

---

## Submitting Changes

### Pull Request Process

1. **Ensure your branch is up to date:**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Push your changes:**
   ```bash
   git push origin feature/my-new-feature
   ```

3. **Create Pull Request on GitHub:**
   - Go to your fork on GitHub
   - Click "New Pull Request"
   - Select your feature branch
   - Fill out the PR template

### Pull Request Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Performance improvement
- [ ] Refactoring

## Testing
How was this tested?

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-reviewed the code
- [ ] Commented complex sections
- [ ] Updated documentation
- [ ] No compiler warnings
- [ ] Tested with example game
```

### Review Process

1. Maintainers will review your PR
2. Address any requested changes
3. Once approved, it will be merged
4. Your contribution will be acknowledged!

---

## Feature Requests

### Proposing New Features

Before implementing a major feature:

1. **Check existing issues** - Maybe it's already planned
2. **Open a discussion** - Create an issue with the `enhancement` label
3. **Describe the feature:**
   - What problem does it solve?
   - How would it work?
   - What are the alternatives?
   - Any implementation ideas?

4. **Wait for feedback** - Maintainers will discuss feasibility

### Feature Request Template

```markdown
## Feature Description
Clear description of the proposed feature

## Problem It Solves
What problem or limitation does this address?

## Proposed Solution
How should this feature work?

## Alternatives Considered
What other approaches did you consider?

## Additional Context
Any other relevant information
```

---

## Bug Reports

### Reporting Bugs

Help us fix bugs by providing detailed information:

1. **Search existing issues** - Check if already reported
2. **Create a new issue** with the `bug` label
3. **Provide details** using the template below

### Bug Report Template

```markdown
## Bug Description
Clear description of the bug

## Steps to Reproduce
1. Do this
2. Then do this
3. Bug occurs

## Expected Behavior
What should happen?

## Actual Behavior
What actually happens?

## Environment
- OS: [e.g., Windows 10, macOS 12, Ubuntu 20.04]
- Java Version: [e.g., JDK 17]
- Engine Version/Commit: [e.g., commit abc123]

## Additional Context
- Screenshots
- Error messages
- Stack traces
- Code snippets
```

### Example Bug Report

```markdown
## Bug Description
Player falls through floor when jumping at high speeds

## Steps to Reproduce
1. Set player velocity to 50
2. Jump while moving
3. Player clips through floor collider

## Expected Behavior
Player should collide with floor and stop

## Actual Behavior
Player passes through floor collider

## Environment
- OS: Windows 10
- Java Version: JDK 17.0.2
- Commit: abc1234

## Additional Context
Seems related to collision detection not handling high velocities.
Swept AABB might help.
```

---

## Areas for Contribution

### Priority Areas

1. **Performance Optimization**
   - Collision detection improvements
   - Rendering optimizations
   - Memory management

2. **New Features**
   - Particle system
   - Lighting system improvements
   - Animation system
   - Tilemap support
   - Scene management

3. **Documentation**
   - More code examples
   - Video tutorials
   - API clarifications
   - Wiki pages

4. **Tools**
   - Level editor
   - Asset pipeline
   - Debugging utilities

5. **Testing**
   - Unit tests
   - Integration tests
   - Performance benchmarks

### Good First Issues

Look for issues labeled `good first issue` - these are great for newcomers!

---

## Questions?

If you have questions:

1. Check the [Documentation](DOCUMENTATION.md)
2. Look at [Examples](EXAMPLES.md)
3. Search existing issues
4. Ask in discussions
5. Open a new issue with the `question` label

---

## Recognition

Contributors are recognized in:
- GitHub contributors page
- Release notes for significant contributions
- Special thanks in documentation

Thank you for contributing to Neutron v2! 🚀

---

## License

By contributing, you agree that your contributions will be licensed under the same license as the project (MIT License).
