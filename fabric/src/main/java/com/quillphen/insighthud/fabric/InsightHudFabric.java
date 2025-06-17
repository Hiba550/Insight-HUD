package com.quillphen.insighthud.fabric;

import com.quillphen.insighthud.InsightHudMod;
import net.fabricmc.api.ModInitializer;

/**
 * Fabric main entry point for Insight HUD
 */
public class InsightHudFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        // Initialize the mod
        InsightHudMod.init();
    }
}
