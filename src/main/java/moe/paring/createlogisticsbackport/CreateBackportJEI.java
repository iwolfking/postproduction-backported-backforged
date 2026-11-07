package moe.paring.createlogisticsbackport;

import com.simibubi.create.compat.jei.GhostIngredientHandler;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import moe.paring.createlogisticsbackport.compat.jei.StockKeeperTransferHandler;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@ParametersAreNonnullByDefault
public class CreateBackportJEI implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(CreateLogisticsBackport.MODID, "jei_plugin");

    public static IJeiRuntime runtime;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new StockKeeperTransferHandler(registration.getJeiHelpers()), RecipeTypes.CRAFTING.getUid());
    }


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    }
}
