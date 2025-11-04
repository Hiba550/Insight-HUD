package com.quillphen.insighthud.neoforge.client;

import com.quillphen.insighthud.InsightHudMod;
import com.quillphen.insighthud.HudRenderer;
import com.quillphen.insighthud.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * NeoForge client event handler for Insight HUD
 */
@EventBusSubscriber(modid = InsightHudMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Initialize client-side components
        InsightHudMod.initClient();
        
        // Register event handlers
        NeoForge.EVENT_BUS.register(GameEventHandler.class);
    }
    
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        // Register key bindings
        event.register(InsightHudMod.OPEN_CONFIG_KEY);
        if (InsightHudMod.SHOW_LOOT_TABLE_KEY != null) {
            event.register(InsightHudMod.SHOW_LOOT_TABLE_KEY);
        }
    }
}

@EventBusSubscriber(modid = InsightHudMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
class GameEventHandler {
    
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiEvent.Post event) {
        // Render the HUD overlay
        HudRenderer.renderHud(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
    }
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) {
            Minecraft minecraft = Minecraft.getInstance();
            
            // Handle key presses
            if (minecraft.screen == null) {
                if (InsightHudMod.OPEN_CONFIG_KEY.consumeClick()) {
                    minecraft.setScreen(new ConfigScreen(null));
                }
            }
        }
    }
}