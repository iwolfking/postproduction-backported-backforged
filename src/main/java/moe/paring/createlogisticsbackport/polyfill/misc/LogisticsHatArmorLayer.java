package moe.paring.createlogisticsbackport.polyfill.misc;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.mixin.accessor.AgeableListModelAccessor;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockTickerBlock;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LogisticsHatArmorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public LogisticsHatArmorLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Nullable
    public static PartialModel getHatFor(LivingEntity entity) {
        if (entity == null)
            return null;
        ItemStack headItem = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (!headItem.isEmpty())
            return null;

        int stations = 0;
        Level level = entity.level;
        BlockPos pos = entity.blockPosition();
        PartialModel hat = null;
        for (Direction d : Iterate.horizontalDirections) {
            for (int y : Iterate.zeroAndOne) {
                if (!(level.getBlockState(pos.relative(d)
                                .above(y))
                        .getBlock() instanceof StockTickerBlock lw))
                    continue;
                PartialModel hatOfStation = lw.getHat(level, pos, entity);
                if (hatOfStation == null)
                    continue;
                hat = hatOfStation;
                stations++;
            }
        }

        if (stations == 1)
            return hat;

        return null;
    }

    @Override
    public void render(PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        // Stock-ticker hat rendering (hat info API) not available on Create 0.5.1; no-op on 1.18.2.
    }


    public static void registerOnAll(EntityRenderDispatcher renderManager) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap()
                .values())
            registerOn(renderer);
        for (EntityRenderer<?> renderer : renderManager.renderers.values())
            registerOn(renderer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOn(EntityRenderer<?> entityRenderer) {
        if (!(entityRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer))
            return;

        EntityModel<?> model = livingRenderer.getModel();

        if (!(model instanceof HierarchicalModel) && !(model instanceof AgeableListModel))
            return;

        LogisticsHatArmorLayer<?, ?> layer = new LogisticsHatArmorLayer<>(livingRenderer);
        livingRenderer.addLayer((LogisticsHatArmorLayer) layer);
    }

    private static ModelPart getHeadPart(AgeableListModel<?> model) {
        for (ModelPart part : ((AgeableListModelAccessor) model).create$callHeadParts())
            return part;
        for (ModelPart part : ((AgeableListModelAccessor) model).create$callBodyParts())
            return part;
        return null;
    }

}
