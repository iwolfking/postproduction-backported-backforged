package moe.paring.createlogisticsbackport.content.logistics.tableCloth;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.Iterate;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import java.util.Random;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TableClothModel extends BakedModelWrapperWithData {

	private static final ModelProperty<CullData> CULL_PROPERTY = new ModelProperty<>();

	private static final Map<TableClothBlock, List<List<BakedQuad>>> CORNERS = new HashMap<>();

	public TableClothModel(BakedModel originalModel) {
		super(originalModel);
	}

	public static void reload() {
		CORNERS.clear();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	private List<BakedQuad> getCorner(TableClothBlock block, int corner, Random rand) {
		if (!CORNERS.containsKey(block)) {
			TextureAtlasSprite targetSprite = getParticleIcon(new ModelDataMap.Builder().build());
			List<List<BakedQuad>> list = new ArrayList<>();

 			for (PartialModel pm : List.of(ExtraPartialModels.TABLE_CLOTH_SW, ExtraPartialModels.TABLE_CLOTH_NW,
				ExtraPartialModels.TABLE_CLOTH_NE, ExtraPartialModels.TABLE_CLOTH_SE))
				list.add(getCornerQuads(rand, targetSprite, pm));

			CORNERS.put(block, list);
		}

		return CORNERS.get(block)
			.get(corner);
	}

	private List<BakedQuad> getCornerQuads(Random rand, TextureAtlasSprite targetSprite, PartialModel pm) {
		List<BakedQuad> quads = new ArrayList<>();

		for (BakedQuad quad : pm.get()
			.getQuads(null, null, rand, new ModelDataMap.Builder().build())) {
			TextureAtlasSprite original = quad.getSprite();
			BakedQuad newQuad = BakedQuadHelper.clone(quad);
			int[] vertexData = newQuad.getVertices();
			for (int vertex = 0; vertex < 4; vertex++) {
				BakedQuadHelper.setU(vertexData, vertex, targetSprite
					.getU(SpriteShiftEntry.getUnInterpolatedU(original, BakedQuadHelper.getU(vertexData, vertex))));
				BakedQuadHelper.setV(vertexData, vertex, targetSprite
					.getV(SpriteShiftEntry.getUnInterpolatedV(original, BakedQuadHelper.getV(vertexData, vertex))));
			}
			quads.add(newQuad);
		}

		return quads;
	}

	@Override
	protected void gatherModelData(ModelDataMap.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
		IModelData blockEntityData) {
		List<Direction> culledSides = new ArrayList<>();
		for (Direction side : Iterate.horizontalDirections)
			if (!Block.shouldRenderFace(state, world, pos, side, pos.relative(side)))
				culledSides.add(side);
		if (culledSides.isEmpty())
			return;
		builder.withInitial(CULL_PROPERTY, new CullData(EnumSet.copyOf(culledSides)));
	}

 	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
		@NotNull Random rand, @NotNull IModelData extraData) {
		@NotNull
		List<BakedQuad> mainQuads = super.getQuads(state, side, rand, extraData);
		if (side == null || side.getAxis() == Axis.Y)
			return mainQuads;

		CullData cullData = extraData.getData(CULL_PROPERTY);
		if (cullData != null && cullData.culled()
			.contains(side.getClockWise()))
			return mainQuads;
		if (state == null || !(state.getBlock() instanceof TableClothBlock dcb))
			return mainQuads;

		List<BakedQuad> copyOf = new ArrayList<>(mainQuads);
		copyOf.addAll(getCorner(dcb, side.get2DDataValue(), rand));
		return copyOf;
	}

	private static record CullData(EnumSet<Direction> culled) {
	}

}
