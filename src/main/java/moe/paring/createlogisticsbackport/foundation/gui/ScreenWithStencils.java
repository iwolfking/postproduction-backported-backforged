package moe.paring.createlogisticsbackport.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public interface ScreenWithStencils {
    default void startStencil(PoseStack graphics, float x, float y, float w, float h) {
        RenderSystem.clear(GL30.GL_STENCIL_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);

        GL11.glDisable(GL11.GL_STENCIL_TEST);
        RenderSystem.stencilMask(~0);
        RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, Minecraft.ON_OSX);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        RenderSystem.stencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
        RenderSystem.stencilMask(0xFF);
        RenderSystem.stencilFunc(GL11.GL_NEVER, 1, 0xFF);

        PoseStack matrixStack = graphics;
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(w, h, 1);
        moe.paring.createlogisticsbackport.DrawHelper.fillGradient(graphics, 0, 0, 1, 1, 0xff000000, 0xff000000);
        matrixStack.popPose();

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        RenderSystem.stencilFunc(GL11.GL_EQUAL, 1, 0xFF);
    }

    default void endStencil() {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

}
