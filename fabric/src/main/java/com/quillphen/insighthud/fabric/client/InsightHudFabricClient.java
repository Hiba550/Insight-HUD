package com.quillphen.insighthud.fabric.client;

import com.quillphen.insighthud.InsightHudMod;
import com.quillphen.insighthud.HudRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

/**
 * Fabric client entry point for Insight HUD
 */
public class InsightHudFabricClient implements ClientModInitializer {
    
    @Override
    @SuppressWarnings("deprecation")
    public void onInitializeClient() {
        // Initialize the mod
        InsightHudMod.initClient();
        
        // Register key bindings
        KeyBindingHelper.registerKeyBinding(InsightHudMod.OPEN_CONFIG_KEY);
        KeyBindingHelper.registerKeyBinding(InsightHudMod.SHOW_LOOT_TABLE_KEY);
        
        // Register HUD rendering
        HudRenderCallback.EVENT.register((graphics, tickCounter) -> {
            HudRenderer.renderHud(graphics, tickCounter.getGameTimeDeltaPartialTick(false));
        });
          // Register key handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (InsightHudMod.OPEN_CONFIG_KEY.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new com.quillphen.insighthud.config.ConfigScreen(null));
                }
            }
              while (InsightHudMod.SHOW_LOOT_TABLE_KEY.consumeClick()) {
                if (client.screen == null) {
                    var targetEntity = com.quillphen.insighthud.util.EntityUtil.getTargetedEntity();
                    if (targetEntity != null) {
                        client.setScreen(new com.quillphen.insighthud.ui.LootTableScreen(null, targetEntity));
                    }
                }
            }
        });
    }
}
