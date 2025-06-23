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
    }    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int leftColumn = centerX - 160;
        int rightColumn = centerX + 20;
        int startY = 90;
        int spacing = 24;
        int currentY = startY;
        
        // Left column - Entity HUD options
        this.enableEntityHud = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Enable Entity HUD"), this.font)
                .pos(leftColumn, currentY)
                .selected(config.enableEntityHud)
                .build());
        currentY += spacing;
        
        this.showEntityHealth = this.addRenderableWidget(
            Checkbox.builder(Component.literal("  Show Entity Health"), this.font)
                .pos(leftColumn + 16, currentY)
                .selected(config.showEntityHealth)
                .build());
        currentY += spacing;
        
        this.showEntityArmor = this.addRenderableWidget(
            Checkbox.builder(Component.literal("  Show Entity Armor"), this.font)
                .pos(leftColumn + 16, currentY)
                .selected(config.showEntityArmor)
                .build());
        currentY += spacing;
        
        this.showEntityEffects = this.addRenderableWidget(
            Checkbox.builder(Component.literal("  Show Entity Effects"), this.font)
                .pos(leftColumn + 16, currentY)
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
                .build());        // Buttons at bottom with better spacing and clearer labels
        int buttonY = this.height - 45; // Moved up more for better visibility
        int buttonWidth = 90;
        int buttonHeight = 20;
        
        // Save & Close button (more prominent) - always ensure it's visible
        this.addRenderableWidget(Button.builder(Component.literal("Save & Exit"), button -> {
            saveConfig();
            this.minecraft.setScreen(this.parent);
        }).bounds(centerX - buttonWidth - 5, buttonY, buttonWidth, buttonHeight).build());
        
        // Reset to Defaults button
        this.addRenderableWidget(Button.builder(Component.literal("Reset"), button -> {
            resetToDefaults();
        }).bounds(centerX + 5, buttonY, buttonWidth, buttonHeight).build());
        
        // Cancel button for convenience - positioned below the main buttons
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> {
            this.minecraft.setScreen(this.parent);
        }).bounds(centerX - buttonWidth/2, buttonY + 25, buttonWidth, buttonHeight).build());
    }    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        
        // Enhanced background with improved modern styling
        int bgColor = 0x99000000; // Slightly more opaque background
        int panelColor = 0xAA222222; // Darker panel for better contrast
        int headerColor = 0xDD1E1E1E; // Dark header
        int accentColor = 0xFF5599FF; // Brighter blue accent
        int separatorColor = 0x66FFFFFF; // Subtle separator
        
        // Main panel with subtle border
        int panelMargin = 25;
        graphics.fill(panelMargin, panelMargin, this.width - panelMargin, this.height - panelMargin, bgColor);
        graphics.fill(panelMargin, panelMargin, this.width - panelMargin, panelMargin + 1, separatorColor);
        graphics.fill(panelMargin, this.height - panelMargin - 1, this.width - panelMargin, this.height - panelMargin, separatorColor);
        graphics.fill(panelMargin, panelMargin, panelMargin + 1, this.height - panelMargin, separatorColor);
        graphics.fill(this.width - panelMargin - 1, panelMargin, this.width - panelMargin, this.height - panelMargin, separatorColor);
        
        // Header section with gradient effect
        graphics.fill(panelMargin, panelMargin, this.width - panelMargin, 75, headerColor);
        graphics.fill(panelMargin, 73, this.width - panelMargin, 75, accentColor);
        
        // Column separator
        int centerX = this.width / 2;
        graphics.fill(centerX - 1, 80, centerX + 1, this.height - 60, 0x33FFFFFF);
        
        // Title with enhanced styling and better positioning
        Component title = Component.literal("Insight HUD Configuration");
        int titleX = centerX - this.font.width(title) / 2;
        // Drop shadow
        graphics.drawString(this.font, title, titleX + 1, 42, 0x80000000);
        // Main title
        graphics.drawString(this.font, title, titleX, 41, 0xFFFFFFFF);
        
        // Section headers with icons and better styling
        Component entityHeader = Component.literal("🎯 Entity Information");
        Component gearHeader = Component.literal("⚔ Equipment & General");
        
        int leftHeader = centerX - 160;
        int rightHeader = centerX + 20;
        
        // Section headers with background
        graphics.fill(leftHeader - 4, 57, leftHeader + this.font.width(entityHeader) + 4, 69, 0x44000000);
        graphics.fill(rightHeader - 4, 57, rightHeader + this.font.width(gearHeader) + 4, 69, 0x44000000);
        
        graphics.drawString(this.font, entityHeader, leftHeader, 60, accentColor);
        graphics.drawString(this.font, gearHeader, rightHeader, 60, accentColor);
        
        // Enhanced tooltips with better descriptions
        if (mouseX >= enableEntityHud.getX() && mouseX <= enableEntityHud.getX() + enableEntityHud.getWidth() &&
            mouseY >= enableEntityHud.getY() && mouseY <= enableEntityHud.getY() + enableEntityHud.getHeight()) {
            graphics.renderTooltip(this.font, Component.literal("Display information about the entity you're targeting"), mouseX, mouseY);
        }
        
        if (mouseX >= showEntityHealth.getX() && mouseX <= showEntityHealth.getX() + showEntityHealth.getWidth() &&
            mouseY >= showEntityHealth.getY() && mouseY <= showEntityHealth.getY() + showEntityHealth.getHeight()) {
            graphics.renderTooltip(this.font, Component.literal("Show health hearts or health bar for targeted entities"), mouseX, mouseY);
        }
        
        if (mouseX >= showEntityArmor.getX() && mouseX <= showEntityArmor.getX() + showEntityArmor.getWidth() &&
            mouseY >= showEntityArmor.getY() && mouseY <= showEntityArmor.getY() + showEntityArmor.getHeight()) {
            graphics.renderTooltip(this.font, Component.literal("Display armor value and protection level"), mouseX, mouseY);
        }
        
        if (mouseX >= showEntityEffects.getX() && mouseX <= showEntityEffects.getX() + showEntityEffects.getWidth() &&
            mouseY >= showEntityEffects.getY() && mouseY <= showEntityEffects.getY() + showEntityEffects.getHeight()) {
            graphics.renderTooltip(this.font, Component.literal("Show active potion effects with levels"), mouseX, mouseY);
        }
        
        if (mouseX >= enableGearHud.getX() && mouseX <= enableGearHud.getX() + enableGearHud.getWidth() &&
            mouseY >= enableGearHud.getY() && mouseY <= enableGearHud.getY() + enableGearHud.getHeight()) {
            graphics.renderTooltip(this.font, Component.literal("Display durability and status of your equipment"), mouseX, mouseY);
        }
        
        if (mouseX >= hideInF3.getX() && mouseX <= hideInF3.getX() + hideInF3.getWidth() &&
            mouseY >= hideInF3.getY() && mouseY <= hideInF3.getY() + hideInF3.getHeight()) {
            graphics.renderTooltip(this.font, Component.literal("Hide HUD when F3 debug screen is open"), mouseX, mouseY);
        }
        
        if (mouseX >= enableAnimations.getX() && mouseX <= enableAnimations.getX() + enableAnimations.getWidth() &&
            mouseY >= enableAnimations.getY() && mouseY <= enableAnimations.getY() + enableAnimations.getHeight()) {
            graphics.renderTooltip(this.font, Component.literal("Enable smooth animations and transitions for better visual appeal"), mouseX, mouseY);
        }          // Help text at bottom with better positioning
        Component helpText = Component.literal("Click 'Save & Exit' to apply changes • Click 'Cancel' to exit without saving");
        int helpX = centerX - this.font.width(helpText) / 2;
        graphics.drawString(this.font, helpText, helpX, this.height - 75, 0xFF999999);
        
        // Additional help text for save button visibility
        Component saveText = Component.literal("All changes are saved automatically when you click 'Save & Exit'");
        int saveX = centerX - this.font.width(saveText) / 2;
        graphics.drawString(this.font, saveText, saveX, this.height - 63, 0xFFAAAAFF);
        
        super.render(graphics, mouseX, mouseY, partialTick);
    }
      private void saveConfig() {
        // Update config with current widget values
        config.enableEntityHud = enableEntityHud.selected();
        config.showEntityHealth = showEntityHealth.selected();
        config.showEntityArmor = showEntityArmor.selected();
        config.showEntityEffects = showEntityEffects.selected();
        config.enableGearHud = enableGearHud.selected();
        config.hideInF3 = hideInF3.selected();
        config.enableAnimations = enableAnimations.selected();
        
        // Force save the configuration to file
        try {
            InsightHudConfig.saveConfig();
            System.out.println("Insight HUD: Configuration saved successfully");
        } catch (Exception e) {
            System.err.println("Insight HUD: Error saving configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }private void resetToDefaults() {
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

