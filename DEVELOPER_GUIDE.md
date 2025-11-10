# Developer Quick Reference Guide

## 🚀 Quick Start

```bash
# Clone and run
git clone https://github.com/SanoKhan22/JavaGrid4.git
cd JavaGrid4
mvn clean compile
mvn exec:java
```

## 📁 Project Structure

```
JavaGrid4/
├── src/main/java/com/mycompany/javagrid4/
│   ├── JavaGrid4.java                 # Entry point, CardLayout navigation
│   ├── GameEngine.java                # Pure business logic (NO Swing)
│   ├── GameState.java                 # Game state management
│   ├── Player.java                    # Player enum
│   │
│   ├── models/
│   │   ├── PlayerConfig.java          # Player configuration data
│   │   └── GameConfig.java            # Game configuration data
│   │
│   ├── ui/
│   │   ├── screens/
│   │   │   ├── MenuPanel.java         # Player setup screen
│   │   │   ├── GamePanel.java         # Main game screen
│   │   │   └── ResultsPanel.java      # Game over results
│   │   │
│   │   ├── components/
│   │   │   ├── CustomGridCell.java    # Material Design grid cell with lock icon
│   │   │   ├── ScoreCard.java         # Score card with progress bars
│   │   │   ├── ControlCard.java       # Material Design control buttons
│   │   │   ├── TurnIndicator.java     # Animated turn indicator
│   │   │   ├── PauseOverlay.java      # Pause modal overlay
│   │   │   ├── GridContainer.java     # Grid wrapper card
│   │   │   ├── ColorPickerButton.java # Color selection button
│   │   │   ├── BoardSizeCard.java     # Board size selector
│   │   │   └── GameTimer.java         # Timer component
│   │   │
│   │   ├── effects/
│   │   │   ├── CellClaimAnimation.java      # Cell claim animations
│   │   │   ├── ScoreIncrementAnimation.java # Score flying animation
│   │   │   └── GameOverOverlay.java         # Game over overlay
│   │   │
│   │   └── dialogs/
│   │       └── HelpDialog.java        # Help/rules dialog
│   │
│   ├── commands/
│   │   ├── GameCommand.java           # Command interface
│   │   ├── MoveCommand.java           # Move command (undo/redo)
│   │   └── CommandHistory.java        # Command history manager
│   │
│   └── audio/
│       └── SoundManager.java          # Sound effects system
│
├── docs/                              # Documentation
│   ├── MATERIAL_DESIGN_BOTTOM_PANEL.md
│   ├── BUG_FIX_MAX_CELL_CLICK.md
│   ├── GRID_DESIGN_OPTIONS.md
│   └── 2D_3D_EFFECTS_GUIDE.md
│
├── README.md                          # Main documentation
├── CHANGELOG.md                       # Version history
├── pom.xml                            # Maven configuration
└── .gitignore                         # Git ignore rules
```

## 🎨 Material Design System

### Elevation Levels
```java
// Grid Cells
ELEVATION_PRESSED = 2   // Flattened
ELEVATION_NORMAL = 6    // Default
ELEVATION_HOVER = 10    // Lifted

// Components
ELEVATION_CARD = 8      // Score cards, active state
ELEVATION_CONTROL = 2   // Control cards normal
ELEVATION_CONTROL_HOVER = 4  // Control cards hover
ELEVATION_PAUSE = 12    // Pause overlay modal
```

### Component Dimensions
```java
// Control Cards
CONTROL_CARD_SIZE = 90×90px
CORNER_RADIUS = 12px

// Turn Indicator
TURN_INDICATOR = 280×70px
PULSE_SCALE = 1.0 → 1.02 → 1.0

// Pause Overlay
PAUSE_MODAL = 400×280px
CORNER_RADIUS = 16px

// Grid Container
CORNER_RADIUS = 12px
ELEVATION = 8dp
```

### Color Constants
```java
NEUTRAL_COLOR = new Color(250, 250, 255)   // Light cell background
HOVER_OVERLAY = new Color(0, 0, 0, 25)     // Hover state
PRESSED_OVERLAY = new Color(0, 0, 0, 50)   // Pressed state
BORDER_COLOR = new Color(180, 185, 200)    // Border color
```

### Shadow Rendering Pattern
```java
// 5-layer blur for depth
for (int i = 5; i >= 1; i--) {
    int alpha = Math.min((elevation * 15) / (i + 1), 90);
    int offset = elevation + (i * 2) - 2;
    g2d.setColor(new Color(0, 0, 0, alpha));
    g2d.fill(new RoundRectangle2D.Float(
        offset, 
        offset + (i / 2),  // Downward offset
        width, height, 
        CORNER_RADIUS + (i / 2), 
        CORNER_RADIUS + (i / 2)
    ));
}
```

## 🔧 Key Components

### CustomGridCell
**Purpose:** Material Design grid cell with elevation and shadows  
**Location:** `ui/components/CustomGridCell.java`  
**Key Methods:**
- `paintComponent()` - Main rendering pipeline
- `drawShadow()` - Multi-layer shadow rendering
- `drawBackground()` - Solid background with overlays
- `drawHoverGlow()` - Colored ring on hover
- `playClaimAnimation()` - Trigger cell claim animation

### ScoreCard
**Purpose:** Elevated card for player scores and current turn  
**Location:** `ui/components/ScoreCard.java`  
**Key Methods:**
- `setValue()` - Update displayed value
- `setAccentColor()` - Change card accent color
- `setActive()` - Toggle active/elevated state

### GameEngine
**Purpose:** Pure business logic (zero Swing dependencies)  
**Location:** `GameEngine.java`  
**Key Methods:**
- `applyMove(row, col, player)` - Process move, return points
- `getCellValue(row, col)` - Get cell value (0-4)
- `isGameOver()` - Check if all cells maxed

## 🎮 Game Flow

```
1. JavaGrid4.main()
   └─> Creates JFrame with CardLayout
   
2. MenuPanel (MENU screen)
   ├─> Player name validation
   ├─> Color selection (8 presets + custom)
   ├─> Board size selection (3×3, 5×5, 7×7)
   └─> Fires "gameStarted" → CardLayout shows GAME
   
3. GamePanel (GAME screen)
   ├─> Creates GameEngine with board size
   ├─> Builds CustomGridCell grid
   ├─> Creates ScoreCard components
   ├─> Handles cell clicks:
   │   ├─> Validate not maxed (value < 4)
   │   ├─> Create MoveCommand
   │   ├─> Execute command (increments cell + neighbors)
   │   ├─> Play claim animation if cell reaches 4
   │   ├─> Switch player turn
   │   └─> Check game over
   └─> Fires "gameEnded" → CardLayout shows RESULTS
   
4. ResultsPanel (RESULTS screen)
   ├─> Shows winner, scores, elapsed time
   ├─> Replay button → Back to MENU
   └─> Menu button → Back to MENU
```

## 🐛 Common Issues & Solutions

### Issue: Cells not showing shadows
**Solution:** Check antialiasing is enabled:
```java
g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                     RenderingHints.VALUE_ANTIALIAS_ON);
```

### Issue: Max value cells still clickable
**Solution:** Add early validation in `handleCellClick()`:
```java
if (gameEngine.getCellValue(row, col) >= 4) {
    SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
    return;
}
```

### Issue: Turn changes even on invalid move
**Solution:** Validation must happen BEFORE `switchPlayer()` call

### Issue: Score cards not updating
**Solution:** Ensure `updateDisplay()` calls card update methods:
```java
player1Card.setValue("Score: " + player1Score);
currentTurnCard.setValue(currentPlayerName);
currentTurnCard.setAccentColor(currentPlayerColor);
```

## 🔑 Keyboard Shortcuts

| Key | Action |
|-----|--------|
| `ESC` | Return to menu (with confirmation) |
| `R` | Restart game (with confirmation) |
| `P` | Pause/Resume game |
| `Ctrl+Z` | Undo last move |
| `Ctrl+Y` | Redo last undone move |
| `F1` | Open help dialog |

## 🎯 Design Patterns Used

### MVC (Model-View-Controller)
- **Model:** `GameEngine`, `GameState`, `PlayerConfig`
- **View:** `CustomGridCell`, `ScoreCard`, `GamePanel`
- **Controller:** `GamePanel` event handlers

### Observer Pattern
- `PropertyChangeListener` for screen transitions
- `PropertyChangeSupport` in panels

### Command Pattern
- `MoveCommand` for undo/redo
- `CommandHistory` for command stack

### Strategy Pattern
- `CellClaimAnimation.AnimationType` enum
- Different animation strategies (Pop, Pulse, Burst, Combo)

## 📝 Code Style Guidelines

### Naming Conventions
- Classes: `PascalCase` (e.g., `CustomGridCell`)
- Methods: `camelCase` (e.g., `drawShadow()`)
- Constants: `UPPER_SNAKE_CASE` (e.g., `ELEVATION_NORMAL`)
- Variables: `camelCase` (e.g., `isHovered`)

### JavaDoc Requirements
```java
/**
 * Brief description of class/method.
 * Additional details if needed.
 * 
 * @param paramName Description
 * @return Description of return value
 */
```

### Import Organization
1. Java standard library (`java.*`)
2. Javax libraries (`javax.*`)
3. Third-party libraries
4. Project packages (`com.mycompany.javagrid4.*`)

## 🧪 Testing Checklist

- [ ] All three board sizes work (3×3, 5×5, 7×7)
- [ ] Player name validation (1-10 characters)
- [ ] Color picker prevents duplicates
- [ ] Cells increment correctly (value + neighbors)
- [ ] Cell claims when reaching 4
- [ ] Scores update correctly
- [ ] Turn switches properly
- [ ] Max value cells reject clicks
- [ ] Undo/Redo works correctly
- [ ] Timer starts on first move
- [ ] Pause/Resume works
- [ ] Help dialog opens with F1
- [ ] Sound toggle works
- [ ] Game over triggers correctly
- [ ] Results screen shows correct data
- [ ] Replay/Menu buttons work

## 📞 Support

- **GitHub Issues:** https://github.com/SanoKhan22/JavaGrid4/issues
- **Documentation:** See `/docs` folder
- **Email:** Contact repository owner

---

**Last Updated:** November 10, 2025  
**Version:** 4.0-MATERIAL-DESIGN
