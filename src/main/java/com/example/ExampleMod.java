package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.Arrays;
import java.util.List;

public class ExampleMod implements ClientModInitializer {
    
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nafp.opengui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.nafp"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openGuiKey.wasPressed() && client.currentScreen == null) {
                client.setScreen(new NafpMenuScreen());
            }
        });
    }

    public static class NafpMenuScreen extends Screen {
        private final List<String> categories = Arrays.asList("Movement", "Combat", "Render", "World");
        private String selectedCategory = "Movement";

        public NafpMenuScreen() {
            super(Text.literal("NAFP Client Menu"));
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.renderBackground(context, mouseX, mouseY, delta);

            int pX = this.width / 2 - 200;
            int pY = this.height / 2 - 120;
            int pWidth = 400;
            int pHeight = 240;
            int sidebarWidth = 100;

            context.fill(pX, pY, pX + pWidth, pY + pHeight, 0xCC111111); 
            context.fill(pX, pY, pX + pWidth, pY + 30, 0xFF0A0A0A);         
            context.fill(pX, pY + 30, pX + sidebarWidth, pY + pHeight, 0xFF141414); 
            
            context.drawBorder(pX, pY, pWidth, pHeight, 0xFF222222);
            context.fill(pX + sidebarWidth, pY + 30, pX + sidebarWidth + 1, pY + pHeight, 0xFF222222);

            context.drawText(this.textRenderer, "§bNAFP§r CLIENT // Main Menu", pX + 15, pY + 10, 0xFFEEEEEE, false);

            int currentY = pY + 45;
            for (String cat : categories) {
                boolean isHovered = mouseX >= pX && mouseX <= pX + sidebarWidth && mouseY >= currentY && mouseY <= currentY + 20;
                boolean isSelected = cat.equals(selectedCategory);
                
                int textColor = isSelected ? 0xFF55FFFF : (isHovered ? 0xFFFFFFFF : 0xFF888888);
                context.drawText(this.textRenderer, cat, pX + 15, currentY + 5, textColor, false);
                currentY += 25;
            }

            int contentX = pX + sidebarWidth + 25;
            int contentY = pY + 45;
            context.drawText(this.textRenderer, "Category: " + selectedCategory, contentX, contentY, 0xFFBBBBBB, false);
            
            context.fill(contentX, contentY + 20, contentX + 140, contentY + 50, 0xFF222222);
            context.drawBorder(contentX, contentY + 20, 140, 30, 0xFF333333);
            context.drawText(this.textRenderer, "[ Toggle Module ]", contentX + 15, contentY + 31, 0xFFAAAAAA, false);

            super.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            int pX = this.width / 2 - 200;
            int pY = this.height / 2 - 120;
            int sidebarWidth = 100;

            int currentY = pY + 45;
            for (String cat : categories) {
                if (mouseX >= pX && mouseX <= pX + sidebarWidth && mouseY >= currentY && mouseY <= currentY + 20) {
                    this.selectedCategory = cat;
                    return true; 
                }
                currentY += 25;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean shouldCloseOnEsc() {
            return true; 
        }
    }
}
