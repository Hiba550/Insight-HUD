package com.quillphen.insighthud.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Utility class for rendering mob icons in the HUD
 */
public class MobIconRenderer {
    
    private static final Minecraft minecraft = Minecraft.getInstance();
    
    /**
     * Renders a mob icon for the given entity
     */
    public static void renderMobIcon(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        if (entity == null) return;
        
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        
        try {
            // Try to render the entity head/icon based on entity type
            if (entity instanceof Player player) {
                renderPlayerHead(graphics, player, x, y, size);
            } else {
                // For non-player entities, try to get their head item or render a simple icon
                ItemStack headItem = getEntityHeadItem(entity);
                if (!headItem.isEmpty()) {
                    renderItemIcon(graphics, headItem, x, y, size);
                } else {
                    renderGenericMobIcon(graphics, entity, x, y, size);
                }
            }
        } catch (Exception e) {
            // Fallback to a simple colored square if rendering fails
            renderFallbackIcon(graphics, entity, x, y, size);
        } finally {
            poseStack.popPose();
        }
    }
      /**
     * Renders a player's head
     */
    private static void renderPlayerHead(GuiGraphics graphics, Player player, int x, int y, int size) {
        // For now, just render a simple player icon
        renderGenericMobIcon(graphics, player, x, y, size);
    }    /**
     * Gets an appropriate head item for the entity type
     */
    private static ItemStack getEntityHeadItem(LivingEntity entity) {
        // Map common entity types to their corresponding head items
        if (entity instanceof Zombie) {
            return new ItemStack(Items.ZOMBIE_HEAD);
        } else if (entity instanceof Skeleton) {
            return new ItemStack(Items.SKELETON_SKULL);
        } else if (entity instanceof Creeper) {
            return new ItemStack(Items.CREEPER_HEAD);
        } else if (entity instanceof WitherSkeleton) {
            return new ItemStack(Items.WITHER_SKELETON_SKULL);
        } else if (entity instanceof Pig) {
            return new ItemStack(Items.PORKCHOP);
        } else if (entity instanceof Cow) {
            return new ItemStack(Items.BEEF);
        } else if (entity instanceof Chicken) {
            return new ItemStack(Items.FEATHER);
        } else if (entity.getType() == EntityType.SHEEP) {
            return new ItemStack(Items.WHITE_WOOL);
        } else if (entity instanceof Spider) {
            return new ItemStack(Items.SPIDER_EYE);
        } else if (entity.getType() == EntityType.ENDERMAN) {
            return new ItemStack(Items.ENDER_PEARL);
        } else if (entity instanceof Witch) {
            return new ItemStack(Items.POTION);
        } else if (entity instanceof Villager) {
            return new ItemStack(Items.EMERALD);
        } else if (entity.getType() == EntityType.CAT) {
            return new ItemStack(Items.COD);
        } else if (entity.getType() == EntityType.WOLF) {
            return new ItemStack(Items.BONE);
        } else if (entity.getType() == EntityType.HORSE) {
            return new ItemStack(Items.SADDLE);
        } else if (entity instanceof Blaze) {
            return new ItemStack(Items.BLAZE_ROD);
        } else if (entity instanceof Ghast) {
            return new ItemStack(Items.GHAST_TEAR);
        } else if (entity instanceof Slime) {
            return new ItemStack(Items.SLIME_BALL);
        }
        
        // For entities without specific head items, return empty
        return ItemStack.EMPTY;
    }
    
    /**
     * Renders an item icon
     */
    private static void renderItemIcon(GuiGraphics graphics, ItemStack item, int x, int y, int size) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        
        // Scale the item to fit the desired size
        float scale = size / 16.0f;
        poseStack.scale(scale, scale, 1.0f);
        
        // Adjust position for scaling
        int scaledX = (int) (x / scale);
        int scaledY = (int) (y / scale);
        
        graphics.renderItem(item, scaledX, scaledY);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a generic mob icon based on entity type
     */
    private static void renderGenericMobIcon(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        // Get entity-specific color
        int color = getEntityColor(entity);
        
        // Draw a simple colored square with a border
        graphics.fill(x, y, x + size, y + size, color);
        graphics.fill(x, y, x + size, y + 1, 0xFF000000); // Top border
        graphics.fill(x, y + size - 1, x + size, y + size, 0xFF000000); // Bottom border
        graphics.fill(x, y, x + 1, y + size, 0xFF000000); // Left border
        graphics.fill(x + size - 1, y, x + size, y + size, 0xFF000000); // Right border
        
        // Add a simple icon pattern
        drawSimpleEntityPattern(graphics, entity, x, y, size);
    }    /**
     * Gets a color associated with the entity type
     */
    private static int getEntityColor(LivingEntity entity) {
        if (entity instanceof Zombie) return 0xFF4CAF50; // Green
        if (entity instanceof Skeleton) return 0xFFE0E0E0; // Light Gray
        if (entity instanceof Creeper) return 0xFF8BC34A; // Light Green
        if (entity instanceof Spider) return 0xFF5D4037; // Dark Brown
        if (entity instanceof Witch) return 0xFF9C27B0; // Purple
        if (entity instanceof Villager) return 0xFF8D6E63; // Brown
        if (entity instanceof Cow) return 0xFF8D6E63; // Brown
        if (entity instanceof Pig) return 0xFFE91E63; // Pink
        if (entity instanceof Chicken) return 0xFFFFFFFF; // White
        if (entity instanceof Blaze) return 0xFFFF5722; // Orange Red
        if (entity instanceof Ghast) return 0xFFF5F5F5; // White Smoke
        if (entity instanceof Slime) return 0xFF4CAF50; // Green
        if (entity instanceof Endermite) return 0xFF424242; // Dark Gray
        
        // Check by EntityType for entities not directly instanceof-able
        EntityType<?> entityType = entity.getType();
        if (entityType == EntityType.SHEEP) return 0xFFF5F5F5; // White
        if (entityType == EntityType.ENDERMAN) return 0xFF212121; // Very Dark Gray
        if (entityType == EntityType.CAT) return 0xFFFF9800; // Orange
        if (entityType == EntityType.WOLF) return 0xFF795548; // Brown
        if (entityType == EntityType.HORSE) return 0xFF8D6E63; // Brown
        if (entityType == EntityType.LLAMA) return 0xFFD7CCC8; // Light Brown
        if (entityType == EntityType.RABBIT) return 0xFFBCAAA4; // Light Brown
        if (entityType == EntityType.PANDA) return 0xFFFFFFFF; // White
        if (entityType == EntityType.FOX) return 0xFFFF5722; // Orange Red
        if (entityType == EntityType.BEE) return 0xFFFFEB3B; // Yellow
        
        // Players get a special color
        if (entity instanceof Player) return 0xFF2196F3; // Blue
        
        // Default color for unknown entities
        return 0xFF607D8B; // Blue Gray
    }
    
    /**
     * Draws a simple pattern inside the entity icon
     */
    private static void drawSimpleEntityPattern(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        
        // Draw simple eyes for most entities
        int eyeSize = Math.max(1, size / 8);
        int eyeOffset = size / 4;
        
        // Left eye
        graphics.fill(centerX - eyeOffset, centerY - eyeSize, 
                     centerX - eyeOffset + eyeSize, centerY, 0xFF000000);
        
        // Right eye
        graphics.fill(centerX + eyeOffset - eyeSize, centerY - eyeSize, 
                     centerX + eyeOffset, centerY, 0xFF000000);
          // Add entity-specific features
        if (entity instanceof Creeper) {
            // Creeper mouth
            graphics.fill(centerX - 1, centerY + eyeSize, centerX + 1, centerY + eyeSize + 2, 0xFF000000);
        }
    }
    
    /**
     * Renders a fallback icon when everything else fails
     */
    private static void renderFallbackIcon(GuiGraphics graphics, LivingEntity entity, int x, int y, int size) {
        // Simple colored square as last resort
        int color = 0xFF404040; // Dark gray
        graphics.fill(x, y, x + size, y + size, color);
        
        // White border
        graphics.fill(x, y, x + size, y + 1, 0xFFFFFFFF);
        graphics.fill(x, y + size - 1, x + size, y + size, 0xFFFFFFFF);
        graphics.fill(x, y, x + 1, y + size, 0xFFFFFFFF);
        graphics.fill(x + size - 1, y, x + size, y + size, 0xFFFFFFFF);
        
        // Question mark in the center
        graphics.drawCenteredString(minecraft.font, "?", x + size / 2, y + size / 2 - 4, 0xFFFFFFFF);
    }
}
