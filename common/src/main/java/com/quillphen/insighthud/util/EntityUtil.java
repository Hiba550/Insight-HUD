package com.quillphen.insighthud.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Utility class for entity-related operations
 */
public class EntityUtil {
    
    /**
     * Gets the entity currently being targeted by the player's crosshair
     * @return The targeted LivingEntity, or null if none
     */
    public static LivingEntity getTargetedEntity() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) minecraft.hitResult;
            Entity entity = entityHit.getEntity();
            if (entity instanceof LivingEntity) {
                return (LivingEntity) entity;
            }
        }
        return null;
    }
    
    /**
     * Checks if the given entity is a valid target for the HUD
     * @param entity The entity to check
     * @return true if the entity should be displayed in the HUD
     */
    public static boolean isValidTarget(LivingEntity entity) {
        if (entity == null) return false;
        
        // Don't show HUD for the player themselves
        Minecraft minecraft = Minecraft.getInstance();
        if (entity == minecraft.player) return false;
        
        // Entity must be alive
        return entity.isAlive();
    }
}
