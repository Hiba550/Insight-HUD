package com.quillphen.insighthud;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * Main mod class for Insight HUD
 */
public class InsightHudMod {
    
    public static final String MOD_ID = "insighthud";
    public static final String MOD_NAME = "Insight HUD";
    
    // Key bindings
    public static KeyMapping OPEN_CONFIG_KEY;
    public static KeyMapping SHOW_LOOT_TABLE_KEY;
    
    public static void init() {
        // Initialize key mappings
        OPEN_CONFIG_KEY = new KeyMapping(
            "key.insighthud.open_config",
            GLFW.GLFW_KEY_H,
            "key.categories.insighthud"
        );
        
        SHOW_LOOT_TABLE_KEY = new KeyMapping(
            "key.insighthud.show_loot_table",
            GLFW.GLFW_KEY_L,
            "key.categories.insighthud"
        );
        
        // Initialize config
        InsightHudConfig.init();
    }
    
    public static void initClient() {
        // Client-side initialization
        HudRenderer.init();
    }
}
