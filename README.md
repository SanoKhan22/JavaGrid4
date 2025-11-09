# 🎮 JavaGrid4 - Strategic Grid Game

> A sophisticated two-player strategy game showcasing advanced Java Swing development and clean architecture patterns.

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=java)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Build-Maven-red?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

---

## 📌 Overview

**JavaGrid4** is a turn-based strategy game where two players compete to claim cells on a dynamic grid. The game demonstrates professional-level Java development with modern UI/UX design, custom component rendering, and robust architecture patterns.

### 🎯 Why This Project Stands Out

- ✅ **Clean Architecture** - Strict separation of business logic and UI (MVC pattern)
- ✅ **Advanced Swing Components** - Custom-rendered components with Graphics2D
- ✅ **Multi-Screen Navigation** - Professional CardLayout implementation
- ✅ **Real-Time Validation** - Live user feedback with visual indicators
- ✅ **Design Patterns** - Observer, Strategy, Factory patterns throughout
- ✅ **Production-Ready Code** - Comprehensive error handling and validation

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

### 🎨 Professional UI/UX
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

### 🏗️ Architecture Highlights

```
📁 Project Structure
├── models/           # Data models (PlayerConfig, GameConfig)
├── ui/
│   ├── components/   # Reusable components (ColorPickerButton, BoardSizeCard)
│   └── screens/      # Screen panels (MenuPanel, GamePanel)
├── GameEngine.java   # Pure business logic (NO Swing imports)
├── GameState.java    # State management
└── JavaGrid4.java    # Entry point with CardLayout
```

**Key Architectural Decisions:**
- ✅ **Zero Swing imports** in business logic classes
- ✅ **PropertyChangeListener** for screen-to-screen communication
- ✅ **Custom paintComponent** for advanced rendering
- ✅ **Immutable configurations** passed between screens

---

## 🚀 Technologies & Skills Demonstrated

### Core Technologies
- **Java 17+** - Modern Java features and best practices
- **Swing GUI** - Advanced components and custom rendering
- **Maven** - Dependency management and build automation
- **Graphics2D** - Custom painting, gradients, antialiasing

### Design Patterns
- **MVC (Model-View-Controller)** - Separation of concerns
- **Observer Pattern** - PropertyChangeListener for event handling
- **Strategy Pattern** - Flexible game rule implementations
- **Factory Pattern** - Component creation

### UI/UX Techniques
- **CardLayout** - Multi-screen navigation
- **Custom Components** - Extended JPanel/JComponent
- **Focus Management** - Visual feedback for user interactions
- **Real-Time Validation** - Immediate user feedback
- **Responsive Design** - Proper layout managers (GridBagLayout)

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
*Professional player setup with enhanced validation and visual feedback*

### Game Board
*Clean, intuitive gameplay interface with real-time updates*

### Results Screen
*Clear winner announcement with replay options*

---

## 🎯 Code Quality Highlights

### Clean Code Practices
```java
// Example: Pure business logic with zero UI dependencies
public class GameEngine {
    // NO Swing imports!
    public void applyMove(int row, int col, Player player) {
        // Increment clicked cell + 4 neighbors
        // Award points when cells reach 4
        // Maintain single responsibility
    }
}
```

### Custom Component Example
```java
// Example: Advanced Graphics2D rendering
public class ColorPickerButton extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Antialiasing, gradients, shadows, highlights
        // Professional 3D effect rendering
    }
}
```

---

## 🌟 What Recruiters Will Notice

1. **Architecture Excellence** 
   - Clean separation between GUI and logic
   - Testable business logic (zero UI dependencies)
   - Proper use of design patterns

2. **Advanced UI Skills**
   - Custom component development
   - Graphics2D rendering techniques
   - Professional layout management

3. **Code Quality**
   - Comprehensive JavaDoc comments
   - Consistent naming conventions
   - Error handling and validation

4. **Modern Development Practices**
   - Maven project structure
   - Git version control
   - Modular, maintainable codebase

5. **Attention to Detail**
   - Pixel-perfect spacing and alignment
   - Smooth user interactions
   - Visual feedback for all actions

---

## 🔮 Future Enhancements

- [ ] Animated screen transitions
- [ ] Results screen with replay functionality
- [ ] Undo/Redo move functionality
- [ ] AI opponent with difficulty levels
- [ ] Theme system (dark mode, custom colors)
- [ ] Sound effects and background music
- [ ] Save/Load game state
- [ ] Online multiplayer support

---

## 👨‍💻 About the Developer

This project demonstrates my expertise in:
- **Java Development** - Clean, maintainable, object-oriented code
- **UI/UX Design** - Modern, intuitive user interfaces
- **Software Architecture** - Scalable, testable design patterns
- **Problem Solving** - Creative solutions to technical challenges

**Perfect for roles in:**
- Java Software Engineer
- Full-Stack Developer
- UI/UX Engineer
- Software Architect

---

## 📄 License

This project is licensed under the MIT License - feel free to use it as a reference or learning resource.

---

## 🤝 Connect

If you're a recruiter or technical lead interested in discussing this project or potential opportunities, I'd love to connect!

**GitHub:** [SanoKhan22](https://github.com/SanoKhan22)

---

<div align="center">

**⭐ Star this repo if you find it interesting!**

*Built with ❤️ and lots of ☕*

</div>
