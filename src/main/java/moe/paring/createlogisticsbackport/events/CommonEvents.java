package moe.paring.createlogisticsbackport.events;

import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import moe.paring.createlogisticsbackport.config.ExtraConfigs;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ServerChainConveyorHandler;
import moe.paring.createlogisticsbackport.foundation.utility.TickBasedCache;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        TickBasedCache.tick();
        ServerChainConveyorHandler.tick();
    }

    @SubscribeEvent
    public static void onServerWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (event.world.isClientSide()) return;

        CreateLogisticsBackport.LOGISTICS.tick(event.world);
    }

    @SubscribeEvent
    public void onLoadWorld(WorldEvent.Load event) {
        CreateLogisticsBackport.LOGISTICS.levelLoaded(event.getWorld());
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        if (!ExtraConfigs.server().suppressWarningMessage.get())
            if (event.getEntity() instanceof Player player && !player.level.isClientSide()) {
                player.displayClientMessage(new net.minecraft.network.chat.TextComponent("Post Production Backported is installed. Any contents from create 6 may have critical bugs and use at your own risk.").withStyle(ChatFormatting.YELLOW), false);
            }
    }
}
