package com.quillphen.insighthud;

import net.minecraft.client.Minecraft;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration management for Insight HUD
 */
public class InsightHudConfig {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path configPath;
    private static ConfigData config = new ConfigData();
    
    public static void init() {
        if (Minecraft.getInstance().gameDirectory != null) {
            configPath = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("insighthud.json");
            loadConfig();
        }
    }
    
    public static ConfigData getConfig() {
        return config;
    }
    
    public static void saveConfig() {
        if (configPath == null) return;
        
        try {
            Files.createDirectories(configPath.getParent());
            try (FileWriter writer = new FileWriter(configPath.toFile())) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void loadConfig() {
        if (configPath == null || !Files.exists(configPath)) {
            saveConfig(); // Create default config
            return;
        }
        
        try (FileReader reader = new FileReader(configPath.toFile())) {
            ConfigData loaded = GSON.fromJson(reader, ConfigData.class);
            if (loaded != null) {
                config = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static class ConfigData {
        // Entity Info HUD Settings
        public boolean enableEntityHud = true;
        public float entityHudScale = 1.0f;
        public int entityHudX = 10;
        public int entityHudY = 10;
        public float entityHudOpacity = 0.9f;
        public boolean showEntityHealth = true;
        public boolean showEntityArmor = true;
        public boolean showEntityEffects = true;
        public boolean showEntityName = true;
        
        // Gear Durability HUD Settings
        public boolean enableGearHud = true;
        public float gearHudScale = 1.0f;
        public int gearHudX = 10;
        public int gearHudY = 50;
        public float gearHudOpacity = 0.9f;
        public boolean showArmorDurability = true;
        public boolean showToolDurability = true;
        public boolean showShieldDurability = true;
        public boolean showCooldowns = true;
        public boolean showEnchantments = false;
        
        // General Settings
        public HudPosition hudPosition = HudPosition.TOP_RIGHT;
        public ThemeType theme = ThemeType.VANILLA;
        public boolean enableAnimations = true;
        public boolean hideInF3 = true;
        public boolean enableLootTableView = true;
        
        // Color thresholds
        public float durabilityGreenThreshold = 0.75f;
        public float durabilityYellowThreshold = 0.30f;
    }
    
    public enum HudPosition {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER_LEFT,
        CENTER_RIGHT,
        CUSTOM
    }
    
    public enum ThemeType {
        VANILLA,
        DARK,
        LIGHT
    }
}
