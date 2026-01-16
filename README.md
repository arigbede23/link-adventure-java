# Link Adventure (Java + JavaScript)

A Link-inspired 2D game built twice: once in **Java** and once in **JavaScript**. This project focuses on core game-engine fundamentals like movement, collisions, sprite interactions, and simple combat mechanics.

## Features
- Player movement + physics
- Collision handling with solid objects (ex: trees/terrain)
- Enemy logic (ex: birds) + interaction rules
- Projectile/item logic (ex: boomerangs that disappear after a hit)
- Animated sprites (frame cycling)

## How to run

### JavaScript version (browser)
1. Open the `js/` folder.
2. Run the game:
   - Easiest: open `js/game.html` in your browser
   - Recommended: use a local server (VS Code “Live Server”) so assets load reliably

### Java version
1. Open the `java/` folder.
2. Compile + run (example):
   ```bash
   cd java
   javac *.java
   java Main
