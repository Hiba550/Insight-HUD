package com.quillphen.insighthud;

import com.quillphen.insighthud.util.EntityUtil;
import com.quillphen.insighthud.util.AnimationHelper;
import com.quillphen.insighthud.util.RenderHelper;
import com.quillphen.insighthud.util.MobIconRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

/**
 * Enhanced HUD rendering class with polished visuals, smooth animations, and vanilla styling
 */
public class HudRenderer {
    
    private static final Minecraft minecraft = Minecraft.getInstance();
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
        
        // Update timing
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;
        
        // Enhanced entity HUD with smooth animations
        if (config.enableEntityHud) {
            renderAdvancedEntityHud(graphics, partialTick, deltaTime);
        }
        
        // Enhanced gear HUD
        if (config.enableGearHud) {
            renderAdvancedGearHud(graphics, partialTick, deltaTime);
        }
    }
      private static boolean shouldRenderHud() {
        InsightHudConfig.ConfigData config = InsightHudConfig.getConfig();
        
        // Don't render if F3 debug is open and configured to hide
        // TODO: Fix renderDebug field name for 1.21.5+
        // if (config.hideInF3 && minecraft.options.renderDebug) {
        //     return false;
        // }
        
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
      private static void renderAdvancedEntityHud(GuiGraphics graphics, float partialTick, float deltaTime) {
        LivingEntity targetEntity = EntityUtil.getTargetedEntity();
        InsightHudConfig.ConfigData config = InsightHudConfig.getConfig();
        
        // Smooth targeting detection
        boolean hasTarget = targetEntity != null && EntityUtil.isValidTarget(targetEntity);
        
        // Handle target changes with smooth transitions
        if (hasTarget && targetEntity != lastTargetedEntity) {
            lastTargetedEntity = targetEntity;
            isTargetingEntity = true;
            AnimationHelper.reset("entity_appear");
            AnimationHelper.reset("entity_scale");
        } else if (!hasTarget && isTargetingEntity) {
            isTargetingEntity = false;
        }
        
        // Animate appearance with bounce effect
        float targetAlpha = hasTarget ? 1.0f : 0.0f;
        entityFadeAlpha = AnimationHelper.animateEased("entity_fade", targetAlpha, 400, 
                                                     AnimationHelper.EaseType.EASE_OUT_CUBIC);
        
        if (entityFadeAlpha > 0.01f && lastTargetedEntity != null) {
            // Smooth scale animation with slight bounce
            float targetScale = hasTarget ? 1.0f : 0.0f;
            float scale = AnimationHelper.animateEased("entity_scale", targetScale, 300, 
                                                     AnimationHelper.EaseType.BACK);
            
            // Position calculations - better default positioning
            int screenWidth = minecraft.getWindow().getGuiScaledWidth();
            int screenHeight = minecraft.getWindow().getGuiScaledHeight();
            
            int panelWidth = 240;
            int panelHeight = 100;
            
            // Default to top-right corner with proper margins
            int defaultX = screenWidth - panelWidth - 20;
            int defaultY = 20;
            
            int x = Math.max(10, Math.min(config.entityHudX != 10 ? config.entityHudX : defaultX, 
                                         screenWidth - panelWidth - 10));
            int y = Math.max(10, Math.min(config.entityHudY != 10 ? config.entityHudY : defaultY, 
                                         screenHeight - panelHeight - 10));
            
            // Apply transformations
            graphics.pose().pushPose();
            graphics.pose().translate(x + panelWidth / 2f, y + panelHeight / 2f, 0);
            graphics.pose().scale(scale, scale, 1.0f);
            graphics.pose().translate(-panelWidth / 2f, -panelHeight / 2f, 0);
            
            renderPolishedEntityPanel(graphics, lastTargetedEntity, 0, 0, 
                                    panelWidth, panelHeight, entityFadeAlpha, config);
            
            graphics.pose().popPose();
        }
    }
      private static void renderAdvancedGearHud(GuiGraphics graphics, float partialTick, float deltaTime) {
        Player player = minecraft.player;
        if (player == null) return;
        
        InsightHudConfig.ConfigData config = InsightHudConfig.getConfig();
        
        // Smooth gear HUD animation
        float gearAlpha = AnimationHelper.animateEased("gear_fade", 1.0f, 500, 
                                                     AnimationHelper.EaseType.EASE_OUT);
        
        if (gearAlpha > 0.01f) {
            int screenWidth = minecraft.getWindow().getGuiScaledWidth();
            int screenHeight = minecraft.getWindow().getGuiScaledHeight();
            
            int panelWidth = 220;
            int panelHeight = 140;
            
            // Default to bottom-right corner with proper margins
            int defaultX = screenWidth - panelWidth - 20;
            int defaultY = screenHeight - panelHeight - 20;
            
            int x = Math.max(10, Math.min(config.gearHudX != 50 ? config.gearHudX : defaultX, 
                                         screenWidth - panelWidth - 10));
            int y = Math.max(10, Math.min(config.gearHudY != 50 ? config.gearHudY : defaultY, 
                                         screenHeight - panelHeight - 10));
            
            renderPolishedGearPanel(graphics, player, x, y, panelWidth, panelHeight, gearAlpha, config);
        }
    }
    
    private static void renderPolishedEntityPanel(GuiGraphics graphics, LivingEntity entity, int x, int y, 
                                                int width, int height, float alpha, InsightHudConfig.ConfigData config) {
        Font font = minecraft.font;
        
        // Enhanced panel background with glow
        RenderHelper.drawRoundedPanel(graphics, x, y, width, height, alpha * 0.95f, true, true);
        
        // Entity icon with enhanced rendering
        int iconSize = 32;
        int iconX = x + 12;
        int iconY = y + 12;
        
        // Animate icon appearance
        float iconScale = AnimationHelper.spring("entity_icon_scale", 1.0f, 8.0f, 0.8f);
        graphics.pose().pushPose();
        graphics.pose().translate(iconX + iconSize / 2f, iconY + iconSize / 2f, 0);
        graphics.pose().scale(iconScale, iconScale, 1.0f);
        graphics.pose().translate(-iconSize / 2f, -iconSize / 2f, 0);
        
        MobIconRenderer.renderMobIcon(graphics, entity, 0, 0, iconSize);
        
        graphics.pose().popPose();
        
        // Entity name with enhanced styling
        String entityName = entity.getDisplayName().getString();
        float nameAlpha = AnimationHelper.animateEased("entity_name", alpha, 200, 
                                                     AnimationHelper.EaseType.EASE_OUT);
        int nameColor = ((int) (nameAlpha * 255) << 24) | 0xFFFFFF;
        
        int nameX = iconX + iconSize + 12;
        int nameY = iconY + 2;
        
        // Name shadow for better readability
        graphics.drawString(font, entityName, nameX + 1, nameY + 1, 0x80000000);
        graphics.drawString(font, entityName, nameX, nameY, nameColor);
        
        // Enhanced health display
        if (config.showEntityHealth) {
            float health = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            
            int healthY = nameY + 14;
            
            // Choose display type based on max health
            if (maxHealth <= 40) {
                // Show as hearts for reasonable health amounts
                RenderHelper.drawHealthHearts(graphics, health, maxHealth, nameX, healthY, false);
            } else {
                // Show as bar for high health amounts
                int barWidth = width - nameX + x - 12;
                RenderHelper.drawHealthBar(graphics, health, maxHealth, nameX, healthY, 
                                         barWidth, 8, "entity_health_" + entity.getId());
                
                // Health text with enhanced styling
                String healthText = String.format("%.1f / %.1f", health, maxHealth);
                graphics.drawString(font, healthText, nameX, healthY + 12, 0xFFFFFFFF);
            }
        }
        
        // Enhanced armor display
        if (config.showEntityArmor && entity.getArmorValue() > 0) {
            int armorValue = entity.getArmorValue();
            int armorY = iconY + 50;
            
            // Armor bar
            float armorPercent = Math.min(armorValue / 20.0f, 1.0f); // Max 20 armor points
            RenderHelper.drawAdvancedProgressBar(graphics, armorPercent, nameX, armorY, 
                                               width - nameX + x - 12, 6, 0xFFAAAAAA, 
                                               "entity_armor_" + entity.getId(), true, 
                                               "Armor: " + armorValue);
        }
        
        // Enhanced status effects display
        if (config.showEntityEffects && !entity.getActiveEffects().isEmpty()) {
            int effectX = x + 12;
            int effectY = y + height - 24;
            int effectIndex = 0;
            
            for (MobEffectInstance effect : entity.getActiveEffects()) {
                if (effectIndex >= 6) break; // Limit to 6 effects
                
                int effX = effectX + effectIndex * 20;
                
                // Effect icon background
                RenderHelper.drawRoundedPanel(graphics, effX, effectY, 16, 16, 0.9f, false, true);
                
                // Effect color
                int effectColor = effect.getEffect().value().getColor() | 0xFF000000;
                graphics.fill(effX + 2, effectY + 2, effX + 14, effectY + 14, effectColor);
                
                // Effect level indicator
                if (effect.getAmplifier() > 0) {
                    String level = String.valueOf(effect.getAmplifier() + 1);
                    graphics.drawString(font, level, effX + 1, effectY - 8, 0xFFFFFFFF);
                }
                
                effectIndex++;
            }
        }
    }
    
    private static void renderPolishedGearPanel(GuiGraphics graphics, Player player, int x, int y, 
                                              int width, int height, float alpha, InsightHudConfig.ConfigData config) {
        Font font = minecraft.font;
        
        // Enhanced panel background
        RenderHelper.drawRoundedPanel(graphics, x, y, width, height, alpha * 0.95f, true, true);
        
        // Title with enhanced styling
        String title = "Equipment Status";
        int titleX = x + width / 2 - font.width(title) / 2;
        graphics.drawString(font, title, titleX + 1, y + 8 + 1, 0x80000000);
        graphics.drawString(font, title, titleX, y + 8, 0xFFFFFFFF);
          // Equipment items with enhanced rendering
        ItemStack[] gearItems = {
            player.getInventory().getItem(39), // Helmet (slot 39)
            player.getInventory().getItem(38), // Chestplate (slot 38)
            player.getInventory().getItem(37), // Leggings (slot 37)
            player.getInventory().getItem(36), // Boots (slot 36)
            player.getMainHandItem(),          // Main hand
            player.getOffhandItem()            // Offhand
        };
        
        String[] gearNames = {"Helmet", "Chestplate", "Leggings", "Boots", "Main Hand", "Offhand"};
        String[] gearIcons = {"⛑", "⚔", "⚔", "👢", "⚔", "🛡"}; // Unicode icons
        
        int startY = y + 28;
        int slotHeight = 18;
        
        for (int i = 0; i < gearItems.length; i++) {
            ItemStack item = gearItems[i];
            int slotY = startY + i * slotHeight;
            String animKey = "gear_slot_" + i;
            
            // Animate slot appearance
            float slotAlpha = AnimationHelper.animateEased(animKey + "_fade", alpha, 
                                                         100 + i * 50, AnimationHelper.EaseType.EASE_OUT);
            
            if (slotAlpha > 0.01f) {
                // Slot background
                RenderHelper.drawRoundedPanel(graphics, x + 8, slotY - 1, width - 16, slotHeight - 2, 
                                            slotAlpha * 0.3f, false, false);
                
                if (!item.isEmpty()) {
                    // Item rendering with enhanced visuals
                    RenderHelper.drawItemWithDurability(graphics, item, x + 12, slotY, 1.0f, animKey);
                    
                    // Item name
                    String itemName = item.getDisplayName().getString();
                    if (itemName.length() > 20) {
                        itemName = itemName.substring(0, 17) + "...";
                    }
                    
                    int textColor = item.isDamageableItem() && 
                                  (item.getDamageValue() / (float) item.getMaxDamage()) > 0.75f ? 
                                  0xFFFF4444 : 0xFFFFFFFF;
                    
                    graphics.drawString(font, itemName, x + 32, slotY + 1, textColor);
                    
                    // Enhanced durability display
                    if (item.isDamageableItem()) {
                        float durabilityPercent = 1.0f - (item.getDamageValue() / (float) item.getMaxDamage());
                        int barX = x + 32;
                        int barY = slotY + 12;
                        int barWidth = width - 50;
                        
                        RenderHelper.drawAdvancedProgressBar(graphics, durabilityPercent, 
                                                           barX, barY, barWidth, 4, 
                                                           RenderHelper.getDurabilityColor(durabilityPercent), 
                                                           animKey + "_durability", false, null);
                    }
                    
                    // Enchantment indicator
                    if (item.isEnchanted()) {
                        graphics.drawString(font, "✦", x + width - 20, slotY + 1, 0xFFAA00FF);
                    }
                } else {
                    // Empty slot
                    graphics.drawString(font, gearIcons[i] + " " + gearNames[i], x + 12, slotY + 4, 0xFF666666);
                    graphics.drawString(font, "(Empty)", x + width - 50, slotY + 4, 0xFF444444);
                }
            }
        }
        
        // Overall durability status at bottom
        long totalDurability = 0;
        long maxTotalDurability = 0;
        int itemCount = 0;
        
        for (ItemStack item : gearItems) {
            if (!item.isEmpty() && item.isDamageableItem()) {
                totalDurability += item.getMaxDamage() - item.getDamageValue();
                maxTotalDurability += item.getMaxDamage();
                itemCount++;
            }
        }
        
        if (itemCount > 0) {
            float overallDurability = (float) totalDurability / maxTotalDurability;
            String statusText = String.format("Overall: %.1f%%", overallDurability * 100);
            
            int statusY = y + height - 16;
            graphics.drawString(font, statusText, x + 12, statusY, 
                              RenderHelper.getDurabilityColor(overallDurability));
            
            // Overall status bar
            RenderHelper.drawAdvancedProgressBar(graphics, overallDurability, 
                                               x + 80, statusY - 2, width - 100, 8, 
                                               RenderHelper.getDurabilityColor(overallDurability), 
                                               "gear_overall", false, null);
        }
    }
}
