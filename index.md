# Insight HUD

**An immersive information overlay mod for Minecraft 1.21.5+**

*Author: quillphen*

---

## Overview
Insight HUD is a modern, highly customizable Minecraft mod that provides real-time, in-game overlays for both entity information and gear durability. Designed to blend seamlessly with the vanilla UI, it gives players instant access to critical information about mobs, their own equipment, and more—without cluttering the screen.

---

## Features

### 🎯 Entity Info HUD
- **Mob Information**: See the name, health, and armor of any entity you target.
- **Status Effects**: Instantly view all active potion effects on mobs.
- **Loot Preview**: Get a quick look at potential drops for the entity you're targeting.
- **Smart Targeting**: Automatically detects and displays info for the entity under your crosshair.
- **Improved Mob Icons**: Color-coded and fallback icons for easy identification.
- **Numerical Health Display**: Shows exact HP values (e.g., "85 / 100 HP").
- **Dynamic Health Bar**: Color-coded (green/yellow/red) with smooth gradients.
- **Smooth Animations**: Fade in/out and transitions for a polished look.

### ⚔️ Gear Durability HUD
- **Equipment Status**: Real-time durability for all equipped armor, tools, and shields.
- **Color-Coded Indicators**: Durability bars change color based on condition.
- **Enchantment Display**: See all active enchantments on your gear.
- **Cooldown Timers**: Visual countdowns for item abilities.

### 🎨 Customization
- **Flexible Positioning**: Move HUD elements anywhere on the screen.
- **Scale & Opacity**: Adjust size and transparency to your liking.
- **Module System**: Enable or disable individual HUD components.
- **Theme Support**: Choose from multiple visual themes.
- **Smooth Animations**: Configurable transitions and effects.

### 🛠️ General
- **Hide in F3 Debug**: Optionally hide HUD when F3 is open.
- **Keybinds**: Open config or loot table screens with a single keypress.
- **Multi-Loader Support**: Works on Fabric, Forge, and NeoForge.

---

## Installation

### Prerequisites
- **Minecraft 1.21.5+**
- **Java 21 or newer**
- **A supported mod loader:**
  - [Fabric](https://fabricmc.net/)
  - [Forge](https://files.minecraftforge.net/)
  - [NeoForge](https://neoforged.net/)

### Steps
1. **Download the latest Insight HUD release** for your loader from the [Releases](https://github.com/quillphen/insighthud/releases) page or your mod platform of choice.
2. **Install the required mod loader** (Fabric, Forge, or NeoForge) for your Minecraft version.
3. **Place the Insight HUD jar** into your `mods` folder.
4. **(Optional)**: Install [Fabric API](https://modrinth.com/mod/fabric-api) if using Fabric.
5. **Launch Minecraft** and enjoy!

---

## How to Use

### Accessing the HUD
- **Entity Info HUD**: Simply look at any mob or entity. The HUD will appear automatically, showing all relevant info.
- **Gear Durability HUD**: Always visible in the bottom right (default), showing your armor, tools, and shield status.

### Configuration
- **Open the Config Screen**: Press the default keybind (`H`) or access via the mod menu.
- **Options Include:**
  - Enable/disable Entity HUD or Gear HUD
  - Show/hide entity health, armor, and effects
  - Hide HUD in F3 debug
  - Enable/disable smooth animations
  - Adjust scale, opacity, and position
  - Switch between visual themes
  - Reset to defaults at any time

### Loot Table Preview
- **Show Loot Table**: Press the loot table keybind (configurable) while targeting an entity to see its drops.

---

## Advanced

### Key Bindings
- `H`: Open Config Screen
- (Customizable in Minecraft Controls menu)

### Supported Loaders
- **Fabric**: Full support, including Fabric API features.
- **Forge**: Full support.
- **NeoForge**: Full support.

### Compatibility
- Designed for vanilla and most modded environments.
- Minimal performance impact.
- No server-side installation required (client-only mod).

---

## Development

### Building from Source
1. **Clone the repository**
2. **Run** `./gradlew build` (Linux/macOS) or `gradlew.bat build` (Windows)
3. **Find the built jars** in the `build/libs/` directory of each loader subproject

### Contributing
- PRs and suggestions are welcome! See [IMPROVEMENTS.md](IMPROVEMENTS.md) for ideas and changelog.

---

## License

Insight HUD is licensed under [CC0 1.0 Universal (Public Domain)](LICENSE).

---

## Credits
- **Author:** quillphen
- **Special Thanks:** Minecraft modding community

---

## Troubleshooting
- If the HUD does not appear, ensure you are using the correct Minecraft and Java versions, and that you have the correct loader and dependencies installed.
- For help, open an issue on the [GitHub repository](https://github.com/quillphen/insighthud).

---

Enjoy a smarter, cleaner Minecraft experience with Insight HUD!
