package com.quillphen.insighthud.forge;

import com.quillphen.insighthud.InsightHudMod;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge entry point for Insight HUD
 */
@Mod(InsightHudMod.MOD_ID)
public class InsightHudForge {
    
    public InsightHudForge() {
        // Initialize the mod
        InsightHudMod.init();
        
        // TODO: Add Forge-specific initialization when needed
    }
}
