package com.quillphen.insighthud.neoforge;

import com.quillphen.insighthud.InsightHudMod;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

/**
 * NeoForge entry point for Insight HUD
 */
@Mod(InsightHudMod.MOD_ID)
public class InsightHudNeoForge {
    
    public InsightHudNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // Initialize the mod
        InsightHudMod.init();
        
        // TODO: Add NeoForge-specific client initialization when dependencies are available
    }
}
