package com.quillphen.insighthud.mixin;

import com.quillphen.insighthud.HudRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to inject HUD rendering into Forge's GUI rendering
 */
@Mixin(Gui.class)
public class ForgeGuiMixin {
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderGui(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        // Render our HUD after the main GUI is rendered
        HudRenderer.renderHud(guiGraphics, partialTick);
    }
}
