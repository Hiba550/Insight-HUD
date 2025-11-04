package com.quillphen.insighthud.forge.mixin;

import com.quillphen.insighthud.HudRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to inject HUD rendering into Forge
 */
@Mixin(Gui.class)
public class GuiMixin {
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        // Render our HUD overlay
        HudRenderer.renderHud(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false));
    }
}
