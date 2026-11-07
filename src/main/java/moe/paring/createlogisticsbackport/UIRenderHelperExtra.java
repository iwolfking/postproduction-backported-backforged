package moe.paring.createlogisticsbackport;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.Color;
import moe.paring.createlogisticsbackport.mixin.UIRenderHelperAccessor;
import moe.paring.createlogisticsbackport.registry.ExtraGuiTextures;

public class UIRenderHelperExtra {
    public static void drawStretched(PoseStack graphics, int left, int top, int w, int h, int z, ExtraGuiTextures tex) {
        tex.bind();
        UIRenderHelperAccessor.invokeDrawTexturedQuad(graphics.last()
                        .pose(), Color.WHITE, left, left + w, top, top + h, z, tex.getStartX() / 256f, (tex.getStartX() + tex.getWidth()) / 256f,
                tex.getStartY() / 256f, (tex.getStartY() + tex.getHeight()) / 256f);
    }
}
