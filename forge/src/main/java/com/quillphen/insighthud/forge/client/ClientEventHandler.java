package com.quillphen.insighthud.forge.client;

import com.quillphen.insighthud.InsightHudMod;
import com.quillphen.insighthud.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Forge client event handler for Insight HUD
 */
@Mod.EventBusSubscriber(modid = InsightHudMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Initialize client-side components
        InsightHudMod.initClient();
        
        // Register event handlers
        MinecraftForge.EVENT_BUS.register(ForgeClientEvents.class);
    }
    
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        // Register key bindings
        event.register(InsightHudMod.OPEN_CONFIG_KEY);
        if (InsightHudMod.SHOW_LOOT_TABLE_KEY != null) {
            event.register(InsightHudMod.SHOW_LOOT_TABLE_KEY);
        }
    }
      @Mod.EventBusSubscriber(modid = InsightHudMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeClientEvents {        
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
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
}