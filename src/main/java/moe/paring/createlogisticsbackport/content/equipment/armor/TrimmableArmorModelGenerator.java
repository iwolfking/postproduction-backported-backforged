package moe.paring.createlogisticsbackport.content.equipment.armor;

import com.simibubi.create.Create;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import moe.paring.createlogisticsbackport.registry.ExtraArmorMaterials;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Map;

public class TrimmableArmorModelGenerator {
    public static final VarHandle TEXTURES_HANDLE;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(ModelBuilder.class, MethodHandles.lookup());
            TEXTURES_HANDLE = lookup.findVarHandle(ModelBuilder.class, "textures", Map.class);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends ArmorItem> void generate(DataGenContext<Item, T> c, RegistrateItemModelProvider p) {
        // Trim model generation (1.20+) not available on 1.18.2; intentionally no-op.
    }

}

