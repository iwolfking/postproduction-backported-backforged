package moe.paring.createlogisticsbackport.registry;

import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelSetItemMenu;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelSetItemScreen;
import moe.paring.createlogisticsbackport.content.logistics.filter.PackageFilterMenu;
import moe.paring.createlogisticsbackport.content.logistics.filter.PackageFilterScreen;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortMenu;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortScreen;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterMenu;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterScreen;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockKeeperCategoryMenu;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockKeeperCategoryScreen;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockKeeperRequestMenu;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockKeeperRequestScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

import static moe.paring.createlogisticsbackport.CreateLogisticsBackport.REGISTRATE;

public class ExtraMenuTypes {
    public static final MenuEntry<StockKeeperCategoryMenu> STOCK_KEEPER_CATEGORY =
            ExtraMenuTypes.<StockKeeperCategoryMenu, StockKeeperCategoryScreen>register("stock_keeper_category", StockKeeperCategoryMenu::new, () -> (menu, inv, title) -> new StockKeeperCategoryScreen(menu, inv, title));

    public static final MenuEntry<StockKeeperRequestMenu> STOCK_KEEPER_REQUEST =
            ExtraMenuTypes.<StockKeeperRequestMenu, StockKeeperRequestScreen>register("stock_keeper_request", StockKeeperRequestMenu::new, () -> (menu, inv, title) -> new StockKeeperRequestScreen(menu, (Inventory) inv, title));

    public static final MenuEntry<PackagePortMenu> PACKAGE_PORT =
            ExtraMenuTypes.<PackagePortMenu, PackagePortScreen>register("package_port", PackagePortMenu::new, () -> (menu, inv, title) -> new PackagePortScreen(menu, inv, title));

    public static final MenuEntry<RedstoneRequesterMenu> REDSTONE_REQUESTER =
            ExtraMenuTypes.<RedstoneRequesterMenu, RedstoneRequesterScreen>register("redstone_requester", RedstoneRequesterMenu::new, () -> (menu, inv, title) -> new RedstoneRequesterScreen(menu, inv, title));

    public static final MenuEntry<PackageFilterMenu> PACKAGE_FILTER =
            ExtraMenuTypes.<PackageFilterMenu, PackageFilterScreen>register("package_filter", PackageFilterMenu::new, () -> (menu, inv, title) -> new PackageFilterScreen(menu, inv, title));

    public static final MenuEntry<FactoryPanelSetItemMenu> FACTORY_PANEL_SET_ITEM =
            ExtraMenuTypes.<FactoryPanelSetItemMenu, FactoryPanelSetItemScreen>register("factory_panel_set_item", FactoryPanelSetItemMenu::new, () -> (menu, inv, title) -> new FactoryPanelSetItemScreen(menu, inv, title));


    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
            String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return REGISTRATE
                .menu(name, factory, screenFactory)
                .register();
    }

    public static void register() {
    }
}
