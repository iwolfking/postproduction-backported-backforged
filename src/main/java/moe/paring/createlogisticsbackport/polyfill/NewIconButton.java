package moe.paring.createlogisticsbackport.polyfill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import com.simibubi.create.AllKeys;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import moe.paring.createlogisticsbackport.registry.ExtraGuiTextures;

public class NewIconButton extends IconButton {
    public boolean green;
    public NewIconButton(int x, int y, ScreenElement icon) {
        super(x, y, icon);
    }

    @Override
    public void renderButton(PoseStack graphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

            Object button = !active ? ExtraGuiTextures.BUTTON_DISABLED
                    : isHovered && AllKeys.isMouseButtonDown(0) ? AllGuiTextures.BUTTON_DOWN
                    : isHovered ? AllGuiTextures.BUTTON_HOVER
                    : green ? ExtraGuiTextures.BUTTON_GREEN : AllGuiTextures.BUTTON;

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            drawBgUnified(graphics, button);
            icon.render(graphics, x + 1, y + 1);
        }
    }

    protected void drawBgUnified(PoseStack graphics, Object button) {
        if (button instanceof AllGuiTextures t) {
            RenderSystem.setShaderTexture(0, t.location);
            GuiComponent.blit(graphics, x, y, 0, (float) t.startX, (float) t.startY, t.width, t.height, 256, 256);
            return;
        }
        if (button instanceof ExtraGuiTextures t) {
            t.bind();
            GuiComponent.blit(graphics, x, y, 0, (float) t.getStartX(), (float) t.getStartY(), t.getWidth(), t.getHeight(), 256, 256);
            return;
        }
        throw new IllegalArgumentException();
    }
}
