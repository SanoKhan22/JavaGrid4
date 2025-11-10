# 🎮 JavaGrid4 - Strategic Grid Game

> A sophisticated two-player strategy game showcasing advanced Java Swing development, Material Design UI, and clean architecture patterns.

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=java)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Build-Maven-red?style=flat-square)
![Design](https://img.shields.io/badge/UI-Material%20Design-purple?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

---

## 📌 Overview

**JavaGrid4** is a turn-based strategy game where two players compete to claim cells on a dynamic grid. The game demonstrates professional-level Java development with modern Material Design UI/UX, custom component rendering with advanced shadow effects, and robust architecture patterns.

### 🎯 Why This Project Stands Out

- ✅ **Clean Architecture** - Strict separation of business logic and UI (MVC pattern)
- ✅ **Material Design UI** - Elevated cards, multi-layer shadows, and depth perception
- ✅ **Advanced Swing Components** - Custom-rendered components with Graphics2D and 2D shadows
- ✅ **Multi-Screen Navigation** - Professional CardLayout implementation
- ✅ **Real-Time Validation** - Live user feedback with visual indicators
- ✅ **Design Patterns** - Observer, Strategy, Command, Factory patterns throughout
- ✅ **Production-Ready Code** - Comprehensive error handling and validation
- ✅ **Smooth Animations** - Cell claim animations, score increments, and visual effects

---

## 🎲 Game Rules

### Objective
**Claim the most cells** by strategically placing moves on the grid to reach the target value of 4.

### How to Play

1. **Choose Your Setup**
   - Select player names (up to 10 characters)
   - Pick unique colors for each player
   - Choose board size: **3×3** (Quick), **5×5** (Classic), or **7×7** (Expert)

2. **Gameplay Mechanics**
   - Players take turns clicking on any cell
   - When you click a cell, it **increments by 1** (0 → 1 → 2 → 3 → 4)
   - The **4 orthogonal neighbors** also increment by 1
   - When a cell reaches **value 4**, the current player **claims** it and earns **1 point**
   - ⚠️ **Cells at maximum value (4) cannot be clicked** - they are locked

3. **Winning Condition**
   - Game ends when **all cells reach value 4**
   - Player with the **most claimed cells wins**
   - Ties are possible!

### Strategy Tips 💡
- Plan ahead! Your move affects 5 cells (clicked + 4 neighbors)
- Corner cells have fewer neighbors = easier to control
- Watch your opponent's potential moves
- Sometimes letting your opponent go first is advantageous

---

## ✨ Key Features

### 🎨 Professional UI/UX - Material Design

- **Material Design Grid Cells** 
  - 2D elevated buttons with multi-layer shadows (6dp-10dp)
  - 5-layer blur effect for realistic depth perception
  - Corner radius: 10px with smooth antialiasing
  - Hover state: 10dp elevation with colored ring
  - Pressed state: 2dp elevation (flattened)
  - Solid colors with subtle overlays for states
  - Visual feedback: cells flash on invalid clicks
  - **Lock icon overlay**: Red lock icon on cells at maximum value (4)

- **Material Design Control Cards** (90×90px)
  - Icon-based elevated cards for all game controls
  - Controls: Restart (↻), Undo (⟲), Redo (⟳), Menu (≡), Pause (⏸)
  - Color-coded actions: Red (Restart), Gray (Undo/Redo), Blue (Menu), Purple (Pause)
  - Hover/press elevation changes (2dp → 4dp → 2dp)
  - Professional icon typography

- **Animated Turn Indicator** (280×70px)
  - Replaces static current turn display
  - Smooth pulsing animation (scale: 1.0 → 1.02 → 1.0)
  - Dynamic color updates matching current player
  - Elevated card with 8dp shadow
  - 1-second pulse cycle for attention

- **Material Design Score Cards**
  - Three elevated cards (Player 1, Current Turn, Player 2)
  - Active card: 8dp elevation with accent top bar
  - Card dimensions: 200×80px with 12px rounded corners
  - Clean typography with title/value hierarchy
  - Dynamic color updates matching player colors
  - Subtle background tint for active card
  - **Animated progress bars**: Show score advancement with smooth fills

- **Enhanced Pause System**
  - Modal overlay (400×280px) with 16dp elevation
  - Fade-in/fade-out animations (300ms)
  - Semi-transparent backdrop (alpha: 120)
  - Large "PAUSED" text (48pt bold)
  - Clear resume instructions

- **Grid Container Wrapper**
  - Game grid wrapped in elevated Material Design card
  - 8dp elevation with multi-layer shadow
  - 12px rounded corners for consistency
  - Adds visual hierarchy to game board

- **Enhanced Background**
  - Diagonal gradient: Blue-gray to purple-gray
  - Subtle dot pattern texture overlay (8×8px grid)
  - Non-intrusive design (alpha: 15)
  - Professional polished appearance

- **Enhanced Text Fields** (220×50px)
  - Focus glow effects (blue border on click)
  - Live character counter with validation icons (✓ ⚠️ ❌)
  - Real-time background color feedback (white/yellow/red)
  
- **Visual Board Size Selector**
  - Interactive cards (140×180px) with mini grid previews
  - Hover effects and smooth transitions
  - Checkmark indicators for selected size

- **Advanced Color Picker**
  - 70×70px gradient-rendered circular buttons
  - 8 preset colors + custom color dialog
  - Duplicate color prevention with warning

- **Game Timer** ⏱️
  - Real-time elapsed time display (MM:SS format)
  - Color coding: Green (<1 min), Gray (1-5 min), Orange (5+ min)
  - Pause/resume support integrated with game state
  - Final time shown in results screen

- **Cell Claim Animations**
  - Pop, Pulse, Burst, and Combo animation types
  - Smooth particle effects on cell claims
  - Score increment animations flying to score cards
  - Glow effects for newly claimed cells

- **Help System** 📖
  - In-game help dialog with 5 comprehensive tabs
  - F1 keyboard shortcut for quick access
  - Custom icon integration for professional look
  - ESC key closes dialog

- **Icon-Based Controls**
  - Custom PNG icons for Undo/Redo buttons
  - Professional sound control with mute/unmute icons
  - Help button with custom icon
  - Compact, space-efficient design

### 🏗️ Architecture Highlights

```
📁 Project Structure
├── models/           # Data models (PlayerConfig, GameConfig)
├── ui/
│   ├── components/   # Reusable components (CustomGridCell, ScoreCard, 
│   │                 # ControlCard, TurnIndicator, PauseOverlay, GridContainer)
│   ├── effects/      # Animations (CellClaimAnimation, ScoreIncrementAnimation)
│   └── screens/      # Screen panels (MenuPanel, GamePanel)
├── commands/         # Command pattern for undo/redo
├── GameEngine.java   # Pure business logic (NO Swing imports)
├── GameState.java    # State management
└── JavaGrid4.java    # Entry point with CardLayout
```

**Key Architectural Decisions:**
- ✅ **Zero Swing imports** in business logic classes
- ✅ **Command Pattern** for undo/redo functionality
- ✅ **PropertyChangeListener** for screen-to-screen communication
- ✅ **Custom paintComponent** for Material Design rendering
- ✅ **Multi-layer shadow rendering** for depth perception
- ✅ **Immutable configurations** passed between screens

---

## 🚀 Technologies & Skills Demonstrated

### Core Technologies
- **Java 17+** - Modern Java features and best practices
- **Swing GUI** - Advanced components and custom rendering with Material Design
- **Maven** - Dependency management and build automation
- **Graphics2D** - Multi-layer shadows, blur effects, 2D depth rendering

### Design Patterns
- **MVC (Model-View-Controller)** - Separation of concerns
- **Observer Pattern** - PropertyChangeListener for event handling
- **Command Pattern** - Undo/Redo functionality with history
- **Strategy Pattern** - Flexible animation implementations
- **Factory Pattern** - Component creation

### Advanced Rendering Techniques
- **Multi-Layer Shadows** - 5-layer blur for realistic depth
- **Material Design Elevation** - 2D shadow system (1dp-10dp)
- **Custom Paint Components** - Full control over rendering pipeline
- **RoundRectangle2D** - Smooth rounded corners with antialiasing
- **Color Overlays** - State-based visual feedback
- **Particle Systems** - Burst animations for cell claims

### UI/UX Techniques
- **CardLayout** - Multi-screen navigation
- **Custom Components** - Extended JPanel/JComponent with custom rendering
- **Focus Management** - Visual feedback for user interactions
- **Real-Time Validation** - Immediate user feedback with error sounds
- **Responsive Design** - Proper layout managers (GridBagLayout, FlowLayout)
- **Animation Timers** - Smooth transitions and effects

---

## 🛠️ Setup & Running

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **NetBeans IDE** (recommended) or any Java IDE

### Quick Start

```bash
# Clone the repository
git clone https://github.com/SanoKhan22/JavaGrid4.git

# Navigate to project directory
cd JavaGrid4

# Build the project
mvn clean install

# Run the game
mvn exec:java
```

### Alternative (NetBeans)
1. Open NetBeans IDE
2. File → Open Project → Select `JavaGrid4`
3. Right-click project → Clean and Build
4. Right-click project → Run

---

## 📸 Screenshots

### Menu Screen
*Professional player setup with enhanced validation, visual feedback, and Material Design elements*

### Game Board - Material Design
*Elevated grid cells with 2D depth shadows, Material Design score cards, and smooth animations*

### Results Screen
*Clear winner announcement with elapsed time and replay options*

---

## 🎯 Code Quality Highlights

### Clean Code Practices
```java
// Example: Pure business logic with zero UI dependencies
public class GameEngine {
    // NO Swing imports!
    public int applyMove(int row, int col, Player player) {
        // Increment clicked cell + 4 neighbors
        // Award points when cells reach 4
        // Maintain single responsibility
        // Return points awarded for validation
    }
}
```

### Material Design Rendering Example
```java
// Example: Multi-layer shadow rendering for depth
private void drawShadow(Graphics2D g2d, int width, int height) {
    int elevation = isPressed ? 2 : (isHovered ? 10 : 6);
    
    // 5-layer blur for smooth depth perception
    for (int i = 5; i >= 1; i--) {
        int alpha = Math.min((elevation * 15) / (i + 1), 90);
        int offset = elevation + (i * 2) - 2;
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fill(new RoundRectangle2D.Float(...));
    }
}
```

### Command Pattern for Undo/Redo
```java
// Example: Reversible game actions
public class MoveCommand implements GameCommand {
    @Override
    public void execute() {
        captureState(); // Save before
        pointsAwarded = gameEngine.applyMove(row, col, player);
    }
    
    @Override
    public void undo() {
        // Restore all affected cells and scores
    }
}
```

---

## 🌟 What Recruiters Will Notice

1. **Architecture Excellence** 
   - Clean separation between GUI and logic
   - Testable business logic (zero UI dependencies)
   - Proper use of design patterns (MVC, Command, Observer, Strategy)
   - CommandHistory for reversible operations

2. **Advanced UI/Rendering Skills**
   - Material Design implementation from scratch
   - Multi-layer shadow rendering for depth perception
   - Custom component development with Graphics2D
   - Smooth animations and particle effects
   - Professional elevation system (1dp-10dp)

3. **Code Quality**
   - Comprehensive JavaDoc comments
   - Consistent naming conventions
   - Error handling and input validation
   - Bug fixes with proper testing (e.g., max cell click prevention)

4. **Modern Development Practices**
   - Maven project structure
   - Git version control with clear commits
   - Modular, maintainable codebase
   - Documentation-driven development

5. **Attention to Detail**
   - Pixel-perfect spacing and alignment
   - Smooth user interactions with visual feedback
   - Error sounds and visual flash for invalid actions
   - Responsive hover/pressed states
   - Dynamic color coordination

6. **Problem-Solving Skills**
   - Fixed gameplay bug (max value cells still clickable)
   - Implemented smooth Material Design conversion
   - Created custom score card component system
   - Optimized shadow rendering performance

---

## 🔮 Future Enhancements

### ✅ Recently Completed
- [x] Material Design UI with elevated components
- [x] Multi-layer shadow rendering (5 layers)
- [x] Material Design score cards with elevation
- [x] Cell claim animations (Pop, Pulse, Burst, Combo)
- [x] Score increment flying animations
- [x] Bug fix: Prevent clicking max value cells
- [x] Undo/Redo functionality with Command pattern
- [x] Sound effects system with mute control
- [x] Game timer with pause/resume
- [x] Help/Rules dialog with F1 shortcut
- [x] Game timer display with pause/resume
- [x] Custom icon integration (undo, redo, help, sound)
- [x] Game over overlay with zoom animation
- [x] Score animations and confetti effects
- [x] Professional menu layout with utility controls

### 🚀 Planned Features
- [ ] AI opponent with difficulty levels (Easy, Medium, Hard)
- [ ] Theme system (Dark mode, Neon, High contrast)
- [ ] Game statistics tracking and persistence (save/load)
- [ ] Move history display panel with timeline
- [ ] Achievements system with unlockable badges
- [ ] Network multiplayer support (LAN/Online)
- [ ] Replay system to review past games
- [ ] Tournament mode with brackets

---

## 🐛 Bug Fixes & Improvements

### Fixed Issues
1. **Max Value Cell Click Bug** (v4.0)
   - Problem: Cells at value 4 were still clickable, wasting player turns
   - Solution: Added early validation to reject clicks on maxed cells
   - Result: Error sound + visual flash, turn preserved

2. **Glassmorphism to Material Design** (v4.0)
   - Problem: Transparent glassmorphism theme lacked clarity
   - Solution: Complete conversion to solid Material Design
   - Result: Clear depth perception with multi-layer shadows

3. **Bottom Panel Redesign** (v4.0)
   - Problem: Plain text labels didn't match grid theme
   - Solution: Created Material Design score cards with elevation
   - Result: Cohesive design with active card indicator

---

## 👨‍💻 About the Developer

This project demonstrates my expertise in:
- **Java Development** - Clean, maintainable, object-oriented code
- **UI/UX Design** - Material Design implementation from scratch
- **Graphics Programming** - Multi-layer rendering and depth effects
- **Software Architecture** - Scalable, testable design patterns
- **Problem Solving** - Bug fixes, refactoring, and optimization
- **Documentation** - Clear, comprehensive technical writing

**Perfect for roles in:**
- Java Software Engineer
- Full-Stack Developer
- UI/UX Engineer
- Software Architect
- Game Developer

---

## 📄 License

This project is licensed under the MIT License - feel free to use it as a reference or learning resource.

---

## 📚 Documentation

Additional documentation available in project root:
- `MATERIAL_DESIGN_BOTTOM_PANEL.md` - Score card implementation
- `BUG_FIX_MAX_CELL_CLICK.md` - Max cell click bug fix details
- `GRID_DESIGN_OPTIONS.md` - Design exploration and options
- `2D_3D_EFFECTS_GUIDE.md` - Shadow and depth techniques

---

## 🤝 Connect

If you're a recruiter or technical lead interested in discussing this project or potential opportunities, I'd love to connect!

**GitHub:** [SanoKhan22](https://github.com/SanoKhan22)

---

<div align="center">

**⭐ Star this repo if you find it interesting!**

*Built with ❤️ and lots of ☕*

</div>
