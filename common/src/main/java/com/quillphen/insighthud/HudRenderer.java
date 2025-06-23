package com.quillphen.insighthud;

import com.quillphen.insighthud.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.Horse;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


/**
 * Polished HUD rendering with modern visuals, smooth animations, and vanilla-style design
 * Features pixel-perfect positioning, smooth transitions, and intuitive user-friendly design
 */
public class HudRenderer {    private static final Minecraft minecraft = Minecraft.getInstance();
    
    // Animation and timing
    private static long lastUpdateTime = 0;
    private static LivingEntity lastTargetedEntity = null;
    private static float entityFadeAlpha = 0.0f;
    private static boolean isTargetingEntity = false;
    
    public static void init() {
        // Initialize rendering system
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public static void renderHud(GuiGraphics graphics, float partialTick) {
        if (!shouldRenderHud()) {
            return;
        }
        
        InsightHudConfig.ConfigData config = InsightHudConfig.getConfig();
        
        // Update timing for smooth animations
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;
        
        // Enhanced entity HUD - repositioned to TOP CENTER
        if (config.enableEntityHud) {
            renderPolishedEntityHud(graphics, partialTick, deltaTime, config);
        }
        
        // Enhanced gear HUD - repositioned to BOTTOM RIGHT
        if (config.enableGearHud) {
            renderPolishedGearHud(graphics, partialTick, deltaTime, config);
        }
    }
      private static boolean shouldRenderHud() {
        // Don't render if GUI is open (except our config screen)
        if (minecraft.screen != null && 
            !(minecraft.screen instanceof com.quillphen.insighthud.config.ConfigScreen)) {
            return false;
        }
        
        // Don't render in spectator mode
        if (minecraft.player != null && minecraft.player.isSpectator()) {
            return false;
        }
        
        return minecraft.player != null && minecraft.level != null;
    }
      private static void renderPolishedEntityHud(GuiGraphics graphics, float partialTick, float deltaTime, InsightHudConfig.ConfigData config) {
        LivingEntity targetEntity = EntityUtil.getTargetedEntity();
        
        // Smooth targeting detection with animations
        boolean hasTarget = targetEntity != null && EntityUtil.isValidTarget(targetEntity);
          // Handle target changes with smooth transitions
        if (hasTarget && targetEntity != lastTargetedEntity) {
            lastTargetedEntity = targetEntity;
            isTargetingEntity = true;
        } else if (!hasTarget && isTargetingEntity) {
            isTargetingEntity = false;
        }
        
        // Smooth fade animation
        float targetAlpha = hasTarget ? 1.0f : 0.0f;
        entityFadeAlpha = Mth.lerp(deltaTime * 8.0f, entityFadeAlpha, targetAlpha);
          if (entityFadeAlpha > 0.01f && lastTargetedEntity != null) {
            // Calculate positioning - TRUE TOP CENTER with reduced blank space
            int screenWidth = minecraft.getWindow().getGuiScaledWidth();
            int screenHeight = minecraft.getWindow().getGuiScaledHeight();
            
            int panelWidth = 240; // Reduced width for tighter layout
            int panelHeight = 52;  // Shrunk height to minimize blank space

            // Position at center of screen with minimal margins
            int x = (screenWidth - panelWidth) / 2; // Center horizontally
            int y = 4; // Top center with minimal margin

            // Apply user positioning only if they've moved it significantly from defaults
            if (Math.abs(config.entityHudX - 10) > 5 || Math.abs(config.entityHudY - 10) > 5) {
                x = Math.max(5, Math.min(config.entityHudX, screenWidth - panelWidth - 5));
                y = Math.max(5, Math.min(config.entityHudY, screenHeight - panelHeight - 5));
            }
            
            renderEntityPanel(graphics, lastTargetedEntity, x, y, panelWidth, panelHeight, entityFadeAlpha, config);
        }    }      private static void renderPolishedGearHud(GuiGraphics graphics, float partialTick, float deltaTime, InsightHudConfig.ConfigData config) {
        Player player = minecraft.player;
        if (player == null) return;
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int panelWidth = 160;   // Increased width for better readability
        int panelHeight = 130;  // Increased height for better spacing

        // Position at bottom right with appropriate margin for larger size
        int x = screenWidth - panelWidth - 12;   // Slightly more margin for larger panel
        int y = screenHeight - panelHeight - 12; // Slightly more margin from bottom
        
        // Apply user positioning only if they've moved it significantly from defaults
        if (Math.abs(config.gearHudX - 10) > 5 || Math.abs(config.gearHudY - 50) > 5) {
            x = Math.max(5, Math.min(config.gearHudX, screenWidth - panelWidth - 5));
            y = Math.max(5, Math.min(config.gearHudY, screenHeight - panelHeight - 5));
        }
        
        renderGearPanel(graphics, player, x, y, panelWidth, panelHeight, 1.0f, config);
    }
      private static void renderEntityPanel(GuiGraphics graphics, LivingEntity entity, int x, int y, 
                                        int width, int height, float alpha, InsightHudConfig.ConfigData config) {
        Font font = minecraft.font;
        
        // Remove panel background for transparent mob HUD
        // drawModernPanel(graphics, x, y, width, height, alpha);
        
        // Entity icon area - larger and better positioned
        int iconSize = 26; // Slightly larger for better visibility
        int iconX = x + 6;
        int iconY = y + 6;
          // Draw enhanced icon background - DISABLED for transparent background
        // drawEnhancedIconBackground(graphics, iconX - 1, iconY - 1, iconSize + 2, 0x404040);
        
        // Render enhanced mob icon
        renderSimpleMobIcon(graphics, entity, iconX, iconY, iconSize);
        
        // Entity name with enhanced styling - better positioned
        String entityName = entity.getDisplayName().getString();
        if (entityName.length() > 22) {
            entityName = entityName.substring(0, 19) + "...";
        }
        
        int nameX = iconX + iconSize + 8;
        int nameY = iconY + 2;
        
        // Enhanced text rendering with shadow and better color
        int nameColor = ((int) (alpha * 255) << 24) | 0xFFFFFF;
        graphics.drawString(font, entityName, nameX + 1, nameY + 1, 0x80000000); // Shadow
        graphics.drawString(font, entityName, nameX, nameY, nameColor);
          // Enhanced health display with reduced blank space
        if (config.showEntityHealth) {
            float health = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            
            int healthY = nameY + 10; // Reduced spacing to minimize blank space
            
            // Numerical health display
            String healthText = String.format("%.0f / %.0f HP", health, maxHealth);
            graphics.drawString(font, healthText, nameX, healthY, ((int) (alpha * 255) << 24) | 0xFFFFFF);
            
            // Enhanced health bar - positioned tighter with minimal spacing
            int barStartX = nameX;
            int barY = healthY + 9; // Reduced spacing for tighter layout
            int maxBarWidth = x + width - barStartX - 6; // Reduced margin
            
            drawAdvancedHealthBar(graphics, health, maxHealth, barStartX, barY, maxBarWidth, alpha);
        }
        
        // Enhanced armor display - positioned in bottom left
        if (config.showEntityArmor && entity.getArmorValue() > 0) {
            int armorValue = entity.getArmorValue();
            String armorText = "⛨ " + armorValue;
            graphics.drawString(font, armorText, iconX, y + height - 12, ((int) (alpha * 255) << 24) | 0xAAAAFF);
        }
        
        // Status effects display - positioned in bottom right
        if (config.showEntityEffects && !entity.getActiveEffects().isEmpty()) {
            int effectCount = Math.min(entity.getActiveEffects().size(), 5);            String effectText = "✦ " + effectCount + " effect" + (effectCount != 1 ? "s" : "");
            int effectWidth = font.width(effectText);
            graphics.drawString(font, effectText, x + width - effectWidth - 6, y + height - 12, 
                              ((int) (alpha * 255) << 24) | 0x88FF88);
        }
    }
      private static void renderGearPanel(GuiGraphics graphics, Player player, int x, int y, 
                                      int width, int height, float alpha, InsightHudConfig.ConfigData config) {
        Font font = minecraft.font;
          // Enhanced panel background with better proportions
        drawModernPanel(graphics, x, y, width, height, alpha);
        
        // Title with improved positioning and size
        String title = "Gear Status";
        int titleX = x + 10;
        graphics.drawString(font, title, titleX + 1, y + 6 + 1, 0x80000000); // Shadow
        graphics.drawString(font, title, titleX, y + 6, 0xFFFFFFFF);
        // Divider under title with better proportions
        graphics.fill(x + 10, y + 18, x + width - 10, y + 19, ((int)(alpha * 120) << 24) | 0xFFFFFF);
        ItemStack[] gearItems = {
            player.getInventory().getItem(39), // Helmet (slot 39)
            player.getInventory().getItem(38), // Chestplate (slot 38) 
            player.getInventory().getItem(37), // Leggings (slot 37)
            player.getInventory().getItem(36), // Boots (slot 36)
            player.getMainHandItem(),           // Main hand
            player.getOffhandItem()             // Offhand
        };        String[] gearNames = {"Helmet", "Chest", "Legs", "Boots", "Main", "Off"};
        int startY = y + 24; // Better spacing from title
        int slotHeight = 16; // Increased height for better readability
        
        for (int i = 0; i < gearItems.length; i++) {
            ItemStack item = gearItems[i];
            int slotY = startY + i * slotHeight;
            
            if (!item.isEmpty()) {
                // Render item icon with better positioning
                graphics.renderItem(item, x + 6, slotY + 1);
                  // Item name with improved truncation and spacing
                String itemName = item.getDisplayName().getString();
                
                // Remove square brackets from item names
                itemName = itemName.replace("[", "").replace("]", "");
                
                // Check if item is enchanted for separate rendering
                boolean isEnchanted = item.isEnchanted();
                
                if (itemName.length() > 18) { // Increased limit for larger panel
                    itemName = itemName.substring(0, 15) + "...";
                }
                
                int nameX = x + 22; // Better spacing for item icon
                int nameY = slotY + 5; // Better vertical alignment
                // Draw item name with shadow for clarity
                graphics.drawString(font, itemName, nameX + 1, nameY + 1, 0x80000000);
                graphics.drawString(font, itemName, nameX, nameY, 0xFFFFFFFF);

                // Enchantment indicator with purple glittery effect (separate from name)
                if (isEnchanted) {
                    int enchantX = nameX + font.width(itemName) + 4; // Position after item name
                    // Purple glittery color for enchantment star
                    graphics.drawString(font, "✦", enchantX + 1, nameY + 1, 0x80000000); // Shadow
                    graphics.drawString(font, "✦", enchantX, nameY, 0xFFAA00FF); // Purple color
                }

                // Durability display with improved layout
                if (item.isDamageableItem()) {
                    int currentDurability = item.getMaxDamage() - item.getDamageValue();
                    int maxDurability = item.getMaxDamage();
                    float durabilityPercent = (float) currentDurability / maxDurability;
                      // Durability bar with better proportions
                    int barWidth = 30; // Increased bar width for better visibility
                    int barX = x + width - barWidth - 6;
                    int barY = slotY + 2; // Better positioning
                    drawCompactDurabilityBar(graphics, durabilityPercent, barX, barY, barWidth, 4, alpha);
                    
                    // Percentage text with better positioning
                    String percentText = String.format("%.0f%%", durabilityPercent * 100);
                    int percentWidth = font.width(percentText);
                    int percentX = barX + (barWidth - percentWidth) / 2;
                    int percentColor = getDurabilityColor(durabilityPercent);
                    graphics.drawString(font, percentText, percentX, slotY + 8, percentColor);
                } else {
                    // Non-damageable item indicator with better positioning
                    graphics.drawString(font, "∞", x + width - 16, slotY + 5, 0xFF88FF88);
                }
            } else {
                // Empty slot with better alignment and spacing
                String slotText = gearNames[i];
                int emptyNameY = slotY + 5; // Better alignment
                graphics.drawString(font, slotText, x + 22, emptyNameY, 0xFF666666);
                graphics.drawString(font, "(Empty)", x + width - 48, emptyNameY, 0xFF444444);
            }
        }
    }
    
    private static void drawCompactDurabilityBar(GuiGraphics graphics, float percent, int x, int y, int width, int height, float alpha) {
        // Background
        int bgColor = ((int) (alpha * 120) << 24) | 0x333333;
        graphics.fill(x, y, x + width, y + height, bgColor);
        
        // Fill
        int fillWidth = (int) (width * percent);
        int fillColor = getDurabilityColor(percent);
        fillColor = ((int) (alpha * 255) << 24) | (fillColor & 0xFFFFFF);
        
        if (fillWidth > 0) {
            graphics.fill(x, y, x + fillWidth, y + height, fillColor);
        }
        
        // Subtle border
        int borderColor = ((int) (alpha * 100) << 24) | 0x666666;
        graphics.fill(x, y, x + width, y + 1, borderColor); // Top
        graphics.fill(x, y + height - 1, x + width, y + height, borderColor); // Bottom
    }
    
    // Helper methods for enhanced rendering
    private static void drawModernPanel(GuiGraphics graphics, int x, int y, int width, int height, float alpha) {
        int bgAlpha = (int) (alpha * 200);
        int borderAlpha = (int) (alpha * 100);
        
        // Background with gradient effect
        int bgColorTop = (bgAlpha << 24) | 0x1a1a1a;
        int bgColorBottom = (bgAlpha << 24) | 0x0a0a0a;
        
        drawGradientRect(graphics, x, y, x + width, y + height, bgColorTop, bgColorBottom);
        
        // Subtle border
        int borderColor = (borderAlpha << 24) | 0x555555;
        graphics.fill(x, y, x + width, y + 1, borderColor); // Top
        graphics.fill(x, y + height - 1, x + width, y + height, borderColor); // Bottom
        graphics.fill(x, y, x + 1, y + height, borderColor); // Left
        graphics.fill(x + width - 1, y, x + width, y + height, borderColor); // Right
    }
      private static void drawGradientRect(GuiGraphics graphics, int x1, int y1, int x2, int y2, int colorTop, int colorBottom) {
        // Simplified gradient using GuiGraphics methods
        graphics.fillGradient(x1, y1, x2, y2, colorTop, colorBottom);
    }    private static void drawAdvancedHealthBar(GuiGraphics graphics, float health, float maxHealth, 
                                            int x, int y, int maxWidth, float alpha) {
        // Draw heart-style health display instead of traditional bar
        drawHeartStyleHealth(graphics, health, maxHealth, x, y, maxWidth, alpha);
    }      private static void drawHeartStyleHealth(GuiGraphics graphics, float health, float maxHealth, 
                                           int x, int y, int maxWidth, float alpha) {
        // Calculate heart display parameters with proper spacing like Minecraft
        int heartSpacing = 10; // Increased spacing between hearts like in Minecraft
        int maxHeartsPerRow = maxWidth / heartSpacing;
        int totalHearts = (int) Math.ceil(maxHealth / 2.0f); // Each heart = 2 HP
        
        // Limit display to reasonable number of hearts
        if (totalHearts > 15) { // Reduced from 20 to 15 for better display
            // For high-health entities, show a condensed version
            drawCondensedHealthBar(graphics, health, maxHealth, x, y, maxWidth, alpha);
            return;
        }
        
        int fullHearts = (int) (health / 2.0f);
        boolean hasHalfHeart = (health % 2.0f) >= 1.0f;
        
        // Draw hearts row by row with proper spacing
        for (int i = 0; i < totalHearts; i++) {
            int row = i / maxHeartsPerRow;
            int col = i % maxHeartsPerRow;
            
            int heartX = x + col * heartSpacing;
            int heartY = y + row * 12; // Increased row spacing
            
            // Draw heart background (empty heart) using Minecraft GUI texture
            drawMinecraftHeart(graphics, heartX, heartY, HeartType.EMPTY, alpha);
            
            // Draw heart fill based on health
            if (i < fullHearts) {
                drawMinecraftHeart(graphics, heartX, heartY, HeartType.FULL, alpha);
            } else if (i == fullHearts && hasHalfHeart) {
                drawMinecraftHeart(graphics, heartX, heartY, HeartType.HALF, alpha);
            }
        }
    }
    
    private static void drawCondensedHealthBar(GuiGraphics graphics, float health, float maxHealth, 
                                             int x, int y, int width, float alpha) {
        // For high-health entities, use a traditional bar with hearts on the ends
        float healthPercent = Mth.clamp(health / maxHealth, 0.0f, 1.0f);
          // Draw heart icons at the ends using Minecraft textures
        drawMinecraftHeart(graphics, x - 12, y, HeartType.FULL, alpha);
        
        // Background bar
        int bgColor = ((int) (alpha * 120) << 24) | 0x222222;
        graphics.fill(x, y + 2, x + width, y + 7, bgColor);
        
        // Health bar with color coding
        int healthWidth = (int) (width * healthPercent);
        int healthColor = getHealthColor(healthPercent);
        healthColor = ((int) (alpha * 255) << 24) | (healthColor & 0xFFFFFF);
        
        if (healthWidth > 0) {
            graphics.fill(x, y + 2, x + healthWidth, y + 7, healthColor);
        }
        
        // Border
        int borderColor = ((int) (alpha * 150) << 24) | 0x888888;
        graphics.fill(x, y + 2, x + width, y + 3, borderColor); // Top
        graphics.fill(x, y + 6, x + width, y + 7, borderColor); // Bottom
        graphics.fill(x, y + 2, x + 1, y + 7, borderColor); // Left
        graphics.fill(x + width - 1, y + 2, x + width, y + 7, borderColor); // Right
    }
      private enum HeartType {
        EMPTY, HALF, FULL
    }    private static void drawMinecraftHeart(GuiGraphics graphics, int x, int y, HeartType type, float alpha) {
        // Simple reliable heart rendering using filled rectangles
        // This avoids all texture loading issues and always works
        
        boolean hardcore = minecraft.level != null && minecraft.level.getLevelData().isHardcore();
          // Choose colors based on heart type and hardcore mode
        int heartColor = 0xFF666666; // Default color (gray)
        
        if (hardcore) {
            // Golden hearts for hardcore mode
            switch (type) {
                case EMPTY -> heartColor = 0x80333333; // Dark gray for empty
                case HALF -> heartColor = 0xFFFFCC00;  // Half gold
                case FULL -> heartColor = 0xFFFFD700;  // Full gold
            }
        } else {
            // Red hearts for normal mode
            switch (type) {
                case EMPTY -> heartColor = 0x80333333; // Dark gray for empty
                case HALF -> heartColor = 0xFFCC0000;  // Half red
                case FULL -> heartColor = 0xFFFF0000;  // Full red
            }
        }
        
        // Draw a simple heart shape using rectangles
        if (type != HeartType.EMPTY) {
            // Draw filled heart shape
            // Top bumps
            graphics.fill(x + 1, y, x + 4, y + 2, heartColor);
            graphics.fill(x + 5, y, x + 8, y + 2, heartColor);
            // Middle section
            graphics.fill(x, y + 2, x + 9, y + 5, heartColor);
            // Bottom point
            graphics.fill(x + 1, y + 5, x + 8, y + 6, heartColor);
            graphics.fill(x + 2, y + 6, x + 7, y + 7, heartColor);
            graphics.fill(x + 3, y + 7, x + 6, y + 8, heartColor);
            graphics.fill(x + 4, y + 8, x + 5, y + 9, heartColor);
            
            // For half hearts, cover the right half with empty color
            if (type == HeartType.HALF) {
                int emptyColor = 0x80333333;
                graphics.fill(x + 4, y, x + 8, y + 2, emptyColor);
                graphics.fill(x + 4, y + 2, x + 9, y + 5, emptyColor);
                graphics.fill(x + 5, y + 5, x + 8, y + 6, emptyColor);
                graphics.fill(x + 5, y + 6, x + 7, y + 7, emptyColor);
                graphics.fill(x + 5, y + 7, x + 6, y + 8, emptyColor);
            }
        } else {
            // Draw empty heart outline
            int outlineColor = 0xFF666666;
            // Draw outline only
            graphics.fill(x + 1, y, x + 8, y + 1, outlineColor); // Top
            graphics.fill(x, y + 1, x + 1, y + 5, outlineColor); // Left
            graphics.fill(x + 8, y + 1, x + 9, y + 5, outlineColor); // Right
            graphics.fill(x + 1, y + 8, x + 5, y + 9, outlineColor); // Bottom
        }
    }private static void renderSimpleMobIcon(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        // Try to render using actual Minecraft mob head textures first
        if (renderMobHeadTexture(graphics, entity, x, y, size)) {
            // Successfully rendered head texture, add border
            drawIconBorder(graphics, x, y, size);
            return;
        }
        
        // If head texture failed, use pixel art icons
        drawPixelArtMobIcon(graphics, entity, x, y, size);
        drawIconBorder(graphics, x, y, size);
    }private static boolean renderMobHeadTexture(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        // Try to get the appropriate head item for this entity
        ItemStack headItem = getMobHeadItem(entity);
        
        if (!headItem.isEmpty()) {
            try {
                // Render the head item as an icon
                graphics.pose().pushPose();
                float scale = (float) size / 16.0f; // Scale to desired size (16 is default item size)
                graphics.pose().scale(scale, scale, 1.0f);
                
                int scaledX = (int) (x / scale);
                int scaledY = (int) (y / scale);
                
                // Ensure we have a proper rendering context
                if (minecraft.getItemRenderer() != null) {
                    graphics.renderItem(headItem, scaledX, scaledY);
                    graphics.pose().popPose();
                    return true;
                }
            } catch (Exception e) {
                // If item rendering fails, fall back to other methods
                graphics.pose().popPose();
            }
        }
        
        // If head item rendering failed, use pixel art approach
        return false;
    }
      private static ItemStack getMobHeadItem(LivingEntity entity) {
        EntityType<?> entityType = entity.getType();
        
        // Return appropriate head items using direct EntityType comparison
        if (entityType == EntityType.ZOMBIE || entityType == EntityType.ZOMBIE_VILLAGER || entityType == EntityType.HUSK || entityType == EntityType.DROWNED) {
            return new ItemStack(net.minecraft.world.item.Items.ZOMBIE_HEAD);
        } else if (entityType == EntityType.SKELETON || entityType == EntityType.STRAY) {
            return new ItemStack(net.minecraft.world.item.Items.SKELETON_SKULL);
        } else if (entityType == EntityType.CREEPER) {
            return new ItemStack(net.minecraft.world.item.Items.CREEPER_HEAD);
        } else if (entityType == EntityType.WITHER_SKELETON) {
            return new ItemStack(net.minecraft.world.item.Items.WITHER_SKELETON_SKULL);
        } else if (entityType == EntityType.ENDER_DRAGON) {
            return new ItemStack(net.minecraft.world.item.Items.DRAGON_HEAD);
        } else if (entityType == EntityType.PIGLIN || entityType == EntityType.ZOMBIFIED_PIGLIN) {
            return new ItemStack(net.minecraft.world.item.Items.PIGLIN_HEAD);
        }
        
        // For animals and other mobs, use representative items
        if (entityType == EntityType.PIG) {
            return new ItemStack(net.minecraft.world.item.Items.PORKCHOP);
        } else if (entityType == EntityType.COW) {
            return new ItemStack(net.minecraft.world.item.Items.BEEF);
        } else if (entityType == EntityType.SHEEP) {
            return new ItemStack(net.minecraft.world.item.Items.WHITE_WOOL);
        } else if (entityType == EntityType.CHICKEN) {
            return new ItemStack(net.minecraft.world.item.Items.FEATHER);
        } else if (entityType == EntityType.SPIDER || entityType == EntityType.CAVE_SPIDER) {
            return new ItemStack(net.minecraft.world.item.Items.SPIDER_EYE);
        } else if (entityType == EntityType.ENDERMAN) {
            return new ItemStack(net.minecraft.world.item.Items.ENDER_PEARL);
        } else if (entityType == EntityType.BLAZE) {
            return new ItemStack(net.minecraft.world.item.Items.BLAZE_ROD);
        } else if (entityType == EntityType.WITCH) {
            return new ItemStack(net.minecraft.world.item.Items.POTION);
        } else if (entityType == EntityType.SLIME || entityType == EntityType.MAGMA_CUBE) {
            return new ItemStack(net.minecraft.world.item.Items.SLIME_BALL);
        } else if (entityType == EntityType.GHAST) {
            return new ItemStack(net.minecraft.world.item.Items.GHAST_TEAR);
        } else if (entityType == EntityType.VILLAGER) {
            return new ItemStack(net.minecraft.world.item.Items.EMERALD);
        }
          return ItemStack.EMPTY;
    }
      private static void drawPixelArtMobIcon(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        EntityType<?> entityType = entity.getType();
        
        // Create pixel-perfect 32x32 icons that match Minecraft's style
        int[][] iconPattern;
        int primaryColor, secondaryColor;
        
        if (entityType == EntityType.CREEPER) {
            primaryColor = 0xFF00AA00;  // Green
            secondaryColor = 0xFF004400; // Dark green
            iconPattern = getCreeperPattern();
        } else if (entityType == EntityType.SKELETON || entityType == EntityType.STRAY) {
            primaryColor = 0xFFFFFFFF;  // White
            secondaryColor = 0xFFCCCCCC; // Light gray
            iconPattern = getSkeletonPattern();
        } else if (entityType == EntityType.ZOMBIE || entityType == EntityType.ZOMBIE_VILLAGER || entityType == EntityType.HUSK || entityType == EntityType.DROWNED) {
            primaryColor = 0xFF00AA00;  // Green skin
            secondaryColor = 0xFF004400; // Dark green
            iconPattern = getZombiePattern();
        } else if (entityType == EntityType.SPIDER || entityType == EntityType.CAVE_SPIDER) {
            primaryColor = 0xFF330000;  // Dark red
            secondaryColor = 0xFF660000; // Red
            iconPattern = getSpiderPattern();
        } else if (entityType == EntityType.ENDERMAN) {
            primaryColor = 0xFF000000;  // Black
            secondaryColor = 0xFF444444; // Dark gray
            iconPattern = getEndermanPattern();
        } else {
            // Default pattern for unknown entities
            primaryColor = 0xFF888888;
            secondaryColor = 0xFF444444;
            iconPattern = getDefaultPattern();
        }
        
        // Render the pixel pattern
        renderPixelPattern(graphics, iconPattern, x, y, size, primaryColor, secondaryColor);
    }
    
    private static void renderPixelPattern(GuiGraphics graphics, int[][] pattern, int x, int y, int size, int primaryColor, int secondaryColor) {
        int pixelSize = Math.max(1, size / pattern.length);
        
        for (int row = 0; row < pattern.length; row++) {
            for (int col = 0; col < pattern[row].length; col++) {
                if (pattern[row][col] > 0) {
                    int color = pattern[row][col] == 1 ? primaryColor : secondaryColor;
                    int pixelX = x + col * pixelSize;
                    int pixelY = y + row * pixelSize;
                    graphics.fill(pixelX, pixelY, pixelX + pixelSize, pixelY + pixelSize, color);
                }
            }
        }
    }
    
    // Pixel patterns for different mobs (8x8 simplified patterns)
    private static int[][] getCreeperPattern() {
        return new int[][]{
            {0,1,1,1,1,1,1,0},
            {1,1,2,1,1,2,1,1},
            {1,1,2,1,1,2,1,1},
            {1,1,1,2,2,1,1,1},
            {1,1,2,2,2,2,1,1},
            {1,1,2,2,2,2,1,1},
            {1,1,2,1,1,2,1,1},
            {0,1,1,1,1,1,1,0}
        };
    }
    
    private static int[][] getSkeletonPattern() {
        return new int[][]{
            {0,1,1,1,1,1,1,0},
            {1,1,2,1,1,2,1,1},
            {1,1,2,1,1,2,1,1},
            {1,1,1,1,1,1,1,1},
            {1,2,1,1,1,1,2,1},
            {1,1,2,2,2,2,1,1},
            {1,1,1,2,2,1,1,1},
            {0,1,1,1,1,1,1,0}
        };
    }
    
    private static int[][] getZombiePattern() {
        return new int[][]{
            {0,1,1,1,1,1,1,0},
            {1,1,2,1,1,2,1,1},
            {1,1,2,1,1,2,1,1},
            {1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,1},
            {1,1,2,1,1,2,1,1},
            {1,1,1,2,2,1,1,1},
            {0,1,1,1,1,1,1,0}
        };
    }
    
    private static int[][] getSpiderPattern() {
        return new int[][]{
            {2,0,1,1,1,1,0,2},
            {0,2,1,2,2,1,2,0},
            {1,1,2,1,1,2,1,1},
            {1,2,1,1,1,1,2,1},
            {1,1,1,2,2,1,1,1},
            {2,1,1,1,1,1,1,2},
            {0,2,1,1,1,1,2,0},
            {0,0,2,0,0,2,0,0}
        };
    }
    
    private static int[][] getEndermanPattern() {
        return new int[][]{
            {0,1,1,1,1,1,1,0},
            {1,1,2,1,1,2,1,1},
            {1,1,2,1,1,2,1,1},
            {1,1,1,1,1,1,1,1},
            {1,1,1,2,2,1,1,1},
            {1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1},
            {0,1,1,1,1,1,1,0}
        };
    }
    
    private static int[][] getDefaultPattern() {
        return new int[][]{
            {0,1,1,1,1,1,1,0},
            {1,1,1,1,1,1,1,1},
            {1,1,2,1,1,2,1,1},
            {1,1,1,1,1,1,1,1},
            {1,1,1,2,2,1,1,1},
            {1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1},
            {0,1,1,1,1,1,1,0}
        };
    }    private static void drawIconBorder(GuiGraphics graphics, int x, int y, int size) {
        // Enhanced border with depth
        int borderDark = 0xFF333333;
        int borderLight = 0xFF888888;
        
        // Dark border (bottom and right)
        graphics.fill(x, y + size - 1, x + size, y + size, borderDark);
        graphics.fill(x + size - 1, y, x + size, y + size, borderDark);
        
        // Light border (top and left)
        graphics.fill(x, y, x + size - 1, y + 1, borderLight);
        graphics.fill(x, y, x + 1, y + size - 1, borderLight);
    }
    
    private static int darkenColor(int color, float factor) {
        int r = (int) (((color >> 16) & 0xFF) * (1 - factor));
        int g = (int) (((color >> 8) & 0xFF) * (1 - factor));
        int b = (int) ((color & 0xFF) * (1 - factor));
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
    
    private static int lightenColor(int color, float factor) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) * (1 + factor)));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) * (1 + factor)));
        int b = Math.min(255, (int) ((color & 0xFF) * (1 + factor)));
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }    private static MobIconInfo getMobIconInfo(LivingEntity entity) {
        // Simplified for now - just return a basic texture location and coords
        // TODO: Implement proper entity texture mapping
        ResourceLocation defaultTexture = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/zombie/zombie.png");
        HeadTextureCoords defaultCoords = new HeadTextureCoords(8, 8, 8, 8);
        return new MobIconInfo(defaultTexture, defaultCoords);
    }
    
    private static ResourceLocation getEntityTextureLocation(LivingEntity entity) {
        EntityType<?> entityType = entity.getType();
        String entityName = EntityType.getKey(entityType).getPath();
          // Handle special cases and variants
        if (entity instanceof Zombie zombie) {
            if (zombie instanceof ZombieVillager) {
                return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/zombie_villager/zombie_villager.png");
            } else if (zombie instanceof Husk) {
                return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/zombie/husk.png");
            } else if (zombie instanceof Drowned) {
                return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/zombie/drowned.png");
            }
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/zombie/zombie.png");
        }
        
        if (entity instanceof Skeleton skeleton) {
            if (skeleton.getType() == EntityType.WITHER_SKELETON) {
                return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/skeleton/wither_skeleton.png");
            } else if (skeleton.getType() == EntityType.STRAY) {
                return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/skeleton/stray.png");
            }
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/skeleton/skeleton.png");
        }
        
        if (entity instanceof Spider) {
            if (entity instanceof CaveSpider) {
                return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/spider/cave_spider.png");
            }
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/spider/spider.png");
        }
        
        if (entity instanceof Creeper) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/creeper/creeper.png");
        }
        
        if (entity instanceof Witch) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/witch.png");
        }
        
        if (entity instanceof Blaze) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/blaze.png");
        }
        
        if (entity instanceof Ghast) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/ghast/ghast.png");
        }
        
        if (entity instanceof Slime) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/slime/slime.png");
        }
        
        if (entity instanceof MagmaCube) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/slime/magmacube.png");
        }
        
        // Passive mobs
        if (entity instanceof Pig) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/pig/pig.png");
        }
        
        if (entity instanceof Cow) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/cow/cow.png");
        }
        
        if (entity instanceof Chicken) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/chicken.png");
        }
        
        if (entity instanceof Horse) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/horse/horse_white.png");
        }
        
        if (entity instanceof Villager) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/villager/villager.png");
        }
        
        if (entity instanceof Cat) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/cat/tabby.png");
        }
          // Default fallback - try to construct from entity name
        String fallbackEntityName = EntityType.getKey(entityType).getPath();
        return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/" + fallbackEntityName + ".png");
    }
    
    private static HeadTextureCoords getHeadTextureCoords(EntityType<?> entityType) {
        String entityName = EntityType.getKey(entityType).getPath();
        
        // Most humanoid mobs use standard head coordinates
        if (entityName.contains("zombie") || entityName.contains("skeleton") || 
            entityName.contains("villager") || entityName.contains("witch")) {
            return new HeadTextureCoords(8, 8, 8, 8); // x, y, width, height on 64x64 texture
        }
        
        // Spiders have heads at different coordinates
        if (entityName.contains("spider")) {
            return new HeadTextureCoords(32, 4, 8, 8);
        }
        
        // Creeper head
        if (entityName.equals("creeper")) {
            return new HeadTextureCoords(8, 8, 8, 8);
        }
        
        // Enderman head
        if (entityName.equals("enderman")) {
            return new HeadTextureCoords(0, 0, 8, 8);
        }
        
        // Animals typically have heads in different positions
        if (entityName.equals("pig")) {
            return new HeadTextureCoords(8, 8, 8, 8);
        }
        
        if (entityName.equals("cow")) {
            return new HeadTextureCoords(0, 0, 8, 8);
        }
        
        if (entityName.equals("sheep")) {
            return new HeadTextureCoords(0, 0, 8, 8);
        }
        
        if (entityName.equals("chicken")) {
            return new HeadTextureCoords(0, 0, 6, 6);
        }
        
        // Default head coordinates for standard mob layout
        return new HeadTextureCoords(8, 8, 8, 8);
    }    
    private static class MobIconInfo {
        final ResourceLocation textureLocation;
        final HeadTextureCoords coords;
        
        MobIconInfo(ResourceLocation textureLocation, HeadTextureCoords coords) {
            this.textureLocation = textureLocation;
            this.coords = coords;
        }
    }
    
    private static class HeadTextureCoords {
        final int x, y, width, height;
        
        HeadTextureCoords(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
    
    private static int getHealthColor(float healthPercent) {
        if (healthPercent > 0.6f) return 0x55FF55; // Green
        else if (healthPercent > 0.3f) return 0xFFFF55; // Yellow
        else return 0xFF5555; // Red
    }
    
    private static int getDurabilityColor(float durabilityPercent) {
        if (durabilityPercent > 0.6f) return 0xFF55FF55; // Green
        else if (durabilityPercent > 0.3f) return 0xFFFFFF55; // Yellow
        else if (durabilityPercent > 0.1f) return 0xFFFF8855; // Orange
        else return 0xFFFF5555; // Red
    }
}
