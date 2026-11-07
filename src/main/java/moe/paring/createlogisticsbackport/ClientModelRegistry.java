package moe.paring.createlogisticsbackport;

import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateLogisticsBackport.MODID, value = net.minecraftforge.api.distmarker.Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModelRegistry {

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        // Construct the model resource location for your factory panel block.
        // Change "factory_panel" to whatever your block's registry name is.
        ResourceLocation blockId = new ResourceLocation(CreateLogisticsBackport.MODID, "factory_panel");
        
        // You can also iterate through block state variants if your block uses properties for states
        ModelResourceLocation mrl = new ModelResourceLocation(blockId, "inventory"); // or state string like "facing=north,..."
        
        BakedModel existingModel = event.getModelRegistry().get(mrl);
        if (existingModel != null) {
            FactoryPanelModel customModel = new FactoryPanelModel(existingModel);
            event.getModelRegistry().put(mrl, customModel);
        }
        
        // Note: If your block has multiple blockstate properties (e.g. facings), 
        // you may want to loop through event.getModelRegistry() keys to match your block name 
        // and wrap all its baked models dynamically:
        for (ResourceLocation key : event.getModelRegistry().keySet()) {
            if (key.getNamespace().equals(CreateLogisticsBackport.MODID) && key.getPath().equals("factory_panel")) {
                BakedModel original = event.getModelRegistry().get(key);
                if (original != null && !(original instanceof FactoryPanelModel)) {
                    event.getModelRegistry().put(key, new FactoryPanelModel(original));
                }
            }
        }
    }
}