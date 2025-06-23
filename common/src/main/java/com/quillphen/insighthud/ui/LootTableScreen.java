package com.quillphen.insighthud.ui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

/**
 * Screen for displaying entity loot tables
 */
public class LootTableScreen extends Screen {
    
    private final Screen parent;
    private final LivingEntity entity;
    private final String[] lootItems;
    
    public LootTableScreen(Screen parent, LivingEntity entity) {
        super(Component.translatable("insighthud.loot_table.title", entity.getDisplayName().getString()));
        this.parent = parent;
        this.entity = entity;
        this.lootItems = generateLootPreview(entity);
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Close button
        addRenderableWidget(
            Button.builder(Component.literal("Close"), button -> {
                this.minecraft.setScreen(parent);
            }).bounds(this.width / 2 - 50, this.height - 40, 100, 20).build()
        );
    }
    
    private String[] generateLootPreview(LivingEntity entity) {
        // This is a simplified loot preview
        // In a full implementation, you would access the actual loot tables
        String entityType = entity.getType().toString();
        
        if (entityType.contains("zombie")) {
            return new String[]{"Rotten Flesh", "Iron Ingot (rare)", "Carrot (rare)", "Potato (rare)"};
        } else if (entityType.contains("skeleton")) {
            return new String[]{"Bone", "Arrow", "Bow (rare)"};
        } else if (entityType.contains("cow")) {
            return new String[]{"Raw Beef", "Leather"};
        } else if (entityType.contains("pig")) {
            return new String[]{"Raw Porkchop"};
        } else if (entityType.contains("chicken")) {
            return new String[]{"Raw Chicken", "Feather"};
        } else if (entityType.contains("sheep")) {
            return new String[]{"Raw Mutton", "Wool"};
        } else {
            return new String[]{"No known loot table"};
        }
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        
        // Title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        
        // Entity info
        String entityInfo = "Entity: " + entity.getDisplayName().getString();
        graphics.drawCenteredString(this.font, entityInfo, this.width / 2, 40, 0xCCCCCC);
          // Loot items
        int startY = 70;
        graphics.drawString(this.font, "Possible Loot:", this.width / 2 - 100, startY, 0xFFFFFF);
        
        for (int i = 0; i < lootItems.length; i++) {
            graphics.drawString(this.font, "• " + lootItems[i], this.width / 2 - 90, startY + 20 + (i * 15), 0xAAAAAA);
        }
        
        super.render(graphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}
