package moe.paring.createlogisticsbackport.mixin;

import com.mojang.math.Matrix4f;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.utility.Color;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(UIRenderHelper.class)
public interface UIRenderHelperAccessor {
    @Invoker("drawTexturedQuad")
    static void invokeDrawTexturedQuad(Matrix4f matrix, Color color, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
        throw new UnsupportedOperationException();
    }
}
