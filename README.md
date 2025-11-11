# 🎮 JavaGrid4 - Strategic Grid Game

> Two-player strategy game built with Java Swing, featuring Material Design UI and clean MVC architecture.

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-4B8BBE?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven)
![Design](https://img.shields.io/badge/Material_Design-UI-757575?style=for-the-badge&logo=material-design)
![Architecture](https://img.shields.io/badge/MVC-Architecture-green?style=for-the-badge)
![Patterns](https://img.shields.io/badge/Design_Patterns-✓-blue?style=for-the-badge)

---

## 🎯 Quick Overview

A turn-based grid strategy game where players compete to claim cells. Click cells to increment values (0→4), affecting adjacent neighbors. Reach 4 to claim! Built to showcase professional Java development skills.

**Key Highlights:**
- 🏗️ Clean MVC architecture (zero Swing in business logic)
- 🎨 Material Design with multi-layer shadow rendering
- ⚡ Performance-optimized animations (40-53% faster)
- 🎮 Command pattern for undo/redo functionality
- 📊 Professional statistics dashboard

---

## 📺 Live Demo

<table>
<tr>
<td width="33%">
<img src="./demos/menu-screen.gif" width="100%"/>
<p align="center"><b>Menu & Setup</b></p>
</td>
<td width="33%">
<img src="./demos/gameplay.gif" width="100%"/>
<p align="center"><b>Gameplay</b></p>
</td>
<td width="33%">
<img src="./demos/results-screen.gif" width="100%"/>
<p align="center"><b>Results</b></p>
</td>
</tr>
</table>

---

## 🎲 Game Rules (Quick)

**Objective:** Claim the most cells by reaching value 4.

**How to Play:**
1. Players take turns clicking cells
2. Clicked cell +1, plus 4 adjacent neighbors +1
3. Cell at value 4 = claimed (earn 1 point)
4. Max cells (4) are locked
5. Game ends when all cells reach 4
6. Most claims wins!

**Board Sizes:** 3×3 (Quick) | 5×5 (Classic) | 7×7 (Expert)

---

## � Live Demo

### Complete Game Flow

Watch the full gameplay experience from setup to victory!

#### 1️⃣ Menu & Setup
![Menu Screen](./demos/menu-screen.gif)
*Professional player setup with real-time validation, color selection, and board size options*

#### 2️⃣ Active Gameplay
![Gameplay](./demos/gameplay.gif)
*Strategic gameplay showing cell claiming, score updates, and smooth animations*

#### 3️⃣ Victory & Statistics
![Results Screen](./demos/results-screen.gif)
*Professional results dashboard with game statistics, confetti animation, and developer credits*

---

## 💡 Open for Improvements

This project represents my recommended implementation and design choices. However, **I welcome contributions and improvements!** 

Feel free to:
- 🎨 Suggest UI/UX enhancements
- ⚡ Optimize performance further
- 🐛 Report bugs or edge cases
- ✨ Propose new features
- � Improve game mechanics
- 📚 Enhance documentation

**Found something that could be better?** Open an issue or submit a pull request! I'm always open to learning and improving the codebase.

---

## �🎲 Game Rules

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

## ⚡ Technical Highlights

<details>
<summary><b>🏗️ Clean Architecture & Design Patterns</b></summary>

- **MVC Pattern** - Zero Swing imports in business logic
- **Command Pattern** - Full undo/redo with state restoration
- **Observer Pattern** - PropertyChangeListener for event handling
- **Factory Pattern** - Reusable component creation
- **Strategy Pattern** - Flexible animation system

```java
// Example: Pure business logic - NO UI dependencies
public class GameEngine {
    public int applyMove(int row, int col, Player player) {
        // Game logic only
    }
}
```
</details>

<details>
<summary><b>🎨 Material Design Implementation</b></summary>

- Multi-layer shadow rendering (5 layers for depth)
- Elevation system (2dp-10dp)
- Custom Graphics2D painting
- 280ms faster animations (40-53% improvement)
- Regional repaints for performance

```java
// Multi-layer shadow for realistic depth
for (int i = 5; i >= 1; i--) {
    int alpha = Math.min((elevation * 15) / (i + 1), 90);
    g2d.setColor(new Color(0, 0, 0, alpha));
    g2d.fill(new RoundRectangle2D.Float(...));
}
```
</details>

<details>
<summary><b>⚡ Performance Optimizations</b></summary>

| Component | Before | After | Improvement |
|-----------|--------|-------|-------------|
| ScoreAnimation | 320ms | 200ms | **40% faster** |
| PauseOverlay | 270ms | 130ms | **53% faster** |
| CellAnimation | 250ms | 160ms | **36% faster** |
| GameOverOverlay | 2.24s | 1.16s | **48% faster** |

- Regional repaints (not full screen)
- Reduced particle counts
- Optimized frame rates
- Removed visual clutter
</details>

---

## 🚀 Quick Start

```bash
# Clone & build
git clone https://github.com/SanoKhan22/JavaGrid4.git
cd JavaGrid4
mvn clean install

# Run
mvn exec:java
```

**Requirements:** Java 17+, Maven 3.6+

---
- **ScoreIncrementAnimation** - 320ms → 200ms (40% faster), disabled particle trails
- **PauseOverlay** - Fade-in 270ms → 130ms (53% faster), fade-out 176ms → 100ms (45% faster)
- **CellClaimAnimation** - 250ms → 160ms (36% faster), reduced particles from 12 to 8
- **GameOverOverlay** - 2.24s → 1.16s (48% faster)
- **Regional Repaints** - Only update changed cells instead of full panel repaints
- **Removed Visual Clutter** - Eliminated overlapping +1 animations for cleaner gameplay

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

## 🤝 Contributing

Open to contributions! Fork, improve, and submit PRs. See areas for enhancement in the code.

---

## 📄 License

MIT License - Free to use as reference or learning resource.

---

## � Connect

**Sano Khan** | [@SanoKhan22](https://github.com/SanoKhan22)

Built to showcase Java development skills for software engineering roles.

---

<div align="center">

⭐ **Star if you find this interesting!**

*Portfolio project demonstrating professional Java development*

</div>
