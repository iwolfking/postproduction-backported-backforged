package moe.paring.createlogisticsbackport;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class DrawHelper {
    public static void drawString(PoseStack g, Font font, Component text, int x, int y, int color, boolean shadow) {
        if (shadow) font.drawShadow(g, text, x, y, color);
        else font.draw(g, text, x, y, color);
    }

    public static void drawString(PoseStack g, Font font, String text, int x, int y, int color, boolean shadow) {
        if (shadow) font.drawShadow(g, text, x, y, color);
        else font.draw(g, text, x, y, color);
    }

    public static void drawString(PoseStack g, Font font, FormattedCharSequence text, int x, int y, int color, boolean shadow) {
        if (shadow) font.drawShadow(g, text, x, y, color);
        else font.draw(g, text, x, y, color);
    }

    public static void setFocused(Object w, boolean v) {
        try {
            w.getClass().getMethod("setFocused", boolean.class).invoke(w, v);
        } catch (Exception ignored) {
        }
    }

    private static class FillProxy extends GuiComponent {
        static void fill(PoseStack poseStack, int x1, int y1, int x2, int y2, int color1, int color2) {
            GuiComponent.fillGradient(poseStack, x1, y1, x2, y2, 0, color1, color2);
        }
    }

    public static void fillGradient(PoseStack poseStack, int x1, int y1, int x2, int y2, int color1, int color2) {
        FillProxy.fill(poseStack, x1, y1, x2, y2, color1, color2);
    }
}
