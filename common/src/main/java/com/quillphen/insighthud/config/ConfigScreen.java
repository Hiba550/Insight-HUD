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
        int panelWidth = 340;
        int panelStartX = centerX - panelWidth / 2;
        int contentStartY = 80;
        int spacing = 22;
        int currentY = contentStartY;
        
        // Entity HUD Section
        this.enableEntityHud = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Entity HUD"), this.font)
                .pos(panelStartX + 20, currentY)
                .selected(config.enableEntityHud)
                .build());
        currentY += spacing;
        
        // Sub-options for Entity HUD (indented)
        this.showEntityHealth = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Show Health"), this.font)
                .pos(panelStartX + 40, currentY)
                .selected(config.showEntityHealth)
                .build());
        
        this.showEntityArmor = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Show Armor"), this.font)
                .pos(panelStartX + 170, currentY)
                .selected(config.showEntityArmor)
                .build());
        currentY += spacing;
        
        this.showEntityEffects = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Show Effects"), this.font)
                .pos(panelStartX + 40, currentY)
                .selected(config.showEntityEffects)
                .build());
        currentY += spacing + 10; // Extra space between sections
        
        // Gear HUD Section
        this.enableGearHud = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Gear HUD"), this.font)
                .pos(panelStartX + 20, currentY)
                .selected(config.enableGearHud)
                .build());
        currentY += spacing + 10; // Extra space
        
        // General Settings Section
        this.enableAnimations = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Smooth Animations"), this.font)
                .pos(panelStartX + 20, currentY)
                .selected(config.enableAnimations)
                .build());
        
        this.hideInF3 = this.addRenderableWidget(
            Checkbox.builder(Component.literal("Hide in F3 Debug"), this.font)
                .pos(panelStartX + 180, currentY)
                .selected(config.hideInF3)
                .build());
        
        // Action buttons at bottom - modern style
        int buttonY = this.height - 50;
        int buttonWidth = 100;
        int buttonHeight = 24;
        
        // Save button (primary action)
        this.addRenderableWidget(Button.builder(Component.literal("Save"), button -> {
            saveConfig();
            this.minecraft.setScreen(this.parent);
        }).bounds(centerX - buttonWidth - 10, buttonY, buttonWidth, buttonHeight).build());
        
        // Cancel button
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> {
            this.minecraft.setScreen(this.parent);
        }).bounds(centerX + 10, buttonY, buttonWidth, buttonHeight).build());
        
        // Reset button (smaller, less prominent)
        this.addRenderableWidget(Button.builder(Component.literal("Reset"), button -> {
            resetToDefaults();
        }).bounds(centerX - 35, buttonY - 30, 70, 18).build());
    }    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        
        // Modern color scheme
        int darkBg = 0xE0000000;        // Semi-transparent dark background
        int panelBg = 0xF0121212;       // Dark panel background
        int accentColor = 0xFF3B82F6;   // Modern blue accent
        int textColor = 0xFFFFFFFF;     // White text
        int subtleText = 0xFFB3B3B3;    // Subtle gray text
        int dividerColor = 0x40FFFFFF;  // Subtle divider
        
        int centerX = this.width / 2;
        int panelWidth = 360;
        int panelHeight = 280;
        int panelX = centerX - panelWidth / 2;
        int panelY = (this.height - panelHeight) / 2 - 10;
        
        // Main panel with subtle shadow
        graphics.fill(panelX + 2, panelY + 2, panelX + panelWidth + 2, panelY + panelHeight + 2, 0x40000000); // Shadow
        graphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, panelBg);
        
        // Header section
        int headerHeight = 50;
        graphics.fill(panelX, panelY, panelX + panelWidth, panelY + headerHeight, 0x20FFFFFF);
        
        // Title
        Component title = Component.literal("Insight HUD Settings");
        int titleX = centerX - this.font.width(title) / 2;
        graphics.drawString(this.font, title, titleX, panelY + 20, textColor);
        
        // Section dividers and labels
        int sectionY = panelY + headerHeight + 20;
        
        // Entity HUD section
        Component entityLabel = Component.literal("Entity Information");
        graphics.drawString(this.font, entityLabel, panelX + 20, sectionY - 5, accentColor);
        graphics.fill(panelX + 20, sectionY + 8, panelX + panelWidth - 20, sectionY + 9, dividerColor);
        
        // Gear HUD section
        int gearSectionY = sectionY + 85;
        Component gearLabel = Component.literal("Equipment Display");
        graphics.drawString(this.font, gearLabel, panelX + 20, gearSectionY - 5, accentColor);
        graphics.fill(panelX + 20, gearSectionY + 8, panelX + panelWidth - 20, gearSectionY + 9, dividerColor);
        
        // General Settings section
        int generalSectionY = gearSectionY + 35;
        Component generalLabel = Component.literal("General Settings");
        graphics.drawString(this.font, generalLabel, panelX + 20, generalSectionY - 5, accentColor);
        graphics.fill(panelX + 20, generalSectionY + 8, panelX + panelWidth - 20, generalSectionY + 9, dividerColor);
        
        // Subtle tooltips on hover
        renderTooltips(graphics, mouseX, mouseY);
        
        super.render(graphics, mouseX, mouseY, partialTick);
        
        // Footer text
        Component footerText = Component.literal("Changes are saved automatically");
        int footerX = centerX - this.font.width(footerText) / 2;
        graphics.drawString(this.font, footerText, footerX, this.height - 25, subtleText);
    }
    
    private void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        // Simple, informative tooltips
        if (isHovering(enableEntityHud, mouseX, mouseY)) {
            graphics.renderTooltip(this.font, Component.literal("Show information about targeted entities"), mouseX, mouseY);
        } else if (isHovering(showEntityHealth, mouseX, mouseY)) {
            graphics.renderTooltip(this.font, Component.literal("Display entity health bars"), mouseX, mouseY);
        } else if (isHovering(showEntityArmor, mouseX, mouseY)) {
            graphics.renderTooltip(this.font, Component.literal("Show entity armor values"), mouseX, mouseY);
        } else if (isHovering(showEntityEffects, mouseX, mouseY)) {
            graphics.renderTooltip(this.font, Component.literal("Display active potion effects"), mouseX, mouseY);
        } else if (isHovering(enableGearHud, mouseX, mouseY)) {
            graphics.renderTooltip(this.font, Component.literal("Show equipment durability status"), mouseX, mouseY);
        } else if (isHovering(enableAnimations, mouseX, mouseY)) {
            graphics.renderTooltip(this.font, Component.literal("Enable smooth transitions and animations"), mouseX, mouseY);
        } else if (isHovering(hideInF3, mouseX, mouseY)) {
            graphics.renderTooltip(this.font, Component.literal("Hide HUD when debug screen is open"), mouseX, mouseY);
        }
    }
    
    private boolean isHovering(Checkbox checkbox, int mouseX, int mouseY) {
        return mouseX >= checkbox.getX() && mouseX <= checkbox.getX() + checkbox.getWidth() &&
               mouseY >= checkbox.getY() && mouseY <= checkbox.getY() + checkbox.getHeight();
    }    private void saveConfig() {
        // Update config with current widget values
        config.enableEntityHud = enableEntityHud.selected();
        config.showEntityHealth = showEntityHealth.selected();
        config.showEntityArmor = showEntityArmor.selected();
        config.showEntityEffects = showEntityEffects.selected();
        config.enableGearHud = enableGearHud.selected();
        config.hideInF3 = hideInF3.selected();
        config.enableAnimations = enableAnimations.selected();
        
        // Save the configuration
        InsightHudConfig.saveConfig();
    }

    private void resetToDefaults() {
        // Reset all checkboxes to default values
        enableEntityHud.selected = true;
        showEntityHealth.selected = true;
        showEntityArmor.selected = true;
        showEntityEffects.selected = true;
        enableGearHud.selected = true;
        hideInF3.selected = true;
        enableAnimations.selected = true;
        
        // Update config with defaults
        config.enableEntityHud = true;
        config.showEntityHealth = true;
        config.showEntityArmor = true;
        config.showEntityEffects = true;
        config.enableGearHud = true;
        config.hideInF3 = true;
        config.enableAnimations = true;
        
        // Save the reset config
        InsightHudConfig.saveConfig();
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}

