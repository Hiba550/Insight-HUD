package com.quillphen.insighthud.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

/**
 * Enhanced helper class for advanced rendering operations with vanilla-style visuals
 */
public class RenderHelper {
    
    private static final ResourceLocation WIDGETS_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/widgets.png");
    
    /**
     * Renders a polished rounded rectangle with gradient, shadow and vanilla styling
     */
    public static void drawRoundedPanel(GuiGraphics graphics, int x, int y, int width, int height, 
                                      float opacity, boolean shadow, boolean glow) {
        int alpha = (int) (opacity * 200); // Max 200 for better visibility
        
        if (shadow) {
            // Soft shadow with gradient
            drawGradientRect(graphics, x + 2, y + 2, x + width + 4, y + height + 4, 
                           0x30000000, 0x10000000);
        }
        
        // Main panel background with subtle gradient
        int bgColorTop = (alpha << 24) | 0x1a1a1a;
        int bgColorBottom = (alpha << 24) | 0x0d0d0d;
        drawGradientRect(graphics, x, y, x + width, y + height, bgColorTop, bgColorBottom);
        
        // Vanilla-style border
        int borderColorLight = ((alpha / 2) << 24) | 0x5a5a5a;
        int borderColorDark = ((alpha / 2) << 24) | 0x2a2a2a;
        
        // Light border (top and left)
        graphics.fill(x, y, x + width, y + 1, borderColorLight);
        graphics.fill(x, y, x + 1, y + height, borderColorLight);
        
        // Dark border (bottom and right)
        graphics.fill(x, y + height - 1, x + width, y + height, borderColorDark);
        graphics.fill(x + width - 1, y, x + width, y + height, borderColorDark);
        
        if (glow) {
            // Subtle inner glow
            int glowColor = ((alpha / 4) << 24) | 0xffffff;
            graphics.fill(x + 1, y + 1, x + width - 1, y + 2, glowColor);
        }
    }
      /**    /**
     * Draws a gradient rectangle (simplified for now)
     */
    private static void drawGradientRect(GuiGraphics graphics, int x1, int y1, int x2, int y2, 
                                       int colorTop, int colorBottom) {
        // Simplified version - just fill with top color for now
        graphics.fill(x1, y1, x2, y2, colorTop);
    }
    
    /**
     * Draws vanilla-style health hearts with smooth animation
     */
    public static void drawHealthHearts(GuiGraphics graphics, float health, float maxHealth, 
                                      int x, int y, boolean showNumbers) {
        int fullHearts = (int) Math.floor(health / 2.0f);
        boolean hasHalfHeart = (health % 2.0f) >= 1.0f;
        int totalHearts = (int) Math.ceil(maxHealth / 2.0f);
        
        // Limit to reasonable number of hearts
        totalHearts = Math.min(totalHearts, 10);
        
        for (int i = 0; i < totalHearts; i++) {
            int heartX = x + i * 9;
            int heartY = y;
            
            // Draw heart container (empty heart)
            drawHeartIcon(graphics, heartX, heartY, HeartType.CONTAINER);
            
            if (i < fullHearts) {
                // Full heart
                drawHeartIcon(graphics, heartX, heartY, HeartType.FULL);
            } else if (i == fullHearts && hasHalfHeart) {
                // Half heart
                drawHeartIcon(graphics, heartX, heartY, HeartType.HALF);
            }
        }
        
        // Show numerical health if requested or too many hearts
        if (showNumbers || totalHearts >= 10) {
            String healthText = String.format("%.1f/%.1f", health, maxHealth);
            int textX = x + totalHearts * 9 + 4;
            graphics.drawString(Minecraft.getInstance().font, healthText, textX, y, 0xFFFFFF);
        }
    }
    
    private enum HeartType {
        CONTAINER, FULL, HALF
    }
    
    private static void drawHeartIcon(GuiGraphics graphics, int x, int y, HeartType type) {
        // Draw simple heart using filled rectangles in heart shape
        switch (type) {
            case CONTAINER -> {
                // Gray heart outline
                graphics.fill(x + 1, y + 1, x + 3, y + 2, 0xFF555555);
                graphics.fill(x + 5, y + 1, x + 7, y + 2, 0xFF555555);
                graphics.fill(x, y + 2, x + 8, y + 6, 0xFF555555);
                graphics.fill(x + 1, y + 6, x + 7, y + 7, 0xFF555555);
                graphics.fill(x + 2, y + 7, x + 6, y + 8, 0xFF555555);
                graphics.fill(x + 3, y + 8, x + 5, y + 9, 0xFF555555);
            }
            case FULL -> {
                // Red filled heart
                graphics.fill(x + 1, y + 1, x + 3, y + 2, 0xFFFF4444);
                graphics.fill(x + 5, y + 1, x + 7, y + 2, 0xFFFF4444);
                graphics.fill(x, y + 2, x + 8, y + 6, 0xFFFF4444);
                graphics.fill(x + 1, y + 6, x + 7, y + 7, 0xFFFF4444);
                graphics.fill(x + 2, y + 7, x + 6, y + 8, 0xFFFF4444);
                graphics.fill(x + 3, y + 8, x + 5, y + 9, 0xFFFF4444);
            }
            case HALF -> {
                // Half red heart
                graphics.fill(x + 1, y + 1, x + 3, y + 2, 0xFFAA2222);
                graphics.fill(x, y + 2, x + 4, y + 6, 0xFFAA2222);
                graphics.fill(x + 1, y + 6, x + 4, y + 7, 0xFFAA2222);
                graphics.fill(x + 2, y + 7, x + 4, y + 8, 0xFFAA2222);
                graphics.fill(x + 3, y + 8, x + 4, y + 9, 0xFFAA2222);
            }
        }
    }
    
    /**
     * Draws a smooth animated health bar
     */
    public static void drawHealthBar(GuiGraphics graphics, float health, float maxHealth, 
                                   int x, int y, int width, int height, String animKey) {
        float healthPercent = maxHealth > 0 ? health / maxHealth : 0;
        float animatedPercent = AnimationHelper.animate(animKey, healthPercent, 0.05f, 1.0f);
        
        // Background
        drawRoundedPanel(graphics, x - 1, y - 1, width + 2, height + 2, 0.8f, true, false);
        
        // Health fill
        if (animatedPercent > 0) {
            int fillWidth = (int) (width * animatedPercent);
            int healthColor = getHealthColor(healthPercent);
            
            // Add pulsing effect for low health
            if (healthPercent < 0.25f) {
                float pulse = AnimationHelper.pulse(animKey + "_pulse", 1.0f, 0.3f, 0.003f);
                healthColor = blendColors(healthColor, 0xFFFFFFFF, pulse * 0.3f);
            }
            
            graphics.fill(x, y, x + fillWidth, y + height, healthColor);
            
            // Shine effect
            int shineColor = blendColors(healthColor, 0xFFFFFFFF, 0.4f);
            graphics.fill(x, y, x + fillWidth, y + 1, shineColor);
        }
    }
    
    /**
     * Renders entity head with proper scaling and fallback to colored icon
     */
    public static void renderEntityHead(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        try {
            // Try to render actual entity head
            renderEntity3D(graphics, entity, x, y, size);
        } catch (Exception e) {
            // Fallback to colored icon with entity initial
            renderEntityIcon(graphics, entity, x, y, size);
        }
    }
    
    private static void renderEntity3D(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        // For now, use the icon fallback - actual 3D rendering is complex
        renderEntityIcon(graphics, entity, x, y, size);
    }
    
    private static void renderEntityIcon(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        String entityName = entity.getType().toString();
        int baseColor = Math.abs(entityName.hashCode());
        
        // Create a nice color palette based on entity type
        int hue = baseColor % 360;
        int color = hsvToRgb(hue, 0.7f, 0.9f) | 0xFF000000;
        
        // Background panel
        drawRoundedPanel(graphics, x - 1, y - 1, size + 2, size + 2, 0.9f, true, true);
        
        // Entity color
        graphics.fill(x, y, x + size, y + size, color);
        
        // Entity initial
        String initial = getEntityInitial(entity);
        int textColor = isColorDark(color) ? 0xFFFFFFFF : 0xFF000000;
        graphics.drawCenteredString(Minecraft.getInstance().font, initial, 
            x + size / 2, y + size / 2 - 4, textColor);
        
        // Health indicator (small border)
        float healthPercent = entity.getHealth() / entity.getMaxHealth();
        int healthColor = getHealthColor(healthPercent);
        int borderSize = Math.max(1, size / 16);
        
        // Top border shows health
        graphics.fill(x, y, x + (int)(size * healthPercent), y + borderSize, healthColor);
    }
    
    /**
     * Draws an advanced progress bar with smooth animation and effects
     */
    public static void drawAdvancedProgressBar(GuiGraphics graphics, float progress, int x, int y, 
                                             int width, int height, int baseColor, String animKey, 
                                             boolean showText, String text) {
        float animatedProgress = AnimationHelper.animateEased(animKey, progress, 300, AnimationHelper.EaseType.EASE_OUT);
        
        // Background panel
        drawRoundedPanel(graphics, x - 1, y - 1, width + 2, height + 2, 0.8f, true, false);
        
        // Progress fill
        if (animatedProgress > 0) {
            int fillWidth = (int) (width * animatedProgress);
            
            // Dynamic color based on progress
            int progressColor = progress > 0.5f ? baseColor : 
                               lerpColor(0xFFFF4444, baseColor, progress * 2);
            
            // Add glow effect for full bars
            if (progress > 0.95f) {
                float glow = AnimationHelper.pulse(animKey + "_glow", 1.0f, 0.2f, 0.002f);
                progressColor = blendColors(progressColor, 0xFFFFFFFF, glow * 0.3f);
            }
            
            graphics.fill(x, y, x + fillWidth, y + height, progressColor);
            
            // Shine effect
            int shineColor = blendColors(progressColor, 0xFFFFFFFF, 0.5f);
            graphics.fill(x, y, x + fillWidth, y + Math.max(1, height / 3), shineColor);
        }
        
        // Text overlay
        if (showText && text != null) {
            int textColor = 0xFFFFFFFF;
            int textX = x + width / 2 - Minecraft.getInstance().font.width(text) / 2;
            int textY = y + height / 2 - 4;
            
            // Text shadow
            graphics.drawString(Minecraft.getInstance().font, text, textX + 1, textY + 1, 0xFF000000);
            graphics.drawString(Minecraft.getInstance().font, text, textX, textY, textColor);
        }
    }
    
    /**
     * Renders an item with durability overlay
     */
    public static void drawItemWithDurability(GuiGraphics graphics, ItemStack item, int x, int y, 
                                            float scale, String animKey) {
        if (item.isEmpty()) return;
        
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0f);
        
        int scaledX = (int) (x / scale);
        int scaledY = (int) (y / scale);
        
        // Draw item
        graphics.renderItem(item, scaledX, scaledY);
        
        // Durability bar if applicable
        if (item.isDamageableItem()) {
            float durability = 1.0f - ((float) item.getDamageValue() / item.getMaxDamage());
            int barWidth = (int) (16 * scale);
            int barHeight = (int) (2 * scale);
            int barX = scaledX;
            int barY = scaledY + (int) (14 * scale);
            
            drawAdvancedProgressBar(graphics, durability, barX, barY, barWidth, barHeight, 
                                  getDurabilityColor(durability), animKey + "_durability", false, null);
        }
        
        graphics.pose().popPose();
    }
    
    // Utility methods
    
    private static String getEntityInitial(LivingEntity entity) {
        String name = entity.getType().toString();
        String[] parts = name.split("[.:]");
        String simpleName = parts[parts.length - 1];
        return simpleName.length() > 0 ? simpleName.substring(0, 1).toUpperCase() : "?";
    }
    
    private static int hsvToRgb(int h, float s, float v) {
        float c = v * s;
        float x = c * (1 - Math.abs(((h / 60.0f) % 2) - 1));
        float m = v - c;
        
        float r, g, b;
        if (h < 60) { r = c; g = x; b = 0; }
        else if (h < 120) { r = x; g = c; b = 0; }
        else if (h < 180) { r = 0; g = c; b = x; }
        else if (h < 240) { r = 0; g = x; b = c; }
        else if (h < 300) { r = x; g = 0; b = c; }
        else { r = c; g = 0; b = x; }
        
        return ((int)((r + m) * 255) << 16) | ((int)((g + m) * 255) << 8) | (int)((b + m) * 255);
    }
    
    private static boolean isColorDark(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return (r + g + b) / 3 < 128;
    }
    
    public static int getHealthColor(float healthPercent) {
        if (healthPercent > 0.75f) return 0xFF44FF44; // Bright green
        else if (healthPercent > 0.5f) return lerpColor(0xFFFFFF44, 0xFF44FF44, (healthPercent - 0.5f) * 4);
        else if (healthPercent > 0.25f) return lerpColor(0xFFFF8844, 0xFFFFFF44, (healthPercent - 0.25f) * 4);
        else return lerpColor(0xFFFF4444, 0xFFFF8844, healthPercent * 4);
    }
    
    public static int getDurabilityColor(float durabilityPercent) {
        if (durabilityPercent > 0.75f) return 0xFF44FF44;
        else if (durabilityPercent > 0.5f) return 0xFFFFFF44;
        else if (durabilityPercent > 0.25f) return 0xFFFF8844;
        else return 0xFFFF4444;
    }
    
    public static int lerpColor(int color1, int color2, float t) {
        t = Mth.clamp(t, 0.0f, 1.0f);
        int r1 = (color1 >> 16) & 0xFF, g1 = (color1 >> 8) & 0xFF, b1 = color1 & 0xFF;
        int r2 = (color2 >> 16) & 0xFF, g2 = (color2 >> 8) & 0xFF, b2 = color2 & 0xFF;
        int r = (int) Mth.lerp(t, r1, r2), g = (int) Mth.lerp(t, g1, g2), b = (int) Mth.lerp(t, b1, b2);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
    
    public static int blendColors(int color1, int color2, float factor) {
        return lerpColor(color1, color2, factor);
    }
}
