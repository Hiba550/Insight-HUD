package com.quillphen.insighthud.config;

import com.quillphen.insighthud.InsightHudConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * Simple configuration screen for Insight HUD
 */
public class ConfigScreen extends Screen {
    
    private final Screen parent;
    private InsightHudConfig.ConfigData config;
    
    // UI Components
    private Checkbox enableEntityHud;
    private Checkbox showEntityHealth;
    private Checkbox showEntityArmor;
    private Checkbox showEntityEffects;
    private Checkbox enableGearHud;
    private Checkbox hideInF3;
    private Checkbox enableAnimations;
    
    public ConfigScreen(Screen parent) {
        super(Component.literal("Insight HUD Configuration"));
        this.parent = parent;
        this.config = InsightHudConfig.getConfig();
    }
      @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int leftColumn = centerX - 160;
        int rightColumn = centerX + 20;
        int startY = 70;
        int spacing = 25;
        int currentY = startY;
        
        // Left column - Entity HUD options
        this.enableEntityHud = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Enable Entity HUD"), this.font)
                .pos(leftColumn, currentY)
                .selected(config.enableEntityHud)
                .build());
        currentY += spacing;
        
        this.showEntityHealth = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Show Entity Health"), this.font)
                .pos(leftColumn + 20, currentY)
                .selected(config.showEntityHealth)
                .build());
        currentY += spacing;
        
        this.showEntityArmor = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Show Entity Armor"), this.font)
                .pos(leftColumn + 20, currentY)
                .selected(config.showEntityArmor)
                .build());
        currentY += 40;
        
        this.showEntityEffects = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Show Entity Effects"), this.font)
                .pos(leftColumn + 20, currentY)
                .selected(config.showEntityEffects)
                .build());
        
        // Right column - Gear HUD and General options
        currentY = startY;
        this.enableGearHud = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Enable Gear HUD"), this.font)
                .pos(rightColumn, currentY)
                .selected(config.enableGearHud)
                .build());
        currentY += spacing;
        
        this.hideInF3 = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Hide in F3 Debug"), this.font)
                .pos(rightColumn, currentY)
                .selected(config.hideInF3)
                .build());
        currentY += spacing;
        
        this.enableAnimations = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Enable Animations"), this.font)
                .pos(rightColumn, currentY)
                .selected(config.enableAnimations)
                .build());
        
        // Buttons at bottom
        int buttonY = this.height - 40;
        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> {
            saveConfig();
            this.minecraft.setScreen(this.parent);
        }).bounds(centerX - 100, buttonY, 95, 20).build());
        
        this.addRenderableWidget(Button.builder(Component.literal("Reset"), button -> {
            resetToDefaults();
        }).bounds(centerX + 5, buttonY, 95, 20).build());
    }
      @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        
        // Enhanced background with transparency
        int bgColor = 0x66000000; // Semi-transparent black
        graphics.fill(20, 15, this.width - 20, this.height - 15, bgColor);
        
        // Border
        int borderColor = 0xFF404040;
        graphics.fill(20, 15, this.width - 20, 16, borderColor);
        graphics.fill(20, this.height - 16, this.width - 20, this.height - 15, borderColor);
        graphics.fill(20, 15, 21, this.height - 15, borderColor);
        graphics.fill(this.width - 21, 15, this.width - 20, this.height - 15, borderColor);
        
        // Main title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 25, 0xFFFFFF);
        
        // Section headers
        graphics.drawString(this.font, "Entity HUD Settings:", 30, 45, 0xFFD700);
        graphics.drawString(this.font, "General Settings:", 30, 145, 0xFFD700);
        
        // Tooltips
        if (mouseX >= enableEntityHud.getX() && mouseX <= enableEntityHud.getX() + enableEntityHud.getWidth() &&
            mouseY >= enableEntityHud.getY() && mouseY <= enableEntityHud.getY() + enableEntityHud.getHeight()) {
            graphics.renderTooltip(this.font, Component.literal("Show information about entities you're looking at"), mouseX, mouseY);
        }
        
        super.render(graphics, mouseX, mouseY, partialTick);
    }
    
    private void saveConfig() {
        config.enableEntityHud = enableEntityHud.selected();
        config.showEntityHealth = showEntityHealth.selected();
        config.showEntityArmor = showEntityArmor.selected();
        config.showEntityEffects = showEntityEffects.selected();
        config.enableGearHud = enableGearHud.selected();
        config.hideInF3 = hideInF3.selected();
        config.enableAnimations = enableAnimations.selected();
        
        InsightHudConfig.saveConfig();
    }    private void resetToDefaults() {
        // Reset config to defaults
        config.enableEntityHud = true;
        config.showEntityHealth = true;
        config.showEntityArmor = true;
        config.showEntityEffects = true;
        config.enableGearHud = true;
        config.hideInF3 = true;
        config.enableAnimations = true;
        
        // Recreate the widgets to reset their state
        this.clearWidgets();
        this.init();
        
        // Save the reset config
        InsightHudConfig.saveConfig();
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
