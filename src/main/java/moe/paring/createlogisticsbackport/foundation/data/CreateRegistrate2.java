package moe.paring.createlogisticsbackport.foundation.data;

import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import moe.paring.createlogisticsbackport.foundation.block.connected.ConnectedTextureBehaviour2;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public class CreateRegistrate2 {
    public static <T extends Block> NonNullConsumer<? super T> connectedTextures(
            Supplier<ConnectedTextureBehaviour2> behavior) {
        return entry -> onClient(() -> () -> registerCTBehviour(entry, behavior));
    }

    protected static void onClient(Supplier<Runnable> toRun) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, toRun);
    }

    @OnlyIn(Dist.CLIENT)
    private static void registerCTBehviour(Block entry, Supplier<ConnectedTextureBehaviour2> behaviorSupplier) {
        // CTModel2 (ModelData-based) disabled during 1.18.2 port
    }
}
